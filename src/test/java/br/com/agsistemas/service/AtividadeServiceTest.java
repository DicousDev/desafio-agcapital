package br.com.agsistemas.service;

import br.com.agsistemas.converter.AtividadeConverter;
import br.com.agsistemas.dto.in.AlterarAtividadeDTO;
import br.com.agsistemas.enums.AtividadeStatus;
import br.com.agsistemas.exception.NaoEncontradoException;
import br.com.agsistemas.model.Atividade;
import br.com.agsistemas.repository.AtividadeRepository;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import utils.AtividadeProvider;

public class AtividadeServiceTest {

  @Mock
  private AtividadeRepository repository;
  @Mock
  private AtividadeConverter converter;
  @InjectMocks
  private AtividadeService service;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void deveAlterarDadosDaAtividade() {
    AlterarAtividadeDTO data = AlterarAtividadeDTO.builder()
        .idExterno(UUID.randomUUID())
        .titulo("desenvolver testes unitários")
        .descricao("Boa qualidade")
        .status(AtividadeStatus.DESENVOLVIMENTO)
        .build();
    Atividade atividade = AtividadeProvider.padrao().build();
    Mockito.when(repository.findByIdExterno(data.getIdExterno())).thenReturn(Optional.of(atividade));
    Mockito.when(repository.save(Mockito.any(Atividade.class))).thenReturn(atividade);
    service.alterar(data);
    Mockito.verify(repository).save(Mockito.any(Atividade.class));
    Mockito.verify(converter).toAtividadeOutDTO(Mockito.any(Atividade.class));
  }


  @Test
  public void deveFalharQuandoNaoEncontrarAtividade() {
    AlterarAtividadeDTO data = AlterarAtividadeDTO.builder()
        .idExterno(UUID.randomUUID())
        .titulo("desenvolver testes unitários")
        .descricao("Boa qualidade")
        .status(AtividadeStatus.DESENVOLVIMENTO)
        .build();
    Mockito.when(repository.findByIdExterno(data.getIdExterno())).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> service.alterar(data))
        .isInstanceOf(NaoEncontradoException.class)
        .hasMessage("Atividade [%s] não encontrada.".formatted(data.getIdExterno()));
  }
}
