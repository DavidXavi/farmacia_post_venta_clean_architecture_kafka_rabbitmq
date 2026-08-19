package com.posfarmacia.adapters.persistence.seguro;

import com.posfarmacia.adapters.persistence.mapper.seguro.AfiliacionClienteMapper;
import com.posfarmacia.adapters.persistence.repository.seguro.AfiliacionClienteJpaRepository;
import com.posfarmacia.usecases.port.out.seguro.AfiliacionClienteRepositoryPort;
import com.posfarmacia.domain.model.seguro.AfiliacionCliente;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AfiliacionClienteRepositoryAdapter implements AfiliacionClienteRepositoryPort {

    private final AfiliacionClienteJpaRepository afiliacionClienteJpaRepository;
    private final AfiliacionClienteMapper afiliacionClienteMapper;

    public AfiliacionClienteRepositoryAdapter(AfiliacionClienteJpaRepository afiliacionClienteJpaRepository,
                                               AfiliacionClienteMapper afiliacionClienteMapper) {
        this.afiliacionClienteJpaRepository = afiliacionClienteJpaRepository;
        this.afiliacionClienteMapper = afiliacionClienteMapper;
    }

    @Override
    public AfiliacionCliente guardar(AfiliacionCliente afiliacion) {
        var guardado = afiliacionClienteJpaRepository.save(afiliacionClienteMapper.toEntity(afiliacion));
        return afiliacionClienteMapper.toDomain(guardado);
    }

    @Override
    public Optional<AfiliacionCliente> buscarPorId(UUID id) {
        return afiliacionClienteJpaRepository.findById(id).map(afiliacionClienteMapper::toDomain);
    }

    @Override
    public List<AfiliacionCliente> buscarPorCliente(UUID clienteId) {
        return afiliacionClienteJpaRepository.findByClienteId(clienteId).stream()
                .map(afiliacionClienteMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<AfiliacionCliente> buscarPorClienteYConvenio(UUID clienteId, UUID convenioId) {
        return afiliacionClienteJpaRepository.findByClienteIdAndConvenioId(clienteId, convenioId)
                .map(afiliacionClienteMapper::toDomain);
    }
}
