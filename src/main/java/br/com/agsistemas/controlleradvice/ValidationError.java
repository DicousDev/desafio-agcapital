package br.com.agsistemas.controlleradvice;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationError {

  private String field;
  private String value;
}
