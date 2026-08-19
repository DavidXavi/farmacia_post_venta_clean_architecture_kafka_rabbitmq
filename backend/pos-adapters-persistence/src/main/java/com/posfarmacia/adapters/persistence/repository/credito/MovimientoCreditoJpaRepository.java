package com.posfarmacia.adapters.persistence.repository.credito;

import com.posfarmacia.adapters.persistence.entity.credito.MovimientoCreditoJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoCreditoJpaRepository extends JpaRepository<MovimientoCreditoJpaEntity, UUID> {

    List<MovimientoCreditoJpaEntity> findByLineaCreditoId(UUID lineaCreditoId);
}
