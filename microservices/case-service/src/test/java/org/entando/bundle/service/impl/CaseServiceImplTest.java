package org.entando.bundle.service.impl;


import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.entando.bundle.CaseServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
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

  @Test
  public void verifyProcessInstanceStarted() {

    ProcessInstance p1 = runtimeService.startProcessInstanceByKey("approvationRequest");
    ProcessInstance p2 = runtimeService.startProcessInstanceByKey("approvationRequest");

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
        assertThat(false,is(v.getValue()));
      }
    });
  }

}
