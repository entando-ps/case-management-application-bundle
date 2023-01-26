package org.entando.bundle.service;

import java.util.Map;

public interface CamundaService {

  /**
   * Run a new process
   * @return
   */
  String startProcess();

  /**
   * Get the process variables
   * @param instanceId process instance ID
   * @return the map of the requested variables
   */
  Map<String, Object> getProcessData(String instanceId);

  /**
   * Update the user task with the given variables and close if desired
   * @param instanceId process ID
   * @param complete if true complete the task
   * @param props the properties to inject in the process
   */
  void setUserTaskVariablesAndState(String instanceId, boolean complete, Map<String, Object> props);

  /**
   * Check whether the process is running or not
   * NOTE: we don't check if the process exists!
   * @param instanceId
   * @return true if the prcess is running, false otherwise
   */
  boolean isProcessRunning(String instanceId);

  /**
   * Delete the given process
   * @param instanceId the process id
   */
  void deleteProcess(String instanceId);

}
