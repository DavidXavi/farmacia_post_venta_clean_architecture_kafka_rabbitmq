package com.posfarmacia.adapters.persistence.repository.incentivo;

import com.posfarmacia.adapters.persistence.entity.incentivo.ReglaIncentivoJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReglaIncentivoJpaRepository extends JpaRepository<ReglaIncentivoJpaEntity, UUID> {
}
