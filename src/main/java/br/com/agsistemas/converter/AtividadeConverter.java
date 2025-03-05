package br.com.agsistemas.converter;

import br.com.agsistemas.dto.in.AdicionarAtividadeDTO;
import br.com.agsistemas.dto.out.AtividadeOutDTO;
import br.com.agsistemas.enums.AtividadeStatus;
import br.com.agsistemas.model.Atividade;
import br.com.agsistemas.model.Atividade.AtividadeBuilder;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class AtividadeConverter {

  private final ClienteConverter clienteConverter;

  public AtividadeBuilder toAtividade(AdicionarAtividadeDTO atividade) {

    return Atividade.builder()
        .titulo(atividade.getTitulo())
        .descricao(atividade.getDescricao());
  }

  public AtividadeOutDTO toAtividadeOutDTO(Atividade atividade) {
    return AtividadeOutDTO.builder()
        .idExterno(atividade.getIdExterno())
        .titulo(atividade.getTitulo())
        .descricao(atividade.getDescricao())
        .dataHoraCriacao(atividade.getDataHoraCriacao())
        .responsavel(clienteConverter.toClienteOut(atividade.getResponsavel()))
        .build();
  }

  public Collection<AtividadeOutDTO> toAtividadeOutDTO(Collection<Atividade> atividades) {

    if(CollectionUtils.isEmpty(atividades)) {
      return Collections.emptyList();
    }

    return atividades.stream().map(this::toAtividadeOutDTO).toList();
  }
}
