package com.posfarmacia.adapters.persistence.repository.catalogo;

import com.posfarmacia.adapters.persistence.entity.catalogo.ProductoJpaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoJpaRepository extends JpaRepository<ProductoJpaEntity, UUID> {

    Optional<ProductoJpaEntity> findByCodigoBarras(String codigoBarras);

    Optional<ProductoJpaEntity> findByCodigoInterno(String codigoInterno);

    @Query("""
            select p from ProductoJpaEntity p
            where (:patron is null or lower(p.nombreComercial) like :patron or lower(p.codigoInterno) like :patron)
            and (:categoriaId is null or p.categoriaId = :categoriaId)
            and (:laboratorioId is null or p.laboratorioId = :laboratorioId)
            """)
    List<ProductoJpaEntity> buscar(@Param("patron") String patron, @Param("categoriaId") UUID categoriaId,
            @Param("laboratorioId") UUID laboratorioId);
}
