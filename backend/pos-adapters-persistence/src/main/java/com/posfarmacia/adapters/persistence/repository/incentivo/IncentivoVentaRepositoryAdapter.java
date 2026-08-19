package com.posfarmacia.adapters.persistence.repository.incentivo;

import com.posfarmacia.adapters.persistence.mapper.incentivo.IncentivoVentaMapper;
import com.posfarmacia.usecases.port.out.incentivo.IncentivoVentaRepositoryPort;
import com.posfarmacia.domain.model.incentivo.IncentivoVenta;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** Implementacion de {@link IncentivoVentaRepositoryPort} sobre Spring Data JPA. */
@Repository
public class IncentivoVentaRepositoryAdapter implements IncentivoVentaRepositoryPort {

    private final IncentivoVentaJpaRepository jpaRepository;
    private final IncentivoVentaMapper mapper;

    public IncentivoVentaRepositoryAdapter(IncentivoVentaJpaRepository jpaRepository, IncentivoVentaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public IncentivoVenta guardar(IncentivoVenta incentivo) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(incentivo)));
    }

    @Override
    public List<IncentivoVenta> buscar(LocalDate desde, LocalDate hasta, UUID usuarioId) {
        ZoneId zona = ZoneId.systemDefault();
        Instant inicio = desde.atStartOfDay(zona).toInstant();
        Instant fin = hasta.plusDays(1).atStartOfDay(zona).toInstant();
        return jpaRepository.buscar(inicio, fin, usuarioId).stream().map(mapper::toDomain).toList();
    }
}
