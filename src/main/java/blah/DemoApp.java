package blah;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// @SpringBootApplication
@AllArgsConstructor
@Slf4j
public class DemoApp implements ApplicationRunner {
  public static void main(String[] args) {
    log.info("Starting app");
    SpringApplication.run(DemoApp.class, args);
  }

  private final FakePublishAndRead jmsThing;

  public void run(ApplicationArguments args) {
    log.info("Starting runner...");
    try {
      jmsThing.publishAndReadToRemote(1);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
