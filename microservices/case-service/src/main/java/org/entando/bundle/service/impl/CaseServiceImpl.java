package org.entando.bundle.service.impl;

import org.camunda.bpm.engine.task.Task;
import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Resource;
import org.entando.bundle.entity.Case;
import org.entando.bundle.entity.enumeration.State;
import org.entando.bundle.repository.CaseRepository;
import org.entando.bundle.service.CamundaService;
import org.entando.bundle.service.CaseIdentityService;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.entando.bundle.BundleConstants.PROCESS_INSTANCE_VARIABLES_LAST_UPDATE;
import static org.entando.bundle.BundleConstants.USER_TASK_NAME;

@Service
public class CaseServiceImpl implements CaseService {


  private Logger log = LoggerFactory.getLogger(CaseServiceImpl.class);

  private final CaseIdentityService caseIdentityService;

  private final FileService fileService;

  private final CamundaService bpmService;

  @Autowired
  private CaseRepository caseRepository;

  public CaseServiceImpl(CaseIdentityService caseIdentityService, FileService fileService, CamundaService bpmService) {
    this.caseIdentityService = caseIdentityService;
    this.fileService = fileService;
    this.bpmService = bpmService;
  }

  @Override
  public List<Case> getAllCases() {
    return caseRepository.findAll().stream()
      .map(c -> syncWithProcessData(c))
      .collect(Collectors.toList());
  }

  @Override
  public List<Case> getCasesByName(String name) {
    return caseRepository.findByOwnerId(name).stream()
      .map(c -> syncWithProcessData(c))
      .collect(Collectors.toList());
  }

  @Override
  public Case saveCase(Case aCase) {
    return caseRepository.save(aCase);
  }

  @Override
  public Optional<Case> getCase(Long id) {
    Optional<Case> dbCase = caseRepository.findById(id);
    return Optional.ofNullable(dbCase.map(c -> syncWithProcessData(c)).orElse(null));
  }

  @Override
  public Optional<Case> getCaseByIdAndOwner(Long id, String name) {
    Optional<Case> dbCase =  caseRepository.findByIdAndOwnerIdIs(id,name);
    return Optional.ofNullable(dbCase.map(c -> syncWithProcessData(c)).orElse(null));
  }

