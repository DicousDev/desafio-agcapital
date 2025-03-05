package br.com.agsistemas.model;

import br.com.agsistemas.exception.EntidadeInvalidaException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cliente")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cliente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private UUID idExterno;
  private String nome;

  public static class ClienteBuilder {

    public Cliente build() {

      if(StringUtils.isBlank(nome)) {
        throw new EntidadeInvalidaException("Não é possível cadastrar cliente sem nome.");
      }

      if(nome.length() > 255) {
        throw new EntidadeInvalidaException("Nâo é possível criar projeto. O campo nome excedeu o limite de caracteres.");
      }

      if(Objects.isNull(idExterno)) {
        idExterno = UUID.randomUUID();
      }

      return new Cliente(id, idExterno, nome);
    }
  }
}
