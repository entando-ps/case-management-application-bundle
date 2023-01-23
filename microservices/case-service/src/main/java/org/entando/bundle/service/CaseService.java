package org.entando.bundle.service;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.entity.Process;
import org.entando.bundle.entity.enumeration.State;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CaseService {

  /**
   * Return all processes FULL information
   * @return the list of the processes
   */
  List<Process> getAllProcesses();

  /**
   * Persist a complete process object in the database
   * @param process the object
   * @return the saved process
   */
  Process saveProcess(Process process);

  /**
   * Return the desired process
   * @param id process ID
   * @return the desired process
   */
  Optional<Process> getProcess(Long id);

  /**
   * Delete the process record from the database
   * @param id the process ID
   */
  void deleteProcess(Long id);

  /**
   * Update the state of the desired process
   * @param id the process ID
   * @param state the new state
   */
  @Transactional
  void updateState(Long id, State state);

  /**
   * Create a process starting from the metadata and attachment information and persist it in the database
   * @param files the attachment (multipart)
   * @param data the metadata of the process
   * @return the saved process
   */
  Process createProcess(MultipartFile[] files, CaseMetadata data);

  /**
   * Delete the process attachments and then delete the record from the DB.
   * If attachments are not deleted the process is kept with status DELETED, this allow
   * for sequent retry
   * @param id the process to delete
   * @return tru if everything ok, false otherwise
   */
  boolean destroyProcess(Long id);

}
