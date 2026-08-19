package com.posfarmacia.adapters.persistence.repository.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.RegistroAuditoriaJpaEntity;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuditoriaJpaRepository extends JpaRepository<RegistroAuditoriaJpaEntity, UUID> {

    @Query("""
            SELECT a FROM RegistroAuditoriaJpaEntity a
            WHERE a.fecha >= :desde AND a.fecha < :hasta
              AND (:entidad IS NULL OR a.entidad = :entidad)
              AND (:usuarioId IS NULL OR a.usuarioId = :usuarioId)
            ORDER BY a.fecha DESC
            """)
    List<RegistroAuditoriaJpaEntity> buscar(@Param("desde") Instant desde, @Param("hasta") Instant hasta,
                                             @Param("entidad") String entidad, @Param("usuarioId") UUID usuarioId);
}
