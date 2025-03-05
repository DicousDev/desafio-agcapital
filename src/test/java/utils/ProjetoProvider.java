package utils;

import br.com.agsistemas.enums.ProjetoStatus;
import br.com.agsistemas.model.Projeto;
import br.com.agsistemas.model.Projeto.ProjetoBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProjetoProvider {

  public static ProjetoBuilder padrao() {
    return Projeto.builder()
        .id(1L)
        .idExterno(UUID.fromString("61a0f2b4-1e23-481a-9d2e-fe0d56a0b530"))
        .descricao("Projeto desafio técnico API")
        .dataHoraCriacao(LocalDateTime.of(2025, 3, 5, 6, 0, 0))
        .status(ProjetoStatus.PARADO)
        .clientes(List.of(ClienteProvider.padrao().build()));
  }
}
