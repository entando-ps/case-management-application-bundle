package org.entando.bundle.config;

import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineServicesConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@Import(SpringProcessEngineServicesConfiguration.class )
public class CamundaConfiguration {

  @Value("${camunda.bpm.history-level:audit}")
  private String historyLevel;

  private final DataSource dataSource;

  private final ResourcePatternResolver resourceLoader;

  public CamundaConfiguration(DataSource dataSource, ResourcePatternResolver resourceLoader) {
    this.dataSource = dataSource;
    this.resourceLoader = resourceLoader;
  }

  @Bean
  public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {
    SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

    config.setDataSource(dataSource);
    config.setDatabaseSchemaUpdate("true");
    config.setTransactionManager(transactionManager());
    config.setProcessEngineName("Process engine for CDP");
    config.setHistory(historyLevel);
    config.setJobExecutorActivate(true);
    config.setMetricsEnabled(false);
    Resource[] resources = resourceLoader.getResources("classpath:/process/*.bpmn");
    config.setDeploymentResources(resources);
    return config;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  public ProcessEngineFactoryBean processEngine() throws IOException {
    ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
    factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
    return factoryBean;
  }

}
