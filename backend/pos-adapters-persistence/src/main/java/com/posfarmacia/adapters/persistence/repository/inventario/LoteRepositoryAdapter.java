package com.posfarmacia.adapters.persistence.repository.inventario;

import com.posfarmacia.adapters.persistence.entity.inventario.LoteJpaEntity;
import com.posfarmacia.adapters.persistence.mapper.inventario.LoteMapper;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.domain.model.inventario.Lote;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LoteRepositoryAdapter implements LoteRepositoryPort {

    private final LoteJpaRepository jpaRepository;

    public LoteRepositoryAdapter(LoteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Lote guardar(Lote lote) {
        var guardado = jpaRepository.save(LoteMapper.aEntidad(lote));
        return LoteMapper.aDominio(guardado);
    }

    @Override
    public Optional<Lote> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(LoteMapper::aDominio);
    }

    @Override
    public List<Lote> listar(UUID productoId) {
        List<LoteJpaEntity> entidades = productoId == null
                ? jpaRepository.findAll()
                : jpaRepository.findByProductoId(productoId);
        return entidades.stream().map(LoteMapper::aDominio).toList();
    }

    @Override
    public List<Lote> listarPorProductoYLocal(UUID productoId, UUID localId) {
        return jpaRepository.findByProductoIdAndLocalId(productoId, localId).stream()
                .map(LoteMapper::aDominio)
                .toList();
    }
}
