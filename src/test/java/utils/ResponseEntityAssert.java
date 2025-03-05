package utils;

import java.util.ArrayList;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityAssert extends AbstractCustomAssert<ResponseEntityAssert, ResponseEntity<String>> {

  protected ResponseEntityAssert(ResponseEntity<String> o) {
    super(o, ResponseEntityAssert.class);
  }

  public static ResponseEntityAssert assertThat(ResponseEntity<String> actual) {
    return new ResponseEntityAssert(actual);
  }

  public ResponseEntityAssert isOk() {
    status(HttpStatus.OK);
    return this;
  }

  public ResponseEntityAssert isCreated() {
    status(HttpStatus.CREATED);
    return this;
  }

  public ResponseEntityAssert isBadRequest() {
    status(HttpStatus.BAD_REQUEST);
    return this;
  }

  public ResponseEntityAssert is404Found() {
    status(HttpStatus.NOT_FOUND);
    return this;
  }

  public ResponseEntityAssert responseBody(String expected, String ... ignore) {
    String message = "current payload: \n" + this.actual.getBody() + "\n";

    if(Arrays.isNullOrEmpty(ignore)) {
      JSONAssert.assertEquals(message, expected, this.actual.getBody(),
        new CustomComparator(JSONCompareMode.STRICT, new Customization[] {}));
    }

    Collection<Customization> customizations = new ArrayList<>();
    for(String fieldIgnore : ignore) {
      customizations.add(new Customization(fieldIgnore, (o1, o2) -> true));
    }

    JSONAssert.assertEquals(message, expected, this.actual.getBody(),
        new CustomComparator(JSONCompareMode.STRICT, customizations.toArray(new Customization[customizations.size()])));

    return this;
  }

  private ResponseEntityAssert status(HttpStatus status) {
    Assertions.assertThat(actual.getStatusCode()).isEqualTo(status);
    return this;
  }
}
