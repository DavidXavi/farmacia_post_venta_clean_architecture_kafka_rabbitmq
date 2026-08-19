package com.posfarmacia.adapters.persistence.repository.promocion;

import com.posfarmacia.adapters.persistence.entity.promocion.PromocionJpaEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromocionJpaRepository extends JpaRepository<PromocionJpaEntity, UUID> {

    @Query("""
            select distinct p from PromocionJpaEntity p
            join p.condiciones c
            where c.productoId = :productoId
              and p.activa = true
              and (p.vigenciaInicio is null or p.vigenciaInicio <= :hoy)
              and (p.vigenciaFin is null or p.vigenciaFin >= :hoy)
            """)
    List<PromocionJpaEntity> buscarVigentesPorProducto(@Param("productoId") UUID productoId, @Param("hoy") LocalDate hoy);
}
