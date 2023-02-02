package org.entando.bundle.service.impl;

import org.camunda.bpm.engine.task.Task;
import org.entando.bundle.CaseServiceApplication;
import org.entando.bundle.domain.AuthorizedUser;
import org.entando.bundle.domain.CaseMetadata;
import org.entando.bundle.domain.Delegation;
import org.entando.bundle.domain.SubscribedUser;
import org.entando.bundle.entity.Case;
import org.entando.bundle.repository.CaseRepository;
import org.entando.bundle.service.CamundaService;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.entando.bundle.BundleConstants.USER_TASK_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableJpaRepositories(basePackages = "org.entando.bundle.repository")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = CaseServiceApplication.class)
//@Transactional
public class CaseServiceImplTest {

  @Autowired
  private CamundaService bpmService;

  @Autowired
  private CaseServiceImpl caseService;

  @Autowired
  private CaseRepository caseRepository;

  @Test
  public void testContext() {
    assertThat(caseService, is(notNullValue()));
    assertThat(bpmService, is(notNullValue()));
    assertThat(caseRepository, is(notNullValue()));
  }

  @Test
  public void testCreateNoAttachments() {
    Case c = null;
    CaseMetadata metadata = getCaseMetadata();
    try {
      // to startup process etc etc
      c = caseService.createCase(null, metadata, "test");
      assertThat(c, is(notNullValue()));
//      assertThat(c.getId(), is(notNullValue()));
      assertThat(c.getProcessInstanceId(), is(notNullValue()));
      assertTrue(bpmService.isProcessRunning(c.getProcessInstanceId()));
    } catch (Throwable t) {
      t.printStackTrace();
      throw t;
    } finally {
      if (c != null && c.getId()!= null) {
        caseService.deleteCase(c.getId());
      }
      setUserTaskVariablesAndState(c.getProcessInstanceId(), null);
    }
  }

  @Test
  public void testTaskTermination() {
    CaseMetadata metadata = getCaseMetadata();
    Case cp = caseService.createCase(null, metadata, "test");
    assertThat(cp,is(notNullValue()));
    caseService.completeTaskState(Optional.of(cp), null);
    assertFalse(bpmService.isProcessRunning(cp.getProcessInstanceId()));
    assertThat(bpmService.getCompletedProcessData(cp.getProcessInstanceId()), is(notNullValue()));
    assertThat(bpmService.getCompletedProcessData(cp.getProcessInstanceId()).size(), is(1));
  }


  @NotNull
  private CaseMetadata getCaseMetadata() {
    CaseMetadata metadata = new CaseMetadata();
    AuthorizedUser au = new AuthorizedUser();
    SubscribedUser su = new SubscribedUser();
    metadata.setAuthorized(au);
    metadata.setSubscriber(su);

    su.setSector("sector");
    su.setName("test");
    su.setLastname("pest");
    su.setBirthDate(LocalDate.now());
    su.setBirthCountry("Italy");
    su.setBirthCity("Cagliari");
    su.setBirthProvince("CA");
    su.setBirthRegion("Sardinia");
    su.setFiscalCode("MTNMTN77J07B745U");
    su.setEmail("su@email.it");
    su.setLandline("048161832");
    su.setMobile("3283285328");
    su.setSector("sector");
    su.setDelegation(Delegation.LEGALE_RAPPRESENTANTE);

    au.setRole("role");
    au.setName("test");
    au.setLastname("pest");
    au.setBirthDate(LocalDate.now());
    au.setBirthCountry("Italy");
    au.setBirthCity("Carbonia");
    au.setBirthProvince("SU");
    au.setBirthRegion("Sardinia");
    au.setFiscalCode("DVDMTN77J07B745U");
    au.setEmail("su@email.it");
    au.setMobile("3283285328");
    return metadata;
  }

  private void setUserTaskVariablesAndState(String instanceId, Map<String, Object> props) {
    Task task = bpmService.getRunningProcessTask(instanceId, USER_TASK_NAME);
    bpmService.setUserTaskVariables(instanceId, task.getId(), props);
    bpmService.completeTask(task);
  }

}
