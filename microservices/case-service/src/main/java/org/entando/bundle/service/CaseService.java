package org.entando.bundle.service;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.entity.Process;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public abstract class CaseService {
  public abstract List<Process> getAllProcesses();

  public abstract Process createProcess(Process process);

  public abstract Optional<Process> getProcess(Long id);

  public abstract Process startProcess(MultipartFile[] files, CaseMetadata data);
}
