package integration;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import utils.ITemplate;
import utils.ResponseEntityAssert;

public class AtividadeControllerIT extends ITemplate {

  private static final String BASE_URL = "atividades";

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveAlterarDadosAtividade() {
    String requestBody = readJSON("IT/alterar-dados-atividade-request.json");
    String expected = readJSON("IT/alterar-dados-atividade-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendPATCH(uri, requestBody);
    ResponseEntityAssert.assertThat(response)
        .isOk()
        .responseBody(expected);
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveFalharQuandoEnviarDadosInvalidosParaAlterarDadosDaAtividade() {
    String requestBody = readJSON("IT/alterar-dados-atividade-validation-error-request.json");
    String expected = readJSON("IT/alterar-dados-atividade-validation-error-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendPATCH(uri, requestBody);
    ResponseEntityAssert.assertThat(response)
        .isBadRequest()
        .responseBody(expected, "timestamp");
  }

  @Test
  @Sql({"classpath:IT/clean.sql", "classpath:IT/insert-all.sql"})
  void deveFalharQuandoNaoEncontrarAtividadeParaAlterarDadosDaAtividade() {
    String requestBody = readJSON("IT/alterar-dados-atividade-not-found-request.json");
    String expected = readJSON("IT/alterar-dados-atividade-not-found-expected.json");
    URI uri = toURI(BASE_URL);
    ResponseEntity<String> response = restTemplate.sendPATCH(uri, requestBody);
    ResponseEntityAssert.assertThat(response)
        .is404Found()
        .responseBody(expected, "timestamp");
  }


}
