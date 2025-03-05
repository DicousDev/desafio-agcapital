package br.com.agsistemas.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class CadastrarProjetoDTO {

  @NotNull
  private UUID clienteIdExterno;
  @NotBlank
  @Size(max = 255)
  private String descricao;
}
