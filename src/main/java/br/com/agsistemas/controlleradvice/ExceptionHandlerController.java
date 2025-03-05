package br.com.agsistemas.controlleradvice;

import br.com.agsistemas.exception.EntidadeInvalidaException;
import br.com.agsistemas.exception.NaoEncontradoException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
  private final String DETAILS_400_BAD_REQUEST = "Erro no payload.";

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    ApiResponseError response = criaApiResponse(DETAILS_400_BAD_REQUEST, status.value(), request);
    Map<String, String> fieldNameValue = new HashMap<>();
    for(FieldError field : ex.getBindingResult().getFieldErrors()) {
      fieldNameValue.put(field.getField(), field.getDefaultMessage());
    }

    fieldNameValue.entrySet().stream()
        .forEach(x -> response.addValidationError(new ValidationError(x.getKey(), x.getValue())));
    return handleExceptionInternal(ex, response, headers, status, request);
  }

  @ExceptionHandler(NaoEncontradoException.class)
  public ResponseEntity<Object> naoEncontradoException(NaoEncontradoException ex, WebRequest request) {
    return criaResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(EntidadeInvalidaException.class)
  public ResponseEntity<Object> entidadeInvalidaException(EntidadeInvalidaException ex, WebRequest request) {
    return criaResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request);
  }

  private ResponseEntity<Object> criaResponse(String details, HttpStatus status, WebRequest request) {
    var response = criaApiResponse(details, status.value(), request);
    return new ResponseEntity<>(response, status);
  }

  private ApiResponseError criaApiResponse(String details, int httpStatus, WebRequest request) {
    return new ApiResponseError(
        details,
        httpStatus,
        ((ServletWebRequest) request).getRequest().getRequestURI().toString(),
        LocalDateTime.now());
  }
}
