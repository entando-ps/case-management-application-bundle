package org.entando.bundle.service.impl;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
    return caseRepository.findAll();
  }

  @Override
  public List<Case> getCasesByName(String name) {
    return caseRepository.findByOwnerId(name);
  }

  @Override
  public Case saveCase(Case aCase) {
    return caseRepository.save(aCase);
  }

  @Override
  public Optional<Case> getCase(Long id) {
    return caseRepository.findById(id);
  }

  @Override
  public Optional<Case> getCaseByIdAndOwner(Long id, String name) {
    return caseRepository.findByIdAndOwnerIdIs(id,name);
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

    // track resource name
    for (MultipartFile file: files) {
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
    final String processId = bpmService.startProcess();
    log.debug("Associating Process {} to the current case");

    data.setResources(resources);
    aCase.setMetadata(data);
    aCase.setCreated(LocalDateTime.now());
    aCase.setIdentifier(progressive);
    aCase.setState(State.VALID);
    aCase.setProcessId(processId);
    aCase.setOwnerId(name);
    // persist
    saveCase(aCase);
    log.info("Created case {} with process {}", aCase.getId(), aCase.getProcessId());
    return aCase;
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
      bpmService.deleteProcess(caseOptional.get().getProcessId());
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


}
