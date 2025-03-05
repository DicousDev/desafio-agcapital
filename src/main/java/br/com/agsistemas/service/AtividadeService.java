package br.com.agsistemas.service;

import br.com.agsistemas.converter.AtividadeConverter;
import br.com.agsistemas.dto.in.AlterarAtividadeDTO;
import br.com.agsistemas.dto.out.AtividadeOutDTO;
import br.com.agsistemas.exception.NaoEncontradoException;
import br.com.agsistemas.model.Atividade;
import br.com.agsistemas.repository.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AtividadeService {

  private final AtividadeRepository repository;
  private final AtividadeConverter converter;

  @Transactional
  public AtividadeOutDTO alterar(AlterarAtividadeDTO atividade) {
    Atividade atividadeBase = repository.findByIdExterno(atividade.getIdExterno())
        .orElseThrow(() -> new NaoEncontradoException("Atividade [%s] não encontrada.".formatted(atividade.getIdExterno())));
    atividadeBase = atividadeBase.alterar(atividade);
    atividadeBase = repository.save(atividadeBase);
    return converter.toAtividadeOutDTO(atividadeBase);
  }
}
