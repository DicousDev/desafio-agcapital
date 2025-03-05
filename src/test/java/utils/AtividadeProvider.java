package utils;

import br.com.agsistemas.enums.AtividadeStatus;
import br.com.agsistemas.model.Atividade;
import br.com.agsistemas.model.Atividade.AtividadeBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

public class AtividadeProvider {

  public static AtividadeBuilder padrao() {
    return Atividade.builder()
        .id(1L)
        .idExterno(UUID.fromString("61a0f2b4-1e23-481a-9d2e-fe0d56a0b530"))
        .titulo("Desafio técnico")
        .descricao("Finalizar desafio técnico")
        .dataHoraCriacao(LocalDateTime.of(2025, 3, 5, 6, 0, 0))
        .status(AtividadeStatus.BACKLOG)
        .responsavel(ClienteProvider.padrao().build());
  }
}
