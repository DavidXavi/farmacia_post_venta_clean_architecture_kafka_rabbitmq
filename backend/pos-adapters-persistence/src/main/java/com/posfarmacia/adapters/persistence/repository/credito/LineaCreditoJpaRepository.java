package com.posfarmacia.adapters.persistence.repository.credito;

import com.posfarmacia.adapters.persistence.entity.credito.LineaCreditoJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineaCreditoJpaRepository extends JpaRepository<LineaCreditoJpaEntity, UUID> {

    Optional<LineaCreditoJpaEntity> findByClienteId(UUID clienteId);
}
