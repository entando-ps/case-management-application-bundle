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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CaseServiceImpl extends CaseService {

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
  public Process createProcess(Process process) {
    return caseRepository.save(process);
  }

  @Override
  public Optional<Process> getProcess(Long id) {
    return caseRepository.findById(id);
  }

  @Override
  public Process startProcess(MultipartFile[] files, CaseMetadata data) {
    List<Resource> resources = new ArrayList<>();
    Process process = new Process();

    try {
      final String progressive = externalService.generateIdentifier();
      log.info("Using progressive {}", progressive);

      // track resource name
      for (MultipartFile file: files) {
        Resource resource = new Resource();
        String fileName = file.getOriginalFilename();

        resource.setKey(fileName);
        resource.setUrl(fileService.getFilePublicUrlNoCheck(fileName));
        resource.setSize(file.getSize());
        resources.add(resource);
        log.info("adding resource {} (size: {}) to case metadata", file.getOriginalFilename(), file.getSize());
        // TODO upload resource
      }
      data.setResources(resources);
      process.setMetadata(data);
      process.setCreated(LocalDateTime.now());
      process.setIdentifier(progressive);
      process.setState(State.CREATED);
      process.setPid(2677L);
      // persist
      createProcess(process);
      // TODO start the process and change state
    } catch (Throwable t) {
      log.error("Error persisting case", t.getLocalizedMessage());
      t.printStackTrace();
    }
    return process;
  }

  public void stopProcess() {
  }

  public void resumeProcess() {
  }

}
