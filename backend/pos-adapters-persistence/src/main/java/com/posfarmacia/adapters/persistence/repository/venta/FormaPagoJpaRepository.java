package com.posfarmacia.adapters.persistence.repository.venta;

import com.posfarmacia.adapters.persistence.entity.venta.FormaPagoJpaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormaPagoJpaRepository extends JpaRepository<FormaPagoJpaEntity, UUID> {

    Optional<FormaPagoJpaEntity> findFirstByTipoAndActivoTrue(String tipo);

    List<FormaPagoJpaEntity> findByActivoTrue();
}
