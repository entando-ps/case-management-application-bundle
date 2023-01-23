package org.entando.bundle.service.impl;

import org.entando.bundle.entity.Process;
import org.entando.bundle.repository.CaseRepository;
import org.entando.bundle.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaseServiceImpl extends CaseService {

  @Autowired
  private CaseRepository caseRepository;

  @Override
  public List<Process> getAllProcesses() {
    return caseRepository.findAll();
  }

  @Override
  public Process crateProcess(Process process) {
    return caseRepository.save(process);
  }

  @Override
  public Optional<Process> getProcess(Long id) {
    return caseRepository.findById(id);
  }


  public void startProcess() {
    // collect files, save them to tmp directory
    // collect form data
  }

  public void stopProcess() {
  }

  public void resumeProcess() {
  }


}
