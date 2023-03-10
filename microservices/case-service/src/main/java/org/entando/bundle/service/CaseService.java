package org.entando.bundle.service;

import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Statistics;
import org.entando.bundle.entity.Case;
import org.entando.bundle.entity.enumeration.State;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CaseService {

  /**
   * Return all cases FULL information
   * @return the list of cases
   */
  List<Case> getAllCases();

  /**
   * Return all cases belonging to the given user. FULL information is returned
   * @param name the name of the owner of the case
   * @return the list of cases
   */
  List<Case> getCasesByName(String name);

  /**
   * Persist a complete case object in the database
   * @param aCase the object
   * @return the saved case
   */
  Case saveCase(Case aCase);

  /**
   * Return the desired case
   * @param id case ID
   * @return the desired case
   */
  Optional<Case> getCase(Long id);

  /**
   * Return the desired case making sure that it belongs to the given user
   * @param id the case id
   * @param name the name of the owner
   * @return the case
   */
  Optional<Case> getCaseByIdAndOwner(Long id, String name);

  /**
   * Delete the case record from the database
   * @param id the case ID
   */
  void deleteCase(Long id);

  /**
   * Update the state of the desired case
   * @param id the case ID
   * @param state the new state
   */
  @Transactional
  void updateState(Long id, State state);

  /**
   * Create a case starting from the metadata and attachment information and persist it in the database
   *
   * @param files   the attachment (multipart)
   * @param data    the metadata of the case
   * @param ownerId the principal name of the user making the request
   * @return the saved case
   */
  Case createCase(MultipartFile[] files, CaseMetadata data, String ownerId);

  /**
   * Delete the case attachments and then delete the record from the DB.
   * If attachments are not deleted the case is kept with status DELETED, this allows
   * for sequent retry
   * @param id the case to delete
   * @return tru if everything ok, false otherwise
   */
  boolean destroyCase(Long id);

  /**
   * Complete the task for the process held by the desired state updating the variables
   * @param id    case ID
   * @param props properties to be inserted in the process
   * @return true if the task was completed and the process run to completion, false if no process found
   * @throws RuntimeException if the task was completed and the process is still running
   */
  boolean completeTaskState(Long id, Map<String, Object> props);

  /**
   * Complete the task for the process held by the desired state updating the variables
   * If properties are null, them we create them with the current time value for
   * 'PROCESS_INSTANCE_VARIABLES_LAST_UPDATE'
   * @param optCase optional case object
   * @param props properties to be inserted in the process
   * @return true if the task was completed and the process run to completion, false if no process found
   * @throws RuntimeException if the task was completed and the process is still running
   */
  boolean completeTaskState(Optional<Case> optCase, Map<String, Object> props);

  /**
   * Return the case by date range
   *  @param from starting date, can be null
   *  @param to ending date, must be null if 'from' is null
   * @return the case in the desired interval (if at least from is specified). Otherwise,
   * it behaves like 'getAllCases'
   */
  List<Case> getCaseByDate(LocalDate from, LocalDate to);

  /**
   * Get the statistics in the desired period (if any)
   * @param from starting date, can be null
   * @param to ending date, must be null if 'from' is null, otherwise the current instant is used if none passed
   * @return the statistics
   */
  Statistics getStatisticsRange(LocalDate from, LocalDate to);

  /**
   * Create fake cases
   * @param size the number of case to create
   */
  void createFakeData(int size);

  /**
   * Delete all the cases
   */
  void flush();
}
