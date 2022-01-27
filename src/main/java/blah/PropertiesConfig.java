package blah;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

import lombok.Builder;
import lombok.Getter;

@Configuration
public class PropertiesConfig {

  @ConfigurationProperties("payload.viewed-by-generator")
  @Builder
  @Getter
  @ConstructorBinding
  public static class ViewedByGeneratorProps {
    private final String queueName;
  }

}
