package com.posfarmacia.adapters.persistence.seguro;

import com.posfarmacia.adapters.persistence.entity.seguro.ConvenioSeguroJpaEntity;
import com.posfarmacia.adapters.persistence.mapper.seguro.ConvenioSeguroMapper;
import com.posfarmacia.adapters.persistence.repository.seguro.CoberturaSeguroJpaRepository;
import com.posfarmacia.adapters.persistence.repository.seguro.ConvenioSeguroJpaRepository;
import com.posfarmacia.usecases.port.out.seguro.ConvenioSeguroRepositoryPort;
import com.posfarmacia.domain.model.seguro.ConvenioSeguro;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ConvenioSeguroRepositoryAdapter implements ConvenioSeguroRepositoryPort {

    private final ConvenioSeguroJpaRepository convenioSeguroJpaRepository;
    private final CoberturaSeguroJpaRepository coberturaSeguroJpaRepository;
    private final ConvenioSeguroMapper convenioSeguroMapper;

    public ConvenioSeguroRepositoryAdapter(ConvenioSeguroJpaRepository convenioSeguroJpaRepository,
                                            CoberturaSeguroJpaRepository coberturaSeguroJpaRepository,
                                            ConvenioSeguroMapper convenioSeguroMapper) {
        this.convenioSeguroJpaRepository = convenioSeguroJpaRepository;
        this.coberturaSeguroJpaRepository = coberturaSeguroJpaRepository;
        this.convenioSeguroMapper = convenioSeguroMapper;
    }

    @Override
    public ConvenioSeguro guardar(ConvenioSeguro convenio) {
        ConvenioSeguroJpaEntity guardado = convenioSeguroJpaRepository.save(convenioSeguroMapper.toEntity(convenio));
        convenio.getCoberturas().forEach(cobertura ->
                coberturaSeguroJpaRepository.save(convenioSeguroMapper.toEntity(cobertura)));
        var coberturas = coberturaSeguroJpaRepository.findByConvenioId(guardado.getId());
        return convenioSeguroMapper.toDomain(guardado, coberturas);
    }

    @Override
    public Optional<ConvenioSeguro> buscarPorId(UUID id) {
        return convenioSeguroJpaRepository.findById(id)
                .map(entity -> convenioSeguroMapper.toDomain(entity, coberturaSeguroJpaRepository.findByConvenioId(id)));
    }

    @Override
    public List<ConvenioSeguro> buscarTodos() {
        return convenioSeguroJpaRepository.findAll().stream()
                .map(entity -> convenioSeguroMapper.toDomain(entity,
                        coberturaSeguroJpaRepository.findByConvenioId(entity.getId())))
                .toList();
    }
}
