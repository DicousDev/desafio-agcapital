package br.com.agsistemas.dto.out;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClienteOutDTO {

  private UUID idExterno;
  private String nome;
}
