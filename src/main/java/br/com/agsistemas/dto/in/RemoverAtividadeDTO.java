package br.com.agsistemas.dto.in;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class RemoverAtividadeDTO {

  @NotNull
  private UUID projetoIdExterno;
  @NotNull
  private UUID clienteResponsavel;
  @NotNull
  private UUID atividadeIdExterno;
}
