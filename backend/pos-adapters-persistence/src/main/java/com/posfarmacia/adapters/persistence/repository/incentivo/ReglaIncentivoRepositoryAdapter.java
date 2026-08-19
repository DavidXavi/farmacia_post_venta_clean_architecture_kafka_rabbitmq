package com.posfarmacia.adapters.persistence.repository.incentivo;

import com.posfarmacia.adapters.persistence.mapper.incentivo.ReglaIncentivoMapper;
import com.posfarmacia.usecases.port.out.incentivo.ReglaIncentivoRepositoryPort;
import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** Implementacion de {@link ReglaIncentivoRepositoryPort} sobre Spring Data JPA. */
@Repository
public class ReglaIncentivoRepositoryAdapter implements ReglaIncentivoRepositoryPort {

    private final ReglaIncentivoJpaRepository jpaRepository;
    private final ReglaIncentivoMapper mapper;

    public ReglaIncentivoRepositoryAdapter(ReglaIncentivoJpaRepository jpaRepository, ReglaIncentivoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ReglaIncentivo guardar(ReglaIncentivo regla) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(regla)));
    }

    @Override
    public Optional<ReglaIncentivo> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ReglaIncentivo> listar() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
