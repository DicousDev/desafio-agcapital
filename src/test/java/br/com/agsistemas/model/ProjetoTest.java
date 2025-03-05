package br.com.agsistemas.model;

import br.com.agsistemas.enums.ProjetoStatus;
import br.com.agsistemas.exception.EntidadeInvalidaException;
import br.com.agsistemas.model.Projeto.ProjetoBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.AtividadeProvider;
import utils.ProjetoProvider;

public class ProjetoTest {

  @Test
  public void naoDeveAlterarQuandoForStatusFinalizado() {
    Projeto projeto = ProjetoProvider.padrao()
        .status(ProjetoStatus.FINALIZADO)
        .dataHoraAtualizacao(null)
        .build();

    projeto = projeto.alterarStatus(ProjetoStatus.ANDAMENTO);
    Assertions.assertThat(projeto.getStatus()).isEqualTo(ProjetoStatus.FINALIZADO);
    Assertions.assertThat(projeto.getDataHoraAtualizacao()).isNull();
  }

  @Test
  public void naoDeveAlterarQuandoForMesmoStatus() {
    Projeto projeto = ProjetoProvider.padrao()
        .status(ProjetoStatus.PARADO)
        .dataHoraAtualizacao(null)
        .build();

    projeto = projeto.alterarStatus(ProjetoStatus.PARADO);
    Assertions.assertThat(projeto.getStatus()).isEqualTo(ProjetoStatus.PARADO);
    Assertions.assertThat(projeto.getDataHoraAtualizacao()).isNull();
  }

  @Test
  public void deveAlterarStatus() {
    Projeto projeto = ProjetoProvider.padrao()
        .status(ProjetoStatus.PARADO)
        .dataHoraAtualizacao(null)
        .build();

    projeto = projeto.alterarStatus(ProjetoStatus.ANDAMENTO);
    Assertions.assertThat(projeto.getStatus()).isEqualTo(ProjetoStatus.ANDAMENTO);
    Assertions.assertThat(projeto.getDataHoraAtualizacao()).isNotNull();
  }

  @Test
  public void deveAdicionarAtividades() {
    Projeto projeto = ProjetoProvider.padrao()
        .atividades(new ArrayList<>())
        .dataHoraAtualizacao(null)
        .build();
    Atividade atividade = AtividadeProvider.padrao().build();
    projeto = projeto.addAtividade(atividade);

    Assertions.assertThat(projeto.getAtividades().size()).isEqualTo(1);
    Assertions.assertThat(projeto.getAtividades().get(0)).isEqualTo(atividade);
    Assertions.assertThat(projeto.getDataHoraAtualizacao()).isNotNull();
  }

  @Test
  public void deveDefinirDataFinalizacaoProjetoQuandoStatusForFinalizado() {
    Projeto projeto = ProjetoProvider.padrao()
        .status(ProjetoStatus.FINALIZADO)
        .dataHoraFinalizacao(null)
        .build();

    Assertions.assertThat(projeto.getDataHoraFinalizacao()).isNotNull();
  }

  @ParameterizedTest
  @MethodSource("projetoBuildException")
  public void projetoExceptions(String erro, ProjetoBuilder builder) {
    Assertions.assertThatThrownBy(builder::build)
        .hasMessage(erro)
        .isInstanceOf(EntidadeInvalidaException.class);
  }

  public static Stream<Arguments> projetoBuildException() {
    return Stream.of(
        Arguments.of("Não é possível criar projeto sem descrição.", ProjetoProvider.padrao().descricao(null)),
        Arguments.of("Não é possível criar projeto sem descrição.", ProjetoProvider.padrao().descricao("")),
        Arguments.of("Não é possível criar projeto sem descrição.", ProjetoProvider.padrao().descricao(" ")),
        Arguments.of("Não é possível criar projeto. O campo descrição excedeu o limite de caracteres.", ProjetoProvider.padrao().descricao("titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de c")),
        Arguments.of("É preciso preencher status atual do projeto.", ProjetoProvider.padrao().status(null)),
        Arguments.of("Não é possível criar projeto sem nenhum cliente.", ProjetoProvider.padrao().clientes(Collections.emptyList()))
    );
  }
}
