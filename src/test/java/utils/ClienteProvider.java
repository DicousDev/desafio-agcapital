package utils;

import br.com.agsistemas.model.Cliente;
import br.com.agsistemas.model.Cliente.ClienteBuilder;
import java.util.UUID;

public class ClienteProvider {

  public static ClienteBuilder padrao() {
    return Cliente.builder()
        .id(1L)
        .idExterno(UUID.fromString("61a0f2b4-1e23-481a-9d2e-fe0d56a0b530"))
        .nome("João");
  }
}
