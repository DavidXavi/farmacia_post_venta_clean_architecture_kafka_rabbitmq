package com.posfarmacia.adapters.persistence.repository.incentivo;

import com.posfarmacia.adapters.persistence.entity.incentivo.IncentivoVentaJpaEntity;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IncentivoVentaJpaRepository extends JpaRepository<IncentivoVentaJpaEntity, UUID> {

    @Query("""
            select i from IncentivoVentaJpaEntity i
            where i.fecha >= :desde
              and i.fecha < :hasta
              and (:usuarioId is null or i.usuarioId = :usuarioId)
            """)
    List<IncentivoVentaJpaEntity> buscar(@Param("desde") Instant desde, @Param("hasta") Instant hasta,
            @Param("usuarioId") UUID usuarioId);
}
