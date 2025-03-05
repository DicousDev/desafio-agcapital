package br.com.agsistemas.model;

import br.com.agsistemas.enums.ProjetoStatus;
import br.com.agsistemas.exception.EntidadeInvalidaException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "projeto")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Projeto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private UUID idExterno;
  private String descricao;
  private LocalDateTime dataHoraCriacao;
  private LocalDateTime dataHoraAtualizacao;
  private LocalDateTime dataHoraFinalizacao;
  @Enumerated(EnumType.STRING)
  private ProjetoStatus status;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "projeto_id")
  private List<Atividade> atividades;
  @Getter(AccessLevel.PRIVATE)
  @ManyToMany
  @JoinTable(
      name = "PROJETOS_PARTICIPANTES",
      joinColumns = @JoinColumn(name = "PROJETO_ID"),
      inverseJoinColumns = @JoinColumn(name = "CLIENTE_ID"))
  private List<Cliente> clientes;

  public Projeto alterarStatus(ProjetoStatus status) {

    if(this.status.equals(status) || ProjetoStatus.FINALIZADO.equals(this.status)) {
      return this;
    }

    return toBuilder()
        .status(status)
        .dataHoraAtualizacao(LocalDateTime.now())
        .build();
  }

  public Projeto addAtividade(Atividade atividade) {

    if(Objects.isNull(atividade)) {
      return this;
    }

    atividades.add(atividade);
    return toBuilder()
        .atividades(atividades)
        .dataHoraAtualizacao(LocalDateTime.now())
        .build();
  }

  public static class ProjetoBuilder {

    public Projeto build() {


      if(StringUtils.isBlank(descricao)) {
        throw new EntidadeInvalidaException("Não é possível criar projeto sem descrição.");
      }

      if(descricao.length() > 255) {
        throw new EntidadeInvalidaException("Não é possível criar projeto. O campo descrição excedeu o limite de caracteres.");
      }

      if(Objects.isNull(status)) {
        throw new EntidadeInvalidaException("É preciso preencher status atual do projeto.");
      }

      if(Objects.isNull(atividades)) {
        atividades = new ArrayList<>();
      }

      if(CollectionUtils.isEmpty(clientes)) {
        throw new EntidadeInvalidaException("Não é possível criar projeto sem nenhum cliente.");
      }

      if(Objects.isNull(dataHoraCriacao)) {
        dataHoraCriacao = LocalDateTime.now();
      }

      if(ProjetoStatus.FINALIZADO.equals(status) && Objects.isNull(dataHoraFinalizacao)) {
        dataHoraFinalizacao = LocalDateTime.now();
      }

      if(Objects.isNull(idExterno)) {
        idExterno = UUID.randomUUID();
      }

      return new Projeto(id, idExterno, descricao, dataHoraCriacao, dataHoraAtualizacao, dataHoraFinalizacao, status, atividades, clientes);
    }
  }
}
