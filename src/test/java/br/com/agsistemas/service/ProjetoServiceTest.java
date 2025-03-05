package br.com.agsistemas.service;

import br.com.agsistemas.converter.AtividadeConverter;
import br.com.agsistemas.converter.ProjetoConverter;
import br.com.agsistemas.dto.in.AdicionarAtividadeDTO;
import br.com.agsistemas.dto.in.AlterarProjetoDTO;
import br.com.agsistemas.dto.in.CadastrarProjetoDTO;
import br.com.agsistemas.dto.in.RemoverAtividadeDTO;
import br.com.agsistemas.dto.in.VincularClienteNoProjetoDTO;
import br.com.agsistemas.dto.out.ProjetoFetchOutDTO;
import br.com.agsistemas.enums.ProjetoStatus;
import br.com.agsistemas.exception.EntidadeInvalidaException;
import br.com.agsistemas.exception.NaoEncontradoException;
import br.com.agsistemas.model.Atividade;
import br.com.agsistemas.model.Cliente;
import br.com.agsistemas.model.Projeto;
import br.com.agsistemas.model.Projeto.ProjetoBuilder;
import br.com.agsistemas.repository.AtividadeRepository;
import br.com.agsistemas.repository.ClienteRepository;
import br.com.agsistemas.repository.ProjetoRepository;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import utils.AtividadeProvider;
import utils.ClienteProvider;
import utils.ProjetoProvider;

public class ProjetoServiceTest {

