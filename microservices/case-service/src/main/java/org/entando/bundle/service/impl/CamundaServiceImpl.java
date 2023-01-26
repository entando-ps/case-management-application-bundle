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

  private Logger log = LoggerFactory.getLogger(CamundaServiceImpl.class);

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
  public Map<String, Object> getProcessData(final String instanceId) {
    Map<String, Object> res = null;
    List<HistoricVariableInstance> vars;

    if (StringUtils.isNotBlank(instanceId)
      && (vars = getProcessVariables(instanceId)) != null) {

      res = vars.stream()
//        .peek(t -> log.debug("Getting variable {} : {} from process {}", t.getName(), t.getValue(), instanceId))
        .collect(Collectors.toMap(t -> t.getName(), t -> t.getValue()));
    }
    return res;
  }


  /**
   * Return the desired user task for the current running process
   * @param instanceId the process instance ID
   * @param taskName the task ID
   * @return the task, null otherwise
   */
  private Task getRunningProcessTask(final String instanceId, final String taskName) {
    return taskService.createTaskQuery()
      .processInstanceId(instanceId)
      .taskName(taskName).singleResult();
  }

  @Override
  public void setUserTaskVariablesAndState(final String instanceId, boolean complete, Map<String, Object> props) {
    if (StringUtils.isNotBlank(instanceId)) {
      Task task;

      if ((task = getRunningProcessTask(instanceId, USER_TASK_NAME)) != null) {
        if (props != null && !props.isEmpty()) {
          props.entrySet()
//            .stream().peek(e -> log.debug("setting variable {} : {} to task {}", e.getKey(), e.getValue(), task.getId()))
            .forEach(e -> taskService.setVariable(task.getId(), e.getKey(), e.getValue()));
        }
        // finally
        if (complete) {
          log.debug("marking complete user task {}", task.getId());
          taskService.complete(task.getId());
        } else {
          log.debug("leaving user task {} start unchanged", task.getId());
        }
      } else {
        log.debug("Invalid process specification");
      }
    }
  }

  @Override
  public boolean isProcessRunning(String instanceId) {
    return (runtimeService.createProcessInstanceQuery()
      .processInstanceId(instanceId).singleResult() != null);
  }

  @Override
  public void deleteProcess(String instanceId) {
    if (StringUtils.isNotBlank(instanceId)) {
      runtimeService.deleteProcessInstance(instanceId, "no reason");
      log.info("deleted process {}", instanceId);
    }
  }

}
