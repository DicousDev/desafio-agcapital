package br.com.agsistemas.dto.in;

import br.com.agsistemas.enums.AtividadeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class AlterarAtividadeDTO {

  @NotNull
  private UUID idExterno;
  @NotBlank
  private String titulo;
  private String descricao;
  @NotNull
  private AtividadeStatus status;
}
