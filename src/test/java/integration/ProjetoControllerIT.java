package integration;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import utils.ITemplate;
import utils.ResponseEntityAssert;

public class ProjetoControllerIT extends ITemplate {

  private static final String BASE_URL = "projetos";

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveConsultarProjetosPorCliente() throws URISyntaxException {
    String expected = readJSON("IT/consulta-projetos-clientes-expected.json");
    URI uri = new URIBuilder(toURI(BASE_URL))
        .addParameter("idExternoCliente", "7680a4b2-0a39-4185-82f9-ba5a02fd917d")
        .build();
    ResponseEntity<String> response = restTemplate.sendGET(uri);
    ResponseEntityAssert.assertThat(response)
        .isOk()
        .responseBody(expected);
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveConsultarProjetoEspecifico() {
    String expected = readJSON("IT/consulta-projeto-especifico-expected.json");
    URI uri = toURI(BASE_URL + "/93bae4f2-7002-4b8b-b913-737d44ff5b4b");
    ResponseEntity<String> response = restTemplate.sendGET(uri);
    ResponseEntityAssert.assertThat(response)
        .isOk()
        .responseBody(expected);
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveCadastrarProjeto() {
    String request = readJSON("IT/cadastrar-projeto-request.json");
    String expected = readJSON("IT/cadastrar-projeto-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendPOST(uri, request);
    ResponseEntityAssert.assertThat(response)
        .isCreated()
        .responseBody(expected, "idExterno", "dataHoraCriacao");
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveCadastrarProjetoValidationErrors() {
    String request = readJSON("IT/cadastrar-projeto-validation-errors-request.json");
    String expected = readJSON("IT/cadastrar-projeto-validation-errors-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendPOST(uri, request);
    ResponseEntityAssert.assertThat(response)
        .isBadRequest()
        .responseBody(expected, "timestamp");
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveFalharQuandoNaoEncontrarClienteNoCadastroDeProjeto() {
    String request = readJSON("IT/cadastrar-projeto-cliente-not-found-request.json");
    String expected = readJSON("IT/cadastrar-projeto-cliente-not-found-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendPOST(uri, request);
    ResponseEntityAssert.assertThat(response)
        .is404Found()
        .responseBody(expected, "timestamp");
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveAdicionarAtividadeAoProjeto() {
    String request = readJSON("IT/adiciona-atividade-ao-projeto-request.json");
    String expected = readJSON("IT/adiciona-atividade-ao-projeto-expected.json");
    URI uri = toURI(BASE_URL + "/atividades");
    ResponseEntity<String> response = restTemplate.sendPOST(uri, request);
    ResponseEntityAssert.assertThat(response)
        .isCreated()
        .responseBody(expected, "idExterno", "dataHoraCriacao");
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveAdicionarAtividadeAoProjetoValidationError() {
    String request = readJSON("IT/adiciona-atividade-ao-projeto-validation-error-request.json");
    String expected = readJSON("IT/adiciona-atividade-ao-projeto-validation-error-expected.json");
    URI uri = toURI(BASE_URL + "/atividades");
    ResponseEntity<String> response = restTemplate.sendPOST(uri, request);
    ResponseEntityAssert.assertThat(response)
        .isBadRequest()
        .responseBody(expected, "timestamp");
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveRemoverAtividadeDoProjeto() {
    String request = readJSON("IT/remove-atividade-do-projeto-request.json");
    URI uri = toURI(BASE_URL + "/atividades");
    ResponseEntity<String> response = restTemplate.sendDELETE(uri, request);
    ResponseEntityAssert.assertThat(response).isOk();
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveRemoverAtividadeDoProjetoValidationError() {
    String request = readJSON("IT/remove-atividade-do-projeto-validation-error-request.json");
    String expected = readJSON("IT/remove-atividade-do-projeto-validation-error-expected.json");
    URI uri = toURI(BASE_URL + "/atividades");
    ResponseEntity<String> response = restTemplate.sendDELETE(uri, request);
    ResponseEntityAssert.assertThat(response)
        .isBadRequest()
        .responseBody(expected, "timestamp");
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveVincularClienteAoProjeto() {
    String request = readJSON("IT/vincula-cliente-projeto-request.json");
    URI uri = toURI(BASE_URL + "/clientes");
    ResponseEntity<String> response = restTemplate.sendPOST(uri, request);
    ResponseEntityAssert.assertThat(response).isOk();
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveAlterarDadosDoProjeto() {
    String request = readJSON("IT/altera-dados-projeto-request.json");
    String expected = readJSON("IT/altera-dados-projeto-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendPATCH(uri, request);
    ResponseEntityAssert.assertThat(response)
        .isOk()
        .responseBody(expected, "dataHoraAtualizacao", "dataFinalizacao");
  }
}
