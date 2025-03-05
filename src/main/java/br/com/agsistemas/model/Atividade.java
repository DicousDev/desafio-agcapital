package br.com.agsistemas.model;

import br.com.agsistemas.dto.in.AlterarAtividadeDTO;
import br.com.agsistemas.enums.AtividadeStatus;
import br.com.agsistemas.exception.EntidadeInvalidaException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "atividade")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Atividade {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private UUID idExterno;
  private String titulo;
  private String descricao;
  private LocalDateTime dataHoraCriacao;
  @Enumerated(EnumType.STRING)
  private AtividadeStatus status;
  @OneToOne
  @JoinColumn(name = "atividade_id")
  private Cliente responsavel;

  public Atividade alterar(AlterarAtividadeDTO atividade) {
    Atividade atividadeUpdate = alterarStatus(atividade.getStatus());
    return atividadeUpdate.toBuilder()
        .titulo(atividade.getTitulo())
        .descricao(atividade.getDescricao())
        .build();
  }

  private Atividade alterarStatus(AtividadeStatus status) {

    if(this.status.equals(status)) {
      return this;
    }

    return toBuilder()
        .status(status)
        .build();
  }

  public static class AtividadeBuilder {

    public Atividade build() {

      if(StringUtils.isBlank(titulo)) {
        throw new EntidadeInvalidaException("Deve ter campo título preenchido.");
      }

      if(titulo.length() > 255) {
        throw new EntidadeInvalidaException("Nâo é possível criar atividade. O campo titulo excedeu o limite de caracteres.");
      }

      if(Objects.nonNull(descricao) && descricao.length() > 255) {
        throw new EntidadeInvalidaException("Nâo é possível criar atividade. O campo descrição excedeu o limite de caracteres.");
      }

      if(Objects.isNull(dataHoraCriacao)) {
        dataHoraCriacao = LocalDateTime.now();
      }

      if(Objects.isNull(responsavel)) {
        throw new EntidadeInvalidaException("Deve alocar um responsável a essa atividade.");
      }

      if(Objects.isNull(idExterno)) {
        idExterno = UUID.randomUUID();
      }

      if(Objects.isNull(status)) {
        throw new EntidadeInvalidaException("É preciso preencher status atual da atividade.");
      }

      return new Atividade(id, idExterno, titulo, descricao, dataHoraCriacao, status, responsavel);
    }
  }
}
