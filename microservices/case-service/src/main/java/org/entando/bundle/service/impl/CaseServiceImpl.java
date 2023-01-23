package org.entando.bundle.service.impl;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Resource;
import org.entando.bundle.entity.Process;
import org.entando.bundle.entity.enumeration.State;
import org.entando.bundle.repository.CaseRepository;
import org.entando.bundle.service.CaseService;
import org.entando.bundle.service.ExternalService;
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

  private final ExternalService externalService;

  private final FileService fileService;

  @Autowired
  private CaseRepository caseRepository;

  public CaseServiceImpl(ExternalService externalService, FileService fileService) {
    this.externalService = externalService;
    this.fileService = fileService;
  }

  @Override
  public List<Process> getAllProcesses() {
    return caseRepository.findAll();
  }

  @Override
  public Process saveProcess(Process process) {
    return caseRepository.save(process);
  }

  @Override
  public Optional<Process> getProcess(Long id) {
    return caseRepository.findById(id);
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
  public Process createProcess(MultipartFile[] files, CaseMetadata data) {
    List<Resource> resources = new ArrayList<>();
    Process process = new Process();

    try {
      final String progressive = externalService.generateIdentifier();
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
      process.setMetadata(data);
      process.setCreated(LocalDateTime.now());
      process.setIdentifier(progressive);
      process.setState(State.CREATED);
      process.setPid(2677L); // FIXME
      // persist
      saveProcess(process);
      // TODO start the process and change state
    } catch (Throwable t) {
      log.error("Error persisting case", t.getLocalizedMessage());
    }
    return process;
  }

  @Override
  @Transactional
  public boolean destroyProcess(Long id) {
    boolean deleted = true;

    try {
        Optional<Process> process = getProcess(id);
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
    } catch (Throwable t) {
      log.error("Error deleting process from DB", t.getMessage());
      t.printStackTrace();
    }
    return deleted;
  }

  /**
   * Delete process resources from storage service
   * @param process
   * @return
   */
  private boolean deleteProcessResources(Process process) {
    final boolean[] deleted = {true};
    List<Resource> resources = process != null && process.getMetadata() != null && process.getMetadata().getResources() != null ? process.getMetadata().getResources() : null;

    if (resources != null && !resources.isEmpty()) {
        resources.stream().forEach(r ->{
          boolean res = fileService.deleteFile(r.getKey());

          if (res) {
            log.info("resource {} successfully removed from process {}", r.getKey(), process.getId());
          } else {
            log.warn("could not remove resource {} from process {}", r.getKey(), process.getId());
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
