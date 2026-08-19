package com.posfarmacia.adapters.persistence.credito;

import com.posfarmacia.adapters.persistence.mapper.credito.LineaCreditoMapper;
import com.posfarmacia.adapters.persistence.repository.credito.LineaCreditoJpaRepository;
import com.posfarmacia.usecases.port.out.credito.LineaCreditoRepositoryPort;
import com.posfarmacia.domain.model.credito.LineaCredito;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LineaCreditoRepositoryAdapter implements LineaCreditoRepositoryPort {

    private final LineaCreditoJpaRepository lineaCreditoJpaRepository;
    private final LineaCreditoMapper lineaCreditoMapper;

    public LineaCreditoRepositoryAdapter(LineaCreditoJpaRepository lineaCreditoJpaRepository, LineaCreditoMapper lineaCreditoMapper) {
        this.lineaCreditoJpaRepository = lineaCreditoJpaRepository;
        this.lineaCreditoMapper = lineaCreditoMapper;
    }

    @Override
    public LineaCredito guardar(LineaCredito lineaCredito) {
        var guardado = lineaCreditoJpaRepository.save(lineaCreditoMapper.toEntity(lineaCredito));
        return lineaCreditoMapper.toDomain(guardado);
    }

    @Override
    public Optional<LineaCredito> buscarPorId(UUID id) {
        return lineaCreditoJpaRepository.findById(id).map(lineaCreditoMapper::toDomain);
    }

    @Override
    public Optional<LineaCredito> buscarPorCliente(UUID clienteId) {
        return lineaCreditoJpaRepository.findByClienteId(clienteId).map(lineaCreditoMapper::toDomain);
    }
}
