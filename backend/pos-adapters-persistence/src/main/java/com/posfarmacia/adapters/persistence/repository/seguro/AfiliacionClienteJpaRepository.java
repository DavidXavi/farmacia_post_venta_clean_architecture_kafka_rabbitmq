package com.posfarmacia.adapters.persistence.repository.seguro;

import com.posfarmacia.adapters.persistence.entity.seguro.AfiliacionClienteJpaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AfiliacionClienteJpaRepository extends JpaRepository<AfiliacionClienteJpaEntity, UUID> {

    List<AfiliacionClienteJpaEntity> findByClienteId(UUID clienteId);

    Optional<AfiliacionClienteJpaEntity> findByClienteIdAndConvenioId(UUID clienteId, UUID convenioId);
}
