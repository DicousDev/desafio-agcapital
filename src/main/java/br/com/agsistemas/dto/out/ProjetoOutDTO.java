package br.com.agsistemas.dto.out;

import br.com.agsistemas.enums.ProjetoStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjetoOutDTO {

  private UUID idExterno;
  private String descricao;
  private ProjetoStatus status;
  private LocalDateTime dataHoraCriacao;
  private LocalDateTime dataHoraAtualizacao;
  private LocalDateTime dataFinalizacao;
}
