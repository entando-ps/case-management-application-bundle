package org.entando.bundle.service.impl;

import org.camunda.bpm.engine.task.Task;
import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Resource;
import org.entando.bundle.domain.StatisticElement;
import org.entando.bundle.domain.Statistics;
import org.entando.bundle.entity.Case;
import org.entando.bundle.entity.enumeration.State;
import org.entando.bundle.repository.CaseRepository;
import org.entando.bundle.service.CamundaService;
import org.entando.bundle.service.CaseIdentityService;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.FileService;
import org.entando.bundle.service.impl.utils.FakerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.utils.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.entando.bundle.BundleConstants.*;

@Service
public class CaseServiceImpl implements CaseService {


  private final Logger log = LoggerFactory.getLogger(CaseServiceImpl.class);

  private final CaseIdentityService caseIdentityService;

  private final FileService fileService;

  private final CamundaService bpmService;

  private final CaseRepository caseRepository;

  public CaseServiceImpl(CaseIdentityService caseIdentityService, FileService fileService, CamundaService bpmService, CaseRepository caseRepository) {
    this.caseIdentityService = caseIdentityService;
    this.fileService = fileService;
    this.bpmService = bpmService;
    this.caseRepository = caseRepository;
  }

  @Override
  public List<Case> getAllCases() {
    return caseRepository.findAll().stream()
      .map(this::syncWithProcessData)
      .collect(Collectors.toList());
  }

  @Override
  public List<Case> getCasesByName(String name) {
    return caseRepository.findByOwnerId(name).stream()
      .map(this::syncWithProcessData)
      .collect(Collectors.toList());
  }

  @Override
  public Case saveCase(Case aCase) {
    return caseRepository.save(aCase);
  }

  @Override
  public Optional<Case> getCase(Long id) {
    Optional<Case> dbCase = caseRepository.findById(id);
    return dbCase.map(this::syncWithProcessData);
  }

