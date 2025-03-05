package br.com.agsistemas.service;

import br.com.agsistemas.converter.AtividadeConverter;
import br.com.agsistemas.converter.ProjetoConverter;
import br.com.agsistemas.dto.in.AdicionarAtividadeDTO;
import br.com.agsistemas.dto.in.AlterarProjetoDTO;
import br.com.agsistemas.dto.in.CadastrarProjetoDTO;
import br.com.agsistemas.dto.in.RemoverAtividadeDTO;
import br.com.agsistemas.dto.in.VincularClienteNoProjetoDTO;
import br.com.agsistemas.dto.out.AtividadeOutDTO;
import br.com.agsistemas.dto.out.ProjetoFetchOutDTO;
import br.com.agsistemas.dto.out.ProjetoOutDTO;
import br.com.agsistemas.enums.AtividadeStatus;
import br.com.agsistemas.enums.ProjetoStatus;
import br.com.agsistemas.exception.EntidadeInvalidaException;
import br.com.agsistemas.exception.NaoEncontradoException;
import br.com.agsistemas.model.Atividade;
import br.com.agsistemas.model.Cliente;
import br.com.agsistemas.model.Projeto;
import br.com.agsistemas.repository.AtividadeRepository;
import br.com.agsistemas.repository.ClienteRepository;
import br.com.agsistemas.repository.ProjetoRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjetoService {

  private final ProjetoRepository repository;
  private final ClienteRepository clienteRepository;
  private final AtividadeRepository atividadeRepository;
  private final AtividadeConverter atividadeConverter;
  private final ProjetoConverter converter;

  public Page<ProjetoOutDTO> buscarTodosPorCliente(UUID idExternoCliente, Integer pagina) {
    clienteRepository.findByIdExterno(idExternoCliente)
        .orElseThrow(() -> new NaoEncontradoException("Cliente [%s] não encontrado.".formatted(idExternoCliente)));

    int pageSize = 10;
    Page<Projeto> projetos = repository.buscarPorCliente(idExternoCliente, PageRequest.of(pagina, pageSize));
    return converter.toProjetoOutPage(projetos);
  }

  public ProjetoFetchOutDTO buscarByIdExterno(UUID idExterno) {
    Projeto projeto = repository.findByIdExternoFetchAtividades(idExterno).orElseThrow(() -> new NaoEncontradoException("Projeto [%s] não encontrado.".formatted(idExterno)));
    return converter.toProjetoFetchOutDTO(projeto)
        .atividades(atividadeConverter.toAtividadeOutDTO(projeto.getAtividades()))
        .build();
  }

  public ProjetoOutDTO cadastrar(CadastrarProjetoDTO projeto) {
    Cliente cliente = clienteRepository.findByIdExterno(projeto.getClienteIdExterno())
        .orElseThrow(() -> new NaoEncontradoException("Cliente [%s] não encontrado.".formatted(projeto.getClienteIdExterno())));

    Projeto toSave = converter.toProjeto(projeto)
        .clientes(List.of(cliente))
        .status(ProjetoStatus.PARADO)
        .build();

    toSave = repository.save(toSave);
    return converter.toProjetoOut(toSave);
  }


  @Transactional
  public AtividadeOutDTO adicionarAtividade(AdicionarAtividadeDTO atividade) {
    Cliente cliente = clienteRepository.buscarPeloProjeto(atividade.getClienteResponsavel(), atividade.getProjetoIdExterno())
        .orElseThrow(() -> new NaoEncontradoException("Cliente [%s] não encontrado no projeto [%s].".formatted(atividade.getClienteResponsavel(), atividade.getProjetoIdExterno())));

    Projeto projeto = repository.buscarProjetoPorIdExternoJoinClientes(atividade.getProjetoIdExterno())
        .orElseThrow(() -> new NaoEncontradoException("Projeto [%s] não encontrado.".formatted(atividade.getProjetoIdExterno())));

    Atividade atividadeModel = atividadeConverter.toAtividade(atividade)
        .status(AtividadeStatus.PARADO)
        .responsavel(cliente)
        .build();
    projeto = projeto.addAtividade(atividadeModel);
    repository.save(projeto);
    return atividadeConverter.toAtividadeOutDTO(atividadeModel);
  }

  @Transactional
  public void removerAtividade(RemoverAtividadeDTO atividade) {
    clienteRepository.buscarPeloProjeto(atividade.getClienteResponsavel(), atividade.getProjetoIdExterno())
        .orElseThrow(() -> new NaoEncontradoException("Cliente [%s] não encontrado no projeto [%s].".formatted(atividade.getClienteResponsavel(), atividade.getProjetoIdExterno())));

    Boolean existsProjeto = repository.existsByIdExterno(atividade.getProjetoIdExterno());
    if(Boolean.FALSE.equals(existsProjeto)) {
      throw new NaoEncontradoException("Projeto [%s] não encontrado.".formatted(atividade.getProjetoIdExterno()));
    }

    atividadeRepository.remove(atividade.getAtividadeIdExterno());
  }

  @Transactional
  public void vincularCliente(VincularClienteNoProjetoDTO cliente) {
    Projeto projeto = repository.findByIdExterno(cliente.getProjetoIdExterno())
        .orElseThrow(() -> new NaoEncontradoException("Projeto [%s] não encontrado.".formatted(cliente.getProjetoIdExterno())));

    Cliente clienteExists = clienteRepository.findByIdExterno(cliente.getClienteIdExterno())
        .orElseThrow(() -> new NaoEncontradoException("Cliente [%s] não encontrado.".formatted(cliente.getClienteIdExterno())));

    Optional<Cliente> clienteOp = clienteRepository.buscarPeloProjeto(cliente.getClienteIdExterno(), cliente.getProjetoIdExterno());
    if(clienteOp.isPresent()) {
      throw new EntidadeInvalidaException("Cliente [%s] já foi inserido no projeto [%s].".formatted(cliente.getClienteIdExterno(), cliente.getProjetoIdExterno()));
    }

    repository.inserirCliente(projeto.getId(), clienteExists.getId());
  }

  @Transactional
  public ProjetoOutDTO alterar(AlterarProjetoDTO projeto) {
    Projeto projetoEncontrado = repository.findByIdExterno(projeto.getIdExternoProjeto())
        .orElseThrow(() -> new NaoEncontradoException("Projeto [%s] não encontrado.".formatted(projeto.getIdExternoProjeto())));
    projetoEncontrado = projetoEncontrado.alterarStatus(projeto.getStatus());
    projetoEncontrado = repository.save(projetoEncontrado);
    return converter.toProjetoOut(projetoEncontrado);
  }
}
