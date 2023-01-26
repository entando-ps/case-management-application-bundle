package org.entando.bundle.service.impl;


import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.entando.bundle.CaseServiceApplication;
import org.entando.bundle.service.CamundaService;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.*;
import static org.entando.bundle.BundleConstants.PROCESS_INSTANCE_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CaseServiceApplication.class)
public class CaseServiceImplTest {

  @Autowired
  HistoryService historyService;

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private CamundaService camundaService;

  @Test
  public void verifyProcessInstanceStarted() {
    ProcessInstance p1 = runtimeService.startProcessInstanceByKey(PROCESS_INSTANCE_KEY);
    ProcessInstance p2 = runtimeService.startProcessInstanceByKey(PROCESS_INSTANCE_KEY);

    List<Task> tasks = taskService.createTaskQuery().taskName("Invia richiesta").list();
    tasks.forEach(t -> {
//      System.out.println(">>> " + t.getProcessInstanceId());
      assertThat(t, is(notNullValue()));
      // set variables in task
      taskService.setVariable(t.getId(), "approved", false);
      taskService.setVariable(t.getId(), "PID", t.getProcessInstanceId());
      assertEquals(false,  taskService.getVariable(t.getId(), "approved"));
      // complete the task
      taskService.complete(t.getId());
    });
    // no process running
    assertThat(runtimeService.createProcessInstanceQuery().count(), is(0L));
    // retrieve values from EXPIRED process p1
    List<HistoricVariableInstance> vars = historyService
      .createHistoricVariableInstanceQuery()
      .processInstanceId(p1.getProcessInstanceId())
      .orderByVariableName()
      .asc()
      .list();

    // verify values injected in task
    assertEquals(2, vars.size());
    HistoricVariableInstanceEntity historicVariable = (HistoricVariableInstanceEntity) vars.get(0);
    vars.forEach(v -> {
      if (v.getName().equals("PID")) {
        assertThat(p1.getProcessInstanceId(),is(v.getValue()));
      } else {
        assertThat("approved",is(v.getName()));
        assertThat(false, is(v.getValue()));
      }
    });
  }

  @Test
  public void testServiceAndClose() {
    // start
    final String instanceId = camundaService.startProcess();
    assertThat(instanceId, is(notNullValue()));

    // update
    camundaService.setUserTaskVariablesAndState(instanceId, true, Map.of("nice",true));
    // retrieve
    Map<String, Object> vars = camundaService.getProcessData(instanceId);
    assertThat(vars, is(notNullValue()));
    assertThat(1, is(vars.size()));
    assertThat(true, is(vars.get("nice")));

    assertFalse(camundaService.isProcessRunning(instanceId));
  }

  @Test
  public void testServiceAndKeepRunning() {
    String instanceId = null;

    try {
      // start
      instanceId = camundaService.startProcess();
      assertThat(instanceId, is(notNullValue()));
      // update
      camundaService.setUserTaskVariablesAndState(instanceId, false, Map.of("nice", true));
      // retrieve
      Map<String, Object> vars = camundaService.getProcessData(instanceId);
      assertThat(vars, is(notNullValue()));
      assertThat(1, is(vars.size()));
      assertThat(true, is(vars.get("nice")));
      // still running
      assertTrue(camundaService.isProcessRunning(instanceId));
    } catch (Throwable t) {
        throw t;
    } finally {
      if (StringUtils.isNotBlank(instanceId)) {
        camundaService.deleteProcess(instanceId);
      }
    }
  }

  @Test
  public void testDelete() {
    final String instanceId = camundaService.startProcess();
    assertTrue(camundaService.isProcessRunning(instanceId));
    camundaService.deleteProcess(instanceId);
    assertFalse(camundaService.isProcessRunning(instanceId));
  }

  @Test
  public void testUnknownProcessForVariables() {
    List<HistoricVariableInstance> varz = historyService
      .createHistoricVariableInstanceQuery()
      .processInstanceId("some-process")
      .orderByVariableName()
      .asc()
      .list();
    assertTrue(varz.isEmpty());
  }

}
