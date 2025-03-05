package br.com.agsistemas.controlleradvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ApiResponseError {

  private String details;
  private int status;
  private String path;
  private LocalDateTime timestamp;
  private List<ValidationError> erros;

  public ApiResponseError(String details, int status, String path, LocalDateTime timestamp) {
    this.details = details;
    this.status = status;
    this.path = path;
    this.timestamp = timestamp;
    erros = new ArrayList<>();
  }

  public void addValidationError(ValidationError error) {
    erros.add(error);
  }
}
