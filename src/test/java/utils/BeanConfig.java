package utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class BeanConfig implements WebMvcConfigurer {

  @Bean
  public TestRestTemplate testRestTemplate() {
    return new TestRestTemplate(new RestTemplateBuilder());
  }

  @Bean
  public RestTemplate restTemplate(TestRestTemplate restTemplate) {
    return new RestTemplate(restTemplate);
  }
}
