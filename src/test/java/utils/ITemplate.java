package utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import br.com.agsistemas.AgsistemasApplication;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.FileCopyUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("IT")
@SpringBootTest(classes = {AgsistemasApplication.class, BeanConfig.class} , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ITemplate {

  private static final String SERVER_URL = "http://localhost:%s/%s";

  @Autowired
  protected RestTemplate restTemplate;

  @LocalServerPort
  private int port;

  private static final PostgreSQLContainer<?> PG_CONTAINER = new PostgreSQLContainer<>(
      "postgres:16")
      .withDatabaseName("integration-test")
      .withUsername("it-user")
      .withPassword("it-password")
      .withReuse(true);

  static {
    PG_CONTAINER.start();
  }

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", PG_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.password", PG_CONTAINER::getPassword);
    registry.add("spring.datasource.username", PG_CONTAINER::getUsername);
  }

  protected String readJSON(String path) {
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    Resource resource = resourceLoader.getResource(path);
    try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  protected URI toURI(String path) {
    try {
      final String uri = String.format(SERVER_URL, port, path);
      return new URI(uri);
    } catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

}
