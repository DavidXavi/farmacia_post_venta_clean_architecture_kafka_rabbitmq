package com.posfarmacia.adapters.persistence.repository.inventario;

import com.posfarmacia.adapters.persistence.entity.inventario.MovimientoInventarioJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoInventarioJpaRepository extends JpaRepository<MovimientoInventarioJpaEntity, UUID> {
}
