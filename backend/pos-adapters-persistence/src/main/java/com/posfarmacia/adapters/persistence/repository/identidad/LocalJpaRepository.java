package com.posfarmacia.adapters.persistence.repository.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.LocalJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalJpaRepository extends JpaRepository<LocalJpaEntity, UUID> {
}
