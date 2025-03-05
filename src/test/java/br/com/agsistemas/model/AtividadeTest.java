package br.com.agsistemas.model;

import br.com.agsistemas.dto.in.AlterarAtividadeDTO;
import br.com.agsistemas.enums.AtividadeStatus;
import br.com.agsistemas.exception.EntidadeInvalidaException;
import br.com.agsistemas.model.Atividade.AtividadeBuilder;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.AtividadeProvider;

public class AtividadeTest {


  @Test
  public void deveAlterarDadosDaAtividade() {
    Atividade atividade = AtividadeProvider.padrao().build();
    Atividade atividadeAtualizado = atividade.alterar(AlterarAtividadeDTO.builder()
        .titulo("Criar testes unitários")
        .descricao("Melhor qualidade")
        .status(AtividadeStatus.FINALIZADO).build());

    Assertions.assertThat(atividadeAtualizado.getTitulo()).isEqualTo("Criar testes unitários");
    Assertions.assertThat(atividadeAtualizado.getDescricao()).isEqualTo("Melhor qualidade");
    Assertions.assertThat(atividadeAtualizado.getStatus()).isEqualTo(AtividadeStatus.FINALIZADO);
  }

  @ParameterizedTest
  @MethodSource("atividadeBuildException")
  public static void atividadeExceptions(String erro, AtividadeBuilder builder) {
    Assertions.assertThatThrownBy(builder::build)
        .hasMessage(erro)
        .isInstanceOf(EntidadeInvalidaException.class);
  }

  public Stream<Arguments> atividadeBuildException() {
    return Stream.of(
      Arguments.of("Deve ter campo título preenchido.", AtividadeProvider.padrao().titulo(null)),
      Arguments.of("Deve ter campo título preenchido.", AtividadeProvider.padrao().titulo("")),
      Arguments.of("Deve ter campo título preenchido.", AtividadeProvider.padrao().titulo(" ")),
      Arguments.of("Nâo é possível criar atividade. O campo titulo excedeu o limite de caracteres.", AtividadeProvider.padrao().titulo("titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de c")),
      Arguments.of("Nâo é possível criar atividade. O campo descrição excedeu o limite de caracteres.", AtividadeProvider.padrao().descricao("titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de c")),
      Arguments.of("Deve alocar um responsável a essa atividade.", AtividadeProvider.padrao().titulo("Deve alocar um responsável a essa atividade.")),
      Arguments.of("É preciso preencher status atual da atividade.", AtividadeProvider.padrao().status(null))
    );
  }
}
