package br.com.agsistemas.repository;

import br.com.agsistemas.model.Cliente;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

  @Transactional(readOnly = true)
  Optional<Cliente> findByIdExterno(UUID idExterno);

  @Query("SELECT EXISTS (SELECT c FROM Cliente c WHERE c.idExterno=:idExterno)")
  @Transactional(readOnly = true)
  Boolean existsCliente(UUID idExterno);

  @Query(value = "SELECT c.* FROM PROJETOS_PARTICIPANTES pp INNER JOIN Cliente c ON c.id=pp.cliente_id INNER JOIN Projeto p ON p.id=pp.projeto_id WHERE c.id_externo=:idExterno AND p.id_externo=:id", nativeQuery = true)
  @Transactional(readOnly = true)
  Optional<Cliente> buscarPeloProjeto(UUID idExterno, @Param("id") UUID idExternoProjeto);
}
