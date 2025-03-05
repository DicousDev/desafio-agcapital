package br.com.agsistemas.repository;

import br.com.agsistemas.model.Atividade;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

  @Transactional(readOnly = true)
  Optional<Atividade> findByIdExterno(UUID idExterno);

  @Modifying
  @Query("DELETE FROM Atividade a WHERE a.idExterno=:id")
  @Transactional
  void remove(@Param("id") UUID idExterno);
}
