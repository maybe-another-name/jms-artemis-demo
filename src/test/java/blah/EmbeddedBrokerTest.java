package blah;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.jms.ConnectionFactory;

import org.apache.activemq.artemis.core.config.impl.SecurityConfiguration;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.spi.core.security.ActiveMQJAASSecurityManager;
import org.apache.activemq.artemis.spi.core.security.jaas.InVMLoginModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import blah.EmbeddedBrokerTest.EmbeddedJmsConfig;

@SpringBootTest(classes = JMSService.class)
@Import(EmbeddedJmsConfig.class)
public class EmbeddedBrokerTest {

  private static EmbeddedActiveMQ embedded;

  @Autowired
  private JMSService jmsService;

  @BeforeAll
  public static void setup() throws Exception {
    SecurityConfiguration configuration = new SecurityConfiguration();
    configuration.addUser("user", "password");
    configuration.addRole("user", "admin");

    embedded = new EmbeddedActiveMQ();

    final ActiveMQJAASSecurityManager securityManager = new ActiveMQJAASSecurityManager(
        InVMLoginModule.class.getName(), configuration);
    embedded.setSecurityManager(securityManager);

    embedded.start();
  }

  @Test
  public void testReceive() throws Exception {
    assertDoesNotThrow(() -> jmsService.send("trysmething"));
    Thread.sleep(2000);
    assertEquals("trysmething", jmsService.read());
  }

  @TestConfiguration
  static class EmbeddedJmsConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.artemis.user}")
    private String user;

    @Value("${spring.artemis.password}")
    private String password;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
      ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
      try {
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUser(user);
        connectionFactory.setPassword(password);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return connectionFactory;
    }

    public ConnectionFactory cachingConnectionFactory() {
      CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory());
      cachingConnectionFactory.setCacheProducers(true);
      cachingConnectionFactory.setSessionCacheSize(5);
      return new CachingConnectionFactory(connectionFactory());
    }

    @Bean
    @Primary
    public JmsTemplate jmsTemplate() {
      return new JmsTemplate(cachingConnectionFactory());
    }
  }
}
