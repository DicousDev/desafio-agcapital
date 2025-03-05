package br.com.agsistemas.service;

import br.com.agsistemas.converter.ClienteConverter;
import br.com.agsistemas.dto.in.CadastrarClienteDTO;
import br.com.agsistemas.dto.out.ClienteOutDTO;
import br.com.agsistemas.model.Cliente;
import br.com.agsistemas.repository.ClienteRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

  private final ClienteRepository repository;
  private final ClienteConverter converter;

  public Collection<ClienteOutDTO> buscarAll() {
    List<Cliente> clientes = repository.findAll();
    return converter.toClienteOut(clientes);
  }

  public ClienteOutDTO cadastrar(CadastrarClienteDTO cliente) {
    Cliente toSave = converter.toCliente(cliente);
    toSave = repository.save(toSave);
    return converter.toClienteOut(toSave);
  }
}
