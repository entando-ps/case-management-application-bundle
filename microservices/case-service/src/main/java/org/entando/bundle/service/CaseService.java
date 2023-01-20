package org.entando.bundle.service;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.entity.Process;
import org.entando.bundle.entity.enumeration.State;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CaseService {

  List<Process> getAllProcesses();

  Process saveProcess(Process process);

  Optional<Process> getProcess(Long id);

  void deleteProcess(Long id);

  @Transactional
  void updateState(Long id, State state);

  Process createProcess(MultipartFile[] files, CaseMetadata data);

  boolean destroyProcess(Long id);

}
