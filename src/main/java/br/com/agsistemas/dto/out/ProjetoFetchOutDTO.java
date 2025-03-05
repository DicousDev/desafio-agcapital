package br.com.agsistemas.dto.out;

import br.com.agsistemas.enums.ProjetoStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ProjetoFetchOutDTO {

  private UUID idExterno;
  private String descricao;
  private ProjetoStatus status;
  private LocalDateTime dataHoraCriacao;
  private LocalDateTime dataHoraAtualizacao;
  private LocalDateTime dataFinalizacao;
  private Collection<AtividadeOutDTO> atividades;
}
