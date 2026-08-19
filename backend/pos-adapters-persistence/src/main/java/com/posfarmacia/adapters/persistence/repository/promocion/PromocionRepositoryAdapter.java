package com.posfarmacia.adapters.persistence.repository.promocion;

import com.posfarmacia.adapters.persistence.mapper.promocion.PromocionMapper;
import com.posfarmacia.usecases.port.out.promocion.PromocionRepositoryPort;
import com.posfarmacia.domain.model.promocion.Promocion;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** Implementacion de {@link PromocionRepositoryPort} sobre Spring Data JPA. */
@Repository
public class PromocionRepositoryAdapter implements PromocionRepositoryPort {

    private final PromocionJpaRepository jpaRepository;
    private final PromocionMapper mapper;

    public PromocionRepositoryAdapter(PromocionJpaRepository jpaRepository, PromocionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Promocion> buscarVigentesPorProducto(UUID productoId, LocalDate hoy) {
        return jpaRepository.buscarVigentesPorProducto(productoId, hoy).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Promocion> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Promocion guardar(Promocion promocion) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(promocion)));
    }

    @Override
    public List<Promocion> listar() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
