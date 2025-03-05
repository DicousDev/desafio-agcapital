package br.com.agsistemas.repository;

import br.com.agsistemas.model.Projeto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

  @Query("SELECT p FROM Projeto p JOIN FETCH p.clientes c WHERE c.idExterno IN (:id)")
  @Transactional(readOnly = true)
  Page<Projeto> buscarPorCliente(@Param("id") UUID idExternoCliente, Pageable pageable);

  @Query("SELECT p FROM Projeto p LEFT JOIN p.atividades WHERE p.idExterno=:id")
  @Transactional(readOnly = true)
  Optional<Projeto> buscarProjetoPorIdExternoJoinClientes(@Param("id") UUID idExterno);

  @Query("SELECT EXISTS (SELECT p FROM Projeto p WHERE p.idExterno=:id)")
  @Transactional(readOnly = true)
  Boolean existsByIdExterno(@Param("id") UUID idExterno);

  @Transactional(readOnly = true)
  Optional<Projeto> findByIdExterno(UUID idExterno);

  @Query("SELECT p FROM Projeto p LEFT JOIN FETCH p.atividades WHERE p.idExterno=:idExterno")
  @Transactional(readOnly = true)
  Optional<Projeto> findByIdExternoFetchAtividades(UUID idExterno);

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO PROJETOS_PARTICIPANTES(PROJETO_ID, CLIENTE_ID) VALUES (:idProjeto, :idCliente)", nativeQuery = true)
  void inserirCliente(Long idProjeto, Long idCliente);
}
