package com.posfarmacia.adapters.persistence.anulacion;

import com.posfarmacia.adapters.persistence.mapper.anulacion.NotaCreditoMapper;
import com.posfarmacia.adapters.persistence.repository.anulacion.NotaCreditoJpaRepository;
import com.posfarmacia.usecases.port.out.anulacion.NotaCreditoRepositoryPort;
import com.posfarmacia.domain.model.anulacion.NotaCredito;
import org.springframework.stereotype.Repository;

@Repository
public class NotaCreditoRepositoryAdapter implements NotaCreditoRepositoryPort {

    private final NotaCreditoJpaRepository notaCreditoJpaRepository;

    public NotaCreditoRepositoryAdapter(NotaCreditoJpaRepository notaCreditoJpaRepository) {
        this.notaCreditoJpaRepository = notaCreditoJpaRepository;
    }

    @Override
    public NotaCredito guardar(NotaCredito notaCredito) {
        notaCreditoJpaRepository.save(NotaCreditoMapper.aEntidad(notaCredito));
        return notaCredito;
    }
}
