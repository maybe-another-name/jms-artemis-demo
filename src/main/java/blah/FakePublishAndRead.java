package blah;

import org.springframework.jms.core.JmsTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class FakePublishAndRead {

  private final JmsTemplate jmsTemplate;

  private final String queueName;

  void publishAndReadToRemote(int n) throws InterruptedException {
    String msg = "fake-msg-" + n;
    jmsTemplate.convertAndSend(queueName, msg);
    log.info("Sent msg: {}", msg);
    Thread.sleep(2000);
    Object receivedMsg = jmsTemplate.receiveAndConvert(queueName);
    log.info("Received msg: {}", receivedMsg);
  }
}
