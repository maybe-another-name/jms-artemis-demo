package blah;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import blah.PropertiesConfig.ViewedByGeneratorProps;

@Configuration
@EnableConfigurationProperties(ViewedByGeneratorProps.class)
public class BeansConfig {

  @Bean
  public FakePublishAndRead jmsTester(ViewedByGeneratorProps props, JmsTemplate jmsTemplate) {
    return new FakePublishAndRead(jmsTemplate, props.getQueueName());
  }
}
