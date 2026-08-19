package com.posfarmacia.adapters.persistence.repository.catalogo;

import com.posfarmacia.adapters.persistence.entity.catalogo.PresentacionJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentacionJpaRepository extends JpaRepository<PresentacionJpaEntity, UUID> {
}
