package com.posfarmacia.adapters.persistence.repository.catalogo;

import com.posfarmacia.adapters.persistence.entity.catalogo.LaboratorioJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratorioJpaRepository extends JpaRepository<LaboratorioJpaEntity, UUID> {
}
