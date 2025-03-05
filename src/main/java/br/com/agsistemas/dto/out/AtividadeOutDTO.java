package br.com.agsistemas.dto.out;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AtividadeOutDTO {

  private UUID idExterno;
  private String titulo;
  private String descricao;
  private LocalDateTime dataHoraCriacao;
  private ClienteOutDTO responsavel;
}
