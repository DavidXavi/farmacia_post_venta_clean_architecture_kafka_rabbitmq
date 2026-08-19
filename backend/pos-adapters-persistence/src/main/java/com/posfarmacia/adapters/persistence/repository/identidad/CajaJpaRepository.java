package com.posfarmacia.adapters.persistence.repository.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.CajaJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CajaJpaRepository extends JpaRepository<CajaJpaEntity, UUID> {
}