  @Mock
  private ProjetoRepository repository;
  @Mock
  private ClienteRepository clienteRepository;
  @Mock
  private AtividadeRepository atividadeRepository;
  @Mock
  private AtividadeConverter atividadeConverter;
  @Mock
  private ProjetoConverter converter;
  @InjectMocks
  private ProjetoService service;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  public void deveFalharQuandoBuscarTodosProjetosPorClienteInexistente() {
    UUID idCliente = UUID.randomUUID();
    Mockito.when(clienteRepository.findByIdExterno(idCliente)).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> service.buscarTodosPorCliente(idCliente, 0))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Cliente [%s] não encontrado.".formatted(idCliente));
  }

  @Test
  public void deveBuscarTodosProjetosPorCliente() {
    UUID idCliente = UUID.randomUUID();
    Optional<Cliente> cliente = Optional.of(Mockito.mock(Cliente.class));
    Mockito.when(clienteRepository.findByIdExterno(idCliente)).thenReturn(cliente);
    service.buscarTodosPorCliente(idCliente, 0);
    Mockito.verify(repository).buscarPorCliente(Mockito.eq(idCliente), Mockito.any(Pageable.class));
  }

  @Test
  public void deveBuscarProjetoPeloIdExterno() {
    UUID idProjeto = UUID.randomUUID();
    Optional<Projeto> projeto = Optional.empty();
    Mockito.when(repository.findByIdExternoFetchAtividades(idProjeto)).thenReturn(projeto);
    Assertions.assertThatThrownBy(() -> service.buscarByIdExterno(idProjeto))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Projeto [%s] não encontrado.".formatted(idProjeto));
  }

  @Test
  public void deveBuscarByIdExterno() {
    UUID idProjeto = UUID.randomUUID();
    Optional<Projeto> projeto = Optional.of(Mockito.mock(Projeto.class));
    Mockito.when(repository.findByIdExternoFetchAtividades(idProjeto)).thenReturn(projeto);
    Mockito.when(converter.toProjetoFetchOutDTO(Mockito.any(Projeto.class))).thenReturn(ProjetoFetchOutDTO.builder());
    service.buscarByIdExterno(idProjeto);
    Mockito.verify(converter).toProjetoFetchOutDTO(Mockito.any());
    Mockito.verify(atividadeConverter).toAtividadeOutDTO(Mockito.anyCollection());
  }

  @Test
  public void deveFalharAoNaoEncontrarClienteParaCadastrarProjeto() {
    UUID idCliente = UUID.randomUUID();
    CadastrarProjetoDTO projeto = CadastrarProjetoDTO.builder()
        .clienteIdExterno(idCliente)
        .descricao("Desafio API")
        .build();
    Optional<Cliente> cliente = Optional.empty();
    Mockito.when(clienteRepository.findByIdExterno(idCliente)).thenReturn(cliente);
    Assertions.assertThatThrownBy(() -> service.cadastrar(projeto))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Cliente [%s] não encontrado.".formatted(idCliente));
  }

  @Test
  public void deveCadastrarProjeto() {
    UUID idCliente = UUID.randomUUID();
    CadastrarProjetoDTO projeto = CadastrarProjetoDTO.builder()
        .clienteIdExterno(idCliente)
        .descricao("Desafio API")
        .build();

    ProjetoBuilder projetoBuilder = ProjetoProvider.padrao();
    Projeto projetoBuild = projetoBuilder.build();

    Optional<Cliente> cliente = Optional.of(ClienteProvider.padrao().build());
    Mockito.when(clienteRepository.findByIdExterno(idCliente)).thenReturn(cliente);
    Mockito.when(converter.toProjeto(projeto)).thenReturn(projetoBuilder);
    Mockito.when(repository.save(projetoBuilder.build())).thenReturn(projetoBuild);
    service.cadastrar(projeto);
    Mockito.verify(clienteRepository).findByIdExterno(idCliente);
    Mockito.verify(converter).toProjeto(Mockito.any(CadastrarProjetoDTO.class));
    Mockito.verify(repository).save(Mockito.any(Projeto.class));
  }

  @Test
  public void deveFalharQuandoTentarEncontrarClienteParaAdicionarAtividade() {
    AdicionarAtividadeDTO atividade = AdicionarAtividadeDTO.builder()
        .projetoIdExterno(UUID.randomUUID())
        .titulo("titulo")
        .descricao("descrição")
        .clienteResponsavel(UUID.randomUUID())
        .build();

    Optional<Cliente> cliente = Optional.empty();
    Mockito.when(clienteRepository.buscarPeloProjeto(Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(cliente);
    Assertions.assertThatThrownBy(() -> service.adicionarAtividade(atividade))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Cliente [%s] não encontrado no projeto [%s].".formatted(atividade.getClienteResponsavel(), atividade.getProjetoIdExterno()));
  }

  @Test
  public void deveFalharQuandoTentarEncontrarProjetoParaAdicionarAtividade() {
    AdicionarAtividadeDTO atividade = AdicionarAtividadeDTO.builder()
        .projetoIdExterno(UUID.randomUUID())
        .titulo("titulo")
        .descricao("descrição")
        .clienteResponsavel(UUID.randomUUID())
        .build();

    Optional<Cliente> cliente = Optional.of(Mockito.mock(Cliente.class));
    Optional<Projeto> projeto = Optional.empty();
    Mockito.when(clienteRepository.buscarPeloProjeto(Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(cliente);
    Mockito.when(repository.buscarProjetoPorIdExternoJoinClientes(Mockito.any(UUID.class))).thenReturn(projeto);
    Assertions.assertThatThrownBy(() -> service.adicionarAtividade(atividade))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Projeto [%s] não encontrado.".formatted(atividade.getProjetoIdExterno()));
  }

  @Test
  public void deveAdicionarAtividadeAoProjeto() {
    AdicionarAtividadeDTO atividade = AdicionarAtividadeDTO.builder()
        .projetoIdExterno(UUID.randomUUID())
        .titulo("titulo")
        .descricao("descrição")
        .clienteResponsavel(UUID.randomUUID())
        .build();

    Projeto projeto = ProjetoProvider.padrao().idExterno(atividade.getProjetoIdExterno()).build();
    Optional<Cliente> cliente = Optional.of(Mockito.mock(Cliente.class));
    Optional<Projeto> projetoOp = Optional.of(projeto);
    Mockito.when(clienteRepository.buscarPeloProjeto(Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(cliente);
    Mockito.when(repository.buscarProjetoPorIdExternoJoinClientes(projeto.getIdExterno())).thenReturn(projetoOp);
    Mockito.when(atividadeConverter.toAtividade(atividade)).thenReturn(AtividadeProvider.padrao());
    service.adicionarAtividade(atividade);
    Mockito.verify(repository).save(Mockito.any(Projeto.class));
    Mockito.verify(atividadeConverter).toAtividadeOutDTO(Mockito.any(Atividade.class));
  }

  @Test
  public void deveRemoverAtividadeDoProjeto() {
    Optional<Cliente> cliente = Optional.of(ClienteProvider.padrao().build());
    Mockito.when(clienteRepository.buscarPeloProjeto(Mockito.any(), Mockito.any())).thenReturn(cliente);
    Mockito.when(repository.existsByIdExterno(Mockito.any())).thenReturn(Boolean.TRUE);
    service.removerAtividade(Mockito.mock(RemoverAtividadeDTO.class));
    Mockito.verify(atividadeRepository).remove(Mockito.any());
  }

  @Test
  public void deveFalharNaTentativaDeRemocaoDeAtividadeDoProjetoPorNaoEncontrarCliente() {
    RemoverAtividadeDTO data = RemoverAtividadeDTO.builder()
        .atividadeIdExterno(UUID.randomUUID())
        .clienteResponsavel(UUID.randomUUID())
        .projetoIdExterno(UUID.randomUUID())
        .build();
    Optional<Cliente> cliente = Optional.empty();
    Mockito.when(clienteRepository.buscarPeloProjeto(Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(cliente);
    Assertions.assertThatThrownBy(() -> service.removerAtividade(data))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Cliente [%s] não encontrado no projeto [%s].".formatted(data.getClienteResponsavel(), data.getProjetoIdExterno()));
  }

  @Test
  public void deveFalharQuandoNaoExistirProjetoNaBaseNaRemocaoDeAtividadeDoProjeto() {
    RemoverAtividadeDTO data = RemoverAtividadeDTO.builder()
        .atividadeIdExterno(UUID.randomUUID())
        .clienteResponsavel(UUID.randomUUID())
        .projetoIdExterno(UUID.randomUUID())
        .build();

    Optional<Cliente> clienteOp = Optional.of(ClienteProvider.padrao().build());
    Mockito.when(clienteRepository.buscarPeloProjeto(Mockito.any(UUID.class), Mockito.any(UUID.class))).thenReturn(clienteOp);
    Mockito.when(repository.existsByIdExterno(Mockito.any(UUID.class))).thenReturn(Boolean.FALSE);
    Assertions.assertThatThrownBy(() -> service.removerAtividade(data))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Projeto [%s] não encontrado.".formatted(data.getProjetoIdExterno()));
  }


  @Test
  public void deveVincularClienteAoProjeto() {
    VincularClienteNoProjetoDTO clientePayload = VincularClienteNoProjetoDTO.builder()
        .clienteIdExterno(UUID.randomUUID())
        .projetoIdExterno(UUID.randomUUID())
        .build();

    Optional<Projeto> projeto = Optional.of(ProjetoProvider.padrao().build());
    Optional<Cliente> cliente = Optional.of(ClienteProvider.padrao().build());
    Optional<Cliente> clienteInProjeto = Optional.empty();
    Mockito.when(repository.findByIdExterno(clientePayload.getProjetoIdExterno())).thenReturn(projeto);
    Mockito.when(clienteRepository.findByIdExterno(clientePayload.getClienteIdExterno())).thenReturn(cliente);
    Mockito.when(clienteRepository.buscarPeloProjeto(clientePayload.getClienteIdExterno(), clientePayload.getProjetoIdExterno())).thenReturn(clienteInProjeto);
    service.vincularCliente(clientePayload);
    Mockito.verify(repository).inserirCliente(Mockito.anyLong(), Mockito.anyLong());
  }


  @Test
  public void deveFalharQuandoNaoEncontrarProjetoParaVincularCliente() {
    VincularClienteNoProjetoDTO clientePayload = VincularClienteNoProjetoDTO.builder()
        .clienteIdExterno(UUID.randomUUID())
        .projetoIdExterno(UUID.randomUUID())
        .build();

    Optional<Projeto> projeto = Optional.empty();
    Mockito.when(repository.findByIdExterno(clientePayload.getProjetoIdExterno())).thenReturn(projeto);
    Assertions.assertThatThrownBy(() -> service.vincularCliente(clientePayload))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Projeto [%s] não encontrado.".formatted(clientePayload.getProjetoIdExterno()));
  }


  @Test
  public void deveFalharQuandoNaoEncontrarClienteParaVincularCliente() {
    VincularClienteNoProjetoDTO clientePayload = VincularClienteNoProjetoDTO.builder()
        .clienteIdExterno(UUID.randomUUID())
        .projetoIdExterno(UUID.randomUUID())
        .build();

    Optional<Projeto> projeto = Optional.of(ProjetoProvider.padrao().build());
    Optional<Cliente> cliente = Optional.empty();
    Mockito.when(repository.findByIdExterno(clientePayload.getProjetoIdExterno())).thenReturn(projeto);
    Mockito.when(clienteRepository.findByIdExterno(clientePayload.getClienteIdExterno())).thenReturn(cliente);

    Assertions.assertThatThrownBy(() -> service.vincularCliente(clientePayload))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Cliente [%s] não encontrado.".formatted(clientePayload.getClienteIdExterno()));
  }

  @Test
  public void deveFalharQuandoClienteJaEstiverVinculadoAoProjeto() {
    VincularClienteNoProjetoDTO clientePayload = VincularClienteNoProjetoDTO.builder()
        .clienteIdExterno(UUID.randomUUID())
        .projetoIdExterno(UUID.randomUUID())
        .build();

    Optional<Projeto> projeto = Optional.of(ProjetoProvider.padrao().build());
    Optional<Cliente> cliente = Optional.of(ClienteProvider.padrao().build());
    Optional<Cliente> clienteInProjeto = cliente;
    Mockito.when(repository.findByIdExterno(clientePayload.getProjetoIdExterno())).thenReturn(projeto);
    Mockito.when(clienteRepository.findByIdExterno(clientePayload.getClienteIdExterno())).thenReturn(cliente);
    Mockito.when(clienteRepository.buscarPeloProjeto(clientePayload.getClienteIdExterno(), clientePayload.getProjetoIdExterno())).thenReturn(clienteInProjeto);

    Assertions.assertThatThrownBy(() -> service.vincularCliente(clientePayload))
        .isInstanceOf(EntidadeInvalidaException.class)
        .hasMessage("Cliente [%s] já foi inserido no projeto [%s].".formatted(clientePayload.getClienteIdExterno(), clientePayload.getProjetoIdExterno()));
  }

  @Test
  public void deveAlterarDadosDoProjeto() {
    AlterarProjetoDTO data = AlterarProjetoDTO.builder()
        .idExternoProjeto(UUID.randomUUID())
        .status(ProjetoStatus.ANDAMENTO)
        .build();

    Projeto projeto = ProjetoProvider.padrao()
        .status(ProjetoStatus.PARADO)
        .build();

    Mockito.when(repository.findByIdExterno(data.getIdExternoProjeto())).thenReturn(Optional.of(projeto));
    Mockito.when(repository.save(Mockito.any(Projeto.class))).thenReturn(projeto);
    service.alterar(data);
    Mockito.verify(repository).save(Mockito.any(Projeto.class));
    Mockito.verify(converter).toProjetoOut(Mockito.any(Projeto.class));

  }

  @Test
  public void deveFalharQuandoNaoEncontrarProjeto() {
    AlterarProjetoDTO data = AlterarProjetoDTO.builder()
        .idExternoProjeto(UUID.randomUUID())
        .status(ProjetoStatus.ANDAMENTO)
        .build();

    Mockito.when(repository.findByIdExterno(data.getIdExternoProjeto())).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> service.alterar(data))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Projeto [%s] não encontrado.".formatted(data.getIdExternoProjeto()));
  }
}
