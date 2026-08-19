package com.posfarmacia.adapters.persistence.repository.seguro;

import com.posfarmacia.adapters.persistence.entity.seguro.ConvenioSeguroJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvenioSeguroJpaRepository extends JpaRepository<ConvenioSeguroJpaEntity, UUID> {
}
