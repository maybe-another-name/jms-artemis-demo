package blah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JMSService {
  @Autowired
  private JmsTemplate jmsTemplate;

  @Value("${queue-name}")
  private String queueName;

  @Transactional
  public void send(String parameter) throws Exception {
    jmsTemplate.convertAndSend(queueName, parameter);
  }

  @Transactional
  public String read() throws Exception {
    return (String) jmsTemplate.receiveAndConvert(queueName);
  }
}