  @Override
  public void deleteCase(Long id) {
    caseRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void updateState(Long id, State state) {
    caseRepository.updateState(id, state);
  }

  @Override
  @Transactional
  public Case createCase(MultipartFile[] files, CaseMetadata data, String name) {
    List<Resource> resources = new ArrayList<>();
    Case aCase = new Case();

    final String progressive = caseIdentityService.generateIdentifier();
    log.debug("Using progressive {}", progressive);

    if (files != null && files.length > 0) {
      // track resource name
      for (MultipartFile file : files) {
        Resource resource = new Resource();
        String fileName = file.getOriginalFilename();

        resource.setKey(fileName);
        resource.setUrl(fileService.getFilePublicUrlNoCheck(fileName));
        resource.setSize(file.getSize());
        resources.add(resource);
        log.info("adding resource {} (size: {}) to case metadata", file.getOriginalFilename(), file.getSize());
        // upload resource
        if (fileService.fileUpload(file, new HashMap<>()) == null) {
          throw new RuntimeException("Could not copy a file in the file repository");
        }
      }
    } else {
      log.debug("No file to attach for the current process");
    }
    final String processId = bpmService.startProcess();
    log.debug("Associating Process {} to the current case", processId);

    data.setResources(resources);
    aCase.setMetadata(data);
    aCase.setCreated(LocalDateTime.now());
    aCase.setIdentifier(progressive);
    aCase.setState(State.CREATED);
    aCase.setProcessInstanceId(processId);
    aCase.setOwnerId(name);
    // persist
    saveCase(aCase);
    log.info("Created case {} with process {}", aCase.getId(), aCase.getProcessInstanceId());
    return syncWithProcessData(aCase);
  }

  @Override
  @Transactional
  public boolean destroyCase(Long id) {
    boolean deleted = true;

    Optional<Case> caseOptional = getCase(id);
    if (caseOptional.isPresent()) {
      // delete the resources
      deleted = deleteCaseResources(caseOptional.get());
      if (deleted) {
        // delete from DB
        deleteCase(id);
      } else {
        log.error("Couldn't remove at least one resource from the storage service");
        log.error("the case persisted {} won't be deleted and will be marked \"DELETED\"", id);
        updateState(id, State.DELETED);
      }
      // delete associated process
      bpmService.deleteProcess(caseOptional.get().getProcessInstanceId());
    }
    return deleted;
  }

  /**
   * Delete aCase resources from storage service
   * @param aCase the aCase object to delete
   * @return true if the aCase was successfully deleted, false otherwise
   */
  private boolean deleteCaseResources(Case aCase) {
    final boolean[] deleted = {true};
    List<Resource> resources = aCase != null && aCase.getMetadata() != null && aCase.getMetadata().getResources() != null ? aCase.getMetadata().getResources() : null;

    if (resources != null && !resources.isEmpty()) {
      resources.forEach(r ->{
        boolean res = fileService.deleteFile(r.getKey());

        if (res) {
          log.info("resource {} successfully removed from aCase {}", r.getKey(), aCase.getId());
        } else {
          log.warn("could not remove resource {} from aCase {}", r.getKey(), aCase.getId());
        }
        deleted[0] = deleted[0] && res;
      });
    }
    return deleted[0];
  }

  /**
   * Update the case with the real state of the underlying process
   * @param aCase the case to be updated
   * @return the updated Case object
   */
  protected Case syncWithProcessData(Case aCase) {
    if (aCase != null && aCase.getState() != State.DELETED) {
      String piid = aCase.getProcessInstanceId();

      log.info("syncing data of case {} with process instance id {} ", aCase.getId(), aCase.getProcessInstanceId());
      if (StringUtils.isNotBlank(piid)) {
        if (bpmService.isProcessRunning(piid)) {
          aCase.setState(State.RUNNING);
        } else {
          aCase.setState(State.COMPLETED);
        }
        // metadata
        CaseMetadata metadata = aCase.getMetadata();
        Map<String, Object> properties = bpmService.getProcessData(piid);
        if (!properties.isEmpty()) {
          log.debug("properties of process {} updated int case {}", aCase.getProcessInstanceId(), aCase.getId());
          metadata.setProcessData(properties);
        } else {
          log.debug("no properties to attach to case {}", aCase.getId());
        }
      }
    }
    return aCase;
  }

  @Override
  @Transactional
  public boolean completeTaskState(final Long id, Map<String, Object> props) {
    if (id != null) {
      Optional<Case> optCase = getCase(id);
      return completeTaskState(optCase, props);
    }
    return false;
  }

  @Override
  public boolean completeTaskState(Optional<Case> optCase, Map<String, Object> props) {
    final Case cur = optCase.orElse(null);
    boolean completed = false;

    if (cur != null && cur.getState() == State.RUNNING) {
      if (props == null) {
        props = new HashMap<>();
      }
      String instanceId = cur.getProcessInstanceId();
      // update /add the current date time
      props.put(PROCESS_INSTANCE_VARIABLES_LAST_UPDATE, LocalDateTime.now());
      // fetch the user task...
      Task task = bpmService.getRunningProcessTask(instanceId, USER_TASK_NAME);
      // ...update variables...
      bpmService.setUserTaskVariables(instanceId, task.getId(), props);
      // .. complete the task
      bpmService.completeTask(task);
      log.info("Changed user task of process {} of case {}", instanceId, cur.getId());
      if (bpmService.isProcessRunning(instanceId)) {
        throw new RuntimeException("process should have been stopped");
      }
      completed = true;
    } else {
      log.debug("no case to operate to");
    }
    return completed;
  }

}
