package integration;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import utils.ITemplate;
import utils.ResponseEntityAssert;

public class ClienteControllerIT extends ITemplate {

  private static final String BASE_URL = "clientes";

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveConsultarTodosClientes() {
    String expected = readJSON("IT/consulta-todos-clientes-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendGET(uri);
    ResponseEntityAssert.assertThat(response)
        .isOk()
        .responseBody(expected);
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveCadastrarCliente() {
    String requestBody = readJSON("IT/cadastro-cliente-request.json");
    String expected = readJSON("IT/cadastro-cliente-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendPOST(uri, requestBody);
    ResponseEntityAssert.assertThat(response)
        .isCreated()
        .responseBody(expected, "idExterno");
  }
}
