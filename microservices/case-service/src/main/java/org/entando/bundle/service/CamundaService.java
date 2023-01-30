package org.entando.bundle.service;

import org.camunda.bpm.engine.task.Task;

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
   * Return the desired user task for the current running process
   * @param instanceId the process instance ID
   * @param taskName the task ID
   * @return the task, null otherwise
   */
  Task getRunningProcessTask(String instanceId, String taskName);

  /**
   * Specify the variables for the given task
   * @param instanceId process instance ID
   * @param taskId task ID
   * @param props properties to write
   */
  void setUserTaskVariables(String instanceId, String taskId, Map<String, Object> props);

  /**
   * Mark user task completed
   * @param task the task object
   */
  void completeTask(Task task);

  /**
   * Mark user task completed
   * @param taskId the task id
   */
  void completeTask(String taskId);

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
