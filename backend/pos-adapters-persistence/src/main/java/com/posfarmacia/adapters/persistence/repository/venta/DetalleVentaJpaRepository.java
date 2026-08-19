package com.posfarmacia.adapters.persistence.repository.venta;

import com.posfarmacia.adapters.persistence.entity.venta.DetalleVentaJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaJpaRepository extends JpaRepository<DetalleVentaJpaEntity, UUID> {

    List<DetalleVentaJpaEntity> findByVentaId(UUID ventaId);

    void deleteByVentaId(UUID ventaId);
}
