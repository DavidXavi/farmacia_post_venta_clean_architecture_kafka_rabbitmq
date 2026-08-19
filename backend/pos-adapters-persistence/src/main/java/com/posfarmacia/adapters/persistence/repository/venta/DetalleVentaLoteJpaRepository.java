package com.posfarmacia.adapters.persistence.repository.venta;

import com.posfarmacia.adapters.persistence.entity.venta.DetalleVentaLoteJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaLoteJpaRepository extends JpaRepository<DetalleVentaLoteJpaEntity, UUID> {

    List<DetalleVentaLoteJpaEntity> findByDetalleVentaId(UUID detalleVentaId);

    void deleteByDetalleVentaId(UUID detalleVentaId);
}
