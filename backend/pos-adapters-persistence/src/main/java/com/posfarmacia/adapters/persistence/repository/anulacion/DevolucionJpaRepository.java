package com.posfarmacia.adapters.persistence.repository.anulacion;

import com.posfarmacia.adapters.persistence.entity.anulacion.DevolucionJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevolucionJpaRepository extends JpaRepository<DevolucionJpaEntity, UUID> {

    List<DevolucionJpaEntity> findByVentaId(UUID ventaId);
}
