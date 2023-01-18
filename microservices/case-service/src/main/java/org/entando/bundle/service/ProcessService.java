package org.entando.bundle.service;

import org.entando.bundle.entity.Process;
import org.entando.bundle.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcessService {

  @Autowired
  private ProcessRepository processRepository;

  public List<Process> getAllProcesses() {
    return processRepository.findAll();
  }

  public Process crateProcess(Process process) {
    return processRepository.save(process);
  }

  public Optional<Process> getProcess(Long id) {
    return processRepository.findById(id);
  }

}
