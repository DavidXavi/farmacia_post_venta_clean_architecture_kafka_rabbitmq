package com.posfarmacia.adapters.persistence.repository.anulacion;

import com.posfarmacia.adapters.persistence.entity.anulacion.NotaCreditoJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaCreditoJpaRepository extends JpaRepository<NotaCreditoJpaEntity, UUID> {
}
