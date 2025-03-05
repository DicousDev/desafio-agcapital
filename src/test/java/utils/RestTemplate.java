package utils;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class RestTemplate {

  private final TestRestTemplate restTemplate;

  public ResponseEntity<String> sendGET(URI uri) {
    return sendJSON(uri, null, String.class, HttpMethod.GET);
  }

  public ResponseEntity<String> sendPOST(URI uri, Object request) {
    return sendJSON(uri, request, String.class, HttpMethod.POST);
  }

  public ResponseEntity<String> sendDELETE(URI uri, Object request) {
    return sendJSON(uri, request, String.class, HttpMethod.DELETE);
  }

  public ResponseEntity<String> sendPATCH(URI uri, Object request) {
    return sendJSON(uri, request, String.class, HttpMethod.PATCH);
  }

  private <T> ResponseEntity<T> sendJSON(URI uri,
      Object request,
      Class<T> clazz,
      HttpMethod httpMethod) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.putIfAbsent(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_JSON_VALUE));
      HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);
      return restTemplate.exchange(uri, httpMethod, requestEntity, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
