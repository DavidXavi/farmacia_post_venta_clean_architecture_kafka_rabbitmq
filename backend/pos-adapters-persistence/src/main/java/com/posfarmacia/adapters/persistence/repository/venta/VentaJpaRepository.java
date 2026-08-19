package com.posfarmacia.adapters.persistence.repository.venta;

import com.posfarmacia.adapters.persistence.entity.venta.VentaJpaEntity;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaJpaRepository extends JpaRepository<VentaJpaEntity, UUID> {

    @Query("SELECT COALESCE(MAX(v.numeroCorrelativo), 0) + 1 FROM VentaJpaEntity v")
    long siguienteNumeroCorrelativo();

    @Query("""
            SELECT v FROM VentaJpaEntity v
            WHERE v.fecha >= :desde AND v.fecha < :hasta
              AND (:cajaId IS NULL OR v.cajaId = :cajaId)
              AND (:usuarioId IS NULL OR v.usuarioId = :usuarioId)
              AND (:clienteId IS NULL OR v.clienteId = :clienteId)
            """)
    List<VentaJpaEntity> buscar(@Param("desde") Instant desde, @Param("hasta") Instant hasta,
            @Param("cajaId") UUID cajaId, @Param("usuarioId") UUID usuarioId, @Param("clienteId") UUID clienteId);
}
