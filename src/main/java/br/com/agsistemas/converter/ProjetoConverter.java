package br.com.agsistemas.converter;

import br.com.agsistemas.dto.in.CadastrarProjetoDTO;
import br.com.agsistemas.dto.out.ProjetoFetchOutDTO;
import br.com.agsistemas.dto.out.ProjetoOutDTO;
import br.com.agsistemas.model.Projeto;
import br.com.agsistemas.model.Projeto.ProjetoBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class ProjetoConverter {

  public ProjetoBuilder toProjeto(CadastrarProjetoDTO projeto) {
    return Projeto.builder()
        .descricao(projeto.getDescricao());
  }

  public Page<ProjetoOutDTO> toProjetoOutPage(Page<Projeto> projeto) {

    if(Objects.isNull(projeto)) {
      return new PageImpl<>(Collections.emptyList(), projeto.getPageable(), projeto.getTotalElements());
    }

    List<ProjetoOutDTO> projetos = projeto.getContent().stream().map(this::toProjetoOut).toList();
    return new PageImpl<>(projetos, projeto.getPageable(), projeto.getTotalElements());
  }

  public ProjetoOutDTO toProjetoOut(Projeto projeto) {
    return ProjetoOutDTO.builder()
        .idExterno(projeto.getIdExterno())
        .descricao(projeto.getDescricao())
        .status(projeto.getStatus())
        .dataHoraCriacao(projeto.getDataHoraCriacao())
        .dataHoraAtualizacao(projeto.getDataHoraAtualizacao())
        .dataFinalizacao(projeto.getDataHoraFinalizacao())
        .build();
  }

  public ProjetoFetchOutDTO.ProjetoFetchOutDTOBuilder toProjetoFetchOutDTO(Projeto projeto) {
    return ProjetoFetchOutDTO.builder()
        .idExterno(projeto.getIdExterno())
        .descricao(projeto.getDescricao())
        .status(projeto.getStatus())
        .dataHoraCriacao(projeto.getDataHoraCriacao())
        .dataHoraAtualizacao(projeto.getDataHoraAtualizacao())
        .dataFinalizacao(projeto.getDataHoraFinalizacao());
  }
}