  @Override
  public Optional<Case> getCaseByIdAndOwner(Long id, String name) {
    Optional<Case> dbCase =  caseRepository.findByIdAndOwnerIdIs(id,name);
    return dbCase.map(this::syncWithProcessData);
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
  public Case createCase(MultipartFile[] files, CaseMetadata data, String ownerId) {
    Case newCase = processCaseMetadata(files, data);
    processCaseFields(ownerId, newCase, LocalDateTime.now());
    return syncWithProcessData(newCase);
  }

  private Case processCaseMetadata(MultipartFile[] files, CaseMetadata metadata) {
    List<Resource> resources = new ArrayList<>();
    Case currentCase = new Case();

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
    // track metadata information
    metadata.setResources(resources);
    currentCase.setMetadata(metadata);
    return currentCase;
  }

  /**
   * After metadata has been taken care of, let's complete the remaining fields
   * @param ownerId      the username of the creator
   * @param currentCase  the case object
   * @param creationDate the creation date of the process
   * @return a completed amd persisted Case object
   */
  private Case processCaseFields(String ownerId, Case currentCase, LocalDateTime creationDate) {
    final String processId = bpmService.startProcess();
    log.debug("Associating Process {} to the current case", processId);

    final String progressive = caseIdentityService.generateIdentifier();
    log.debug("Using progressive {}", progressive);

    currentCase.setCreated(creationDate);
    currentCase.setIdentifier(progressive);
    currentCase.setState(State.CREATED);
    currentCase.setProcessInstanceId(processId);
    currentCase.setOwnerId(ownerId);
    // persist
    saveCase(currentCase);
    log.info("Created case {} with process {} (state: {})", currentCase.getId(), currentCase.getProcessInstanceId(), currentCase.getState());
    return currentCase;
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
   * @param currentCase the case to be updated
   * @return the updated Case object
   */
  protected Case syncWithProcessData(Case currentCase) {
    if (currentCase != null && currentCase.getState() != State.DELETED) {
      String processInstanceId = currentCase.getProcessInstanceId();

      log.info("syncing data of case {} with process instance id {} ", currentCase.getId(), currentCase.getProcessInstanceId());
      if (StringUtils.isNotBlank(processInstanceId)) {
        if (bpmService.isProcessRunning(processInstanceId)) {
          currentCase.setState(State.RUNNING);
        } else {
          currentCase.setState(State.COMPLETED);
        }
        // metadata
        CaseMetadata metadata = currentCase.getMetadata();
        Map<String, Object> properties = bpmService.getCompletedProcessData(processInstanceId);
        if (!properties.isEmpty()) {
          log.debug("properties of process {} updated int case {}", currentCase.getProcessInstanceId(), currentCase.getId());
          metadata.setProcessData(properties);
        } else {
          log.debug("no properties to attach to case {}", currentCase.getId());
        }
      }
    }
    return currentCase;
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

      if (!props.containsKey(PROCESS_INSTANCE_VARIABLES_LAST_UPDATE)) {
        // set update time if not specified
        props.put(PROCESS_INSTANCE_VARIABLES_LAST_UPDATE, LocalDateTime.now());
      }
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

  @Override
  public Statistics getStatisticsRange(LocalDate from, LocalDate to) {
    List<Case> cases = getCaseByDate(from, to);
    return extractStatistics(cases);
  }

  @Override
  public List<Case> getCaseByDate(LocalDate from, LocalDate to) {
    if (from != null) {
      if (to == null) {
        to = LocalDate.now();
      }
      return caseRepository.findByCreatedAfterAndCreatedBefore(from.atStartOfDay(),
          to.atTime(LocalTime.now())).stream()
        .map(this::syncWithProcessData)
        .collect(Collectors.toList());
    }
    return getAllCases();
  }

  /**
   * Return a subset of cases data useful for the statistics
   * @param cases case list to analyze
   * @return the statistics
   */
  private Statistics extractStatistics(final List<Case> cases) {
    final Statistics stats = new Statistics();
    final StatisticElement overall = new StatisticElement();

    stats.setByStatus(overall);
    if (cases != null && !cases.isEmpty()) {
      cases.forEach(c -> {
        if (c.getState() == State.RUNNING) {
          final StatisticElement creationYearElement = stats.getYear(c.getCreated().getYear());
          // add to opened...
          overall.addOpen();
          creationYearElement.addOpen();
        } else if (c.getState() == State.COMPLETED) {
          final LocalDateTime openingDate = c.getCreated();
          final LocalDateTime completedDate = (LocalDateTime) c.getMetadata().getProcessData().get(PROCESS_INSTANCE_VARIABLES_LAST_UPDATE);
          final StatisticElement completedYearElement = stats.getYear(completedDate.getYear());
          final StatisticElement openingYearElement = stats.getYear(openingDate.getYear());

          openingYearElement.addOpen();
          if ((boolean)c.getMetadata().getProcessData().get(PROCESS_INSTANCE_VARIABLES_APPROVED)) {
            // ..add approved...
            completedYearElement.addApproved();
            overall.addApproved();
          } else {
            // ...add rejected...
            completedYearElement.addRejected();
            overall.addRejected();
          }
        } else {
          // add fault
//          stats.addFaulty(elem);
        }
      });
    }
    return stats;
  }

  /**
   * Create a random but likely case.
   * @param creationDate the creation date
   * @param approve true will complete the task
   * @return the case created
   */
  private Case createFakeCase(LocalDateTime creationDate, Boolean approve) {
    Case newCase = new Case();
    Resource resource = new Resource();
    // fake metadata...
    CaseMetadata metadata = FakerHelper.getRandomMetadata();

    newCase.setMetadata(metadata);
    // generate the resource to upload
    String resourceToUpload = metadata.toString();
    String key = metadata.getAuthorized().getLastname() + "_" + metadata.getSubscriber().getLastname() + ".txt";
    resource.setSize((long) resourceToUpload.length());
    resource.setUrl(fileService.getFilePublicUrlNoCheck(key));
    resource.setKey(key);
    if (fileService.fileUpload(resourceToUpload, key, new HashMap<>()) == null) {
      throw new RuntimeException("Could not copy a file in the file repository");
    }
    metadata.setResources(Arrays.asList(resource));
    // persist case and run the process
    processCaseFields(metadata.getAuthorized().getFiscalCode(), newCase, creationDate);
    // sync internal state
    syncWithProcessData(newCase);
    // approve or reject?
    if (approve != null) {
      LocalDateTime closeTime = creationDate.plusHours(12);
      HashMap<String, Object> vars = new HashMap<>();

      vars.put(PROCESS_INSTANCE_VARIABLES_APPROVED, approve);
      vars.put(PROCESS_INSTANCE_VARIABLES_LAST_UPDATE, closeTime);
      if (!completeTaskState(Optional.of(newCase), vars)) {
        log.error("error completing the task");
      }
      log.info("case {} was closed at {} with approval: {}", newCase.getId(), closeTime, approve);
    } else {
      log.debug("leaving case {} running", newCase.getId());
    }
    return syncWithProcessData(newCase);
  }

  @Override
  public void createFakeData(int size) {
    IntStream.range(0, size)
      .forEach(c -> {
        LocalDate caseDate = FakerHelper.getRandomPastDate(4, 0);
        // close the process?
        boolean caseClose = FakerHelper.trueFalse();
        // if yes select the final state
        Boolean caseApprove = caseClose ? FakerHelper.trueFalse() : null;
        createFakeCase(caseDate.atStartOfDay(), caseApprove);
        log.info("Created case #{}", c);
      });
  }

  @Override
  public void flush() {
    List<Case> cases = getAllCases();

    if (cases != null && !cases.isEmpty()) {
        cases.forEach(c -> {
          try {
            destroyCase(c.getId());
          } catch (Throwable t) {
            log.error("erro deleting case #" + c, t);
          }
        });
    }
  }

}
