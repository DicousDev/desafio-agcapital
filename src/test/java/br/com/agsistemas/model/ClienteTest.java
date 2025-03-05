package br.com.agsistemas.model;

import br.com.agsistemas.exception.EntidadeInvalidaException;
import br.com.agsistemas.model.Cliente.ClienteBuilder;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.ClienteProvider;

public class ClienteTest {

  @ParameterizedTest
  @MethodSource("clienteBuildException")
  public static void clienteExceptions(String erro, ClienteBuilder builder) {
    Assertions.assertThatThrownBy(builder::build)
        .hasMessage(erro)
        .isInstanceOf(EntidadeInvalidaException.class);
  }

  public Stream<Arguments> clienteBuildException() {
    return Stream.of(
        Arguments.of("Não é possível cadastrar cliente sem nome.", ClienteProvider.padrao().nome(null)),
        Arguments.of("Não é possível cadastrar cliente sem nome.", ClienteProvider.padrao().nome("")),
        Arguments.of("Não é possível cadastrar cliente sem nome.", ClienteProvider.padrao().nome(" ")),
        Arguments.of("Nâo é possível criar projeto. O campo nome excedeu o limite de caracteres.", ClienteProvider.padrao().nome("titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de caracteres.titulo excedeu o limite de c"))
    );
  }
}
