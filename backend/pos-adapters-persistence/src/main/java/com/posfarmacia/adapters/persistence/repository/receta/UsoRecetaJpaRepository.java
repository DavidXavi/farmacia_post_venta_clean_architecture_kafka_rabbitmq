package com.posfarmacia.adapters.persistence.repository.receta;

import com.posfarmacia.adapters.persistence.entity.receta.UsoRecetaJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsoRecetaJpaRepository extends JpaRepository<UsoRecetaJpaEntity, UUID> {

    List<UsoRecetaJpaEntity> findByRecetaId(UUID recetaId);
}
