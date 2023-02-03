package org.entando.bundle.service.impl;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.entando.bundle.service.CamundaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.entando.bundle.BundleConstants.PROCESS_INSTANCE_KEY;
import static org.entando.bundle.BundleConstants.USER_TASK_NAME;

@Service
public class CamundaServiceImpl implements CamundaService {

  private final Logger log = LoggerFactory.getLogger(CamundaServiceImpl.class);

  private final HistoryService historyService;

  private final RuntimeService runtimeService;

  private final TaskService taskService;

  public CamundaServiceImpl(HistoryService historyService, RuntimeService runtimeService, TaskService taskService) {
    this.historyService = historyService;
    this.runtimeService = runtimeService;
    this.taskService = taskService;
  }

  @Override
  public String startProcess() {
    ProcessInstance proc = runtimeService.startProcessInstanceByKey(PROCESS_INSTANCE_KEY);
    log.info("Created new process instance {}", proc.getProcessInstanceId());
    return proc.getProcessInstanceId();
  }

  /**
   * Return the process variables of a completed process
   * @param instanceId the instanceId of the process
   * @return the process variables
   */
  protected List<HistoricVariableInstance> getProcessVariables(final String instanceId) {
    return historyService.createHistoricVariableInstanceQuery()
      .processInstanceId(instanceId)
      .orderByVariableName()
      .asc()
      .list();
  }

  @Override
  public Map<String, Object> getCompletedProcessData(final String instanceId) {
    Map<String, Object> res = null;
    List<HistoricVariableInstance> vars;

    if (StringUtils.isNotBlank(instanceId)
      && (vars = getProcessVariables(instanceId)) != null) {

      res = vars.stream()
        .collect(Collectors.toMap(HistoricVariableInstance::getName, HistoricVariableInstance::getValue));
    }
    return res;
  }

  @Override
  public Task getRunningProcessTask(final String instanceId, final String taskName) {
    return taskService.createTaskQuery()
      .processInstanceId(instanceId)
      .taskName(taskName).singleResult();
  }

  @Override
  public void setUserTaskVariables(final String instanceId, final String taskId, final Map<String, Object> props) {
    if (StringUtils.isNotBlank(instanceId)) {
      Task task;

      if ((task = getRunningProcessTask(instanceId, USER_TASK_NAME)) != null) {
        if (props != null && !props.isEmpty()) {
          props.forEach((key, value) -> taskService.setVariable(task.getId(), key, value));
        }
      } else {
        log.debug("Invalid process specification");
      }
    }
  }

  @Override
  public void completeTask(Task task) {
      completeTask(task.getId());
  }

  @Override
  public void completeTask(String taskId) {
    taskService.complete(taskId);
  }

  @Override
  public boolean isProcessRunning(String instanceId) {
    return (runtimeService.createProcessInstanceQuery()
      .processInstanceId(instanceId).singleResult() != null);
  }

  @Override
  public void deleteProcess(String instanceId) {
    if (StringUtils.isNotBlank(instanceId) && isProcessRunning(instanceId)) {
      runtimeService.deleteProcessInstance(instanceId, "no reason");
      log.info("deleted running process {}", instanceId);
    } else if (StringUtils.isNotBlank(instanceId)) {
      historyService.deleteHistoricProcessInstanceIfExists(instanceId);
      log.info("deleted completed process {}", instanceId);
    } else {
      log.debug("Invalid process to delete");
    }
  }

}
