package org.entando.bundle.service.impl;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Resource;
import org.entando.bundle.entity.Case;
import org.entando.bundle.entity.enumeration.State;
import org.entando.bundle.repository.CaseRepository;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.CaseIdentityService;
import org.entando.bundle.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;

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

  @Autowired
  private CaseRepository caseRepository;

  public CaseServiceImpl(CaseIdentityService caseIdentityService, FileService fileService) {
    this.caseIdentityService = caseIdentityService;
    this.fileService = fileService;
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
  public Case saveProcess(Case aCase) {
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
  public void deleteProcess(Long id) {
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
      fileService.fileUpload(file, new HashMap<>());
    }
    data.setResources(resources);
    aCase.setMetadata(data);
    aCase.setCreated(LocalDateTime.now());
    aCase.setIdentifier(progressive);
    aCase.setState(State.CREATED);
    aCase.setPid(2677L); // FIXME
    aCase.setOwnerId(name);
    // persist
    saveProcess(aCase);
    // TODO start the related process and change state
    return aCase;
  }

  @Override
  @Transactional
  public boolean destroyCase(Long id) {
    boolean deleted = true;

    Optional<Case> process = getCase(id);
    if (process.isPresent()) {
      // delete the resources
      deleted = deleteProcessResources(process.get());
      if (deleted) {
        // delete from DB
        deleteProcess(id);
      } else {
        log.error("Couldn't remove at least one resource from the storage service");
        log.error("the process persisted {} won't be deleted and will be marked \"DELETED\"", id);
        //  update state -> DELETED
        updateState(id, State.DELETED);
      }
    }
    return deleted;
  }

  /**
   * Delete aCase resources from storage service
   * @param aCase the aCase object to delete
   * @return true if the aCase was successfully deleted, false otherwise
   */
  private boolean deleteProcessResources(Case aCase) {
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

  public void stopProcess() {
  }

  public void resumeProcess() {
  }

}
