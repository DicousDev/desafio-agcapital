package br.com.agsistemas.dto.in;

import br.com.agsistemas.enums.ProjetoStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class AlterarProjetoDTO {

  @NotNull
  private UUID idExternoProjeto;
  @NotNull
  private ProjetoStatus status;
}
