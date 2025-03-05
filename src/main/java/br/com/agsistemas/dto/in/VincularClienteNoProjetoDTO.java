package br.com.agsistemas.dto.in;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class VincularClienteNoProjetoDTO {

  @NotNull
  private UUID clienteIdExterno;
  @NotNull
  private UUID projetoIdExterno;
}
