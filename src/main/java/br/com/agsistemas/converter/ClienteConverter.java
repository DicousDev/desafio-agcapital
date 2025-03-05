package br.com.agsistemas.converter;

import br.com.agsistemas.dto.in.CadastrarClienteDTO;
import br.com.agsistemas.dto.out.ClienteOutDTO;
import br.com.agsistemas.model.Cliente;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ClienteConverter {

  public Cliente toCliente(CadastrarClienteDTO cliente) {
    return Cliente.builder()
        .nome(cliente.getNome())
        .build();
  }

  public Collection<ClienteOutDTO> toClienteOut(Collection<Cliente> clientes) {

    if(CollectionUtils.isEmpty(clientes)) {
      return new ArrayList<>();
    }

    return clientes.stream().map(this::toClienteOut).toList();
  }

  public ClienteOutDTO toClienteOut(Cliente cliente) {
    return ClienteOutDTO.builder()
        .idExterno(cliente.getIdExterno())
        .nome(cliente.getNome())
        .build();
  }
}
