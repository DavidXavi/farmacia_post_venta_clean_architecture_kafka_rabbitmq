package com.posfarmacia.adapters.persistence.repository.cliente;

import com.posfarmacia.adapters.persistence.entity.cliente.ClienteJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, UUID> {

    Optional<ClienteJpaEntity> findByDni(String dni);
}
