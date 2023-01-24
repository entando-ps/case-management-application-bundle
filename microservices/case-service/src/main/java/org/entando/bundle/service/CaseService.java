package org.entando.bundle.service;

import org.entando.bundle.entity.Process;

import java.util.List;
import java.util.Optional;

public abstract class CaseService {
  public abstract List<Process> getAllProcesses();

  public abstract Process crateProcess(Process process);

  public abstract Optional<Process> getProcess(Long id);
}
