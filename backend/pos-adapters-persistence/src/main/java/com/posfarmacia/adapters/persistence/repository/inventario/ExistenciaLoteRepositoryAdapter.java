package com.posfarmacia.adapters.persistence.repository.inventario;

import com.posfarmacia.adapters.persistence.mapper.inventario.ExistenciaLoteMapper;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.domain.model.inventario.ExistenciaLote;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ExistenciaLoteRepositoryAdapter implements ExistenciaLoteRepositoryPort {

    private final ExistenciaLoteJpaRepository jpaRepository;

    public ExistenciaLoteRepositoryAdapter(ExistenciaLoteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<ExistenciaLote> buscarPorProductoYLocal(UUID productoId, UUID localId) {
        return jpaRepository.findByProductoIdAndLocalId(productoId, localId).map(ExistenciaLoteMapper::aDominio);
    }

    @Override
    public List<ExistenciaLote> listarPorLocal(UUID localId) {
        return jpaRepository.findByLocalId(localId).stream().map(ExistenciaLoteMapper::aDominio).toList();
    }

    @Override
    public ExistenciaLote guardar(ExistenciaLote existencia) {
        var guardado = jpaRepository.save(ExistenciaLoteMapper.aEntidad(existencia));
        return ExistenciaLoteMapper.aDominio(guardado);
    }
}
