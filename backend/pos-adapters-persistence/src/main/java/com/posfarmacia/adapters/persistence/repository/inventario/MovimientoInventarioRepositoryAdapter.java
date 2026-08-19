package com.posfarmacia.adapters.persistence.repository.inventario;

import com.posfarmacia.adapters.persistence.mapper.inventario.MovimientoInventarioMapper;
import com.posfarmacia.usecases.port.out.inventario.MovimientoInventarioRepositoryPort;
import com.posfarmacia.domain.model.inventario.MovimientoInventario;
import org.springframework.stereotype.Component;

@Component
public class MovimientoInventarioRepositoryAdapter implements MovimientoInventarioRepositoryPort {

    private final MovimientoInventarioJpaRepository jpaRepository;

    public MovimientoInventarioRepositoryAdapter(MovimientoInventarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MovimientoInventario guardar(MovimientoInventario movimiento) {
        var guardado = jpaRepository.save(MovimientoInventarioMapper.aEntidad(movimiento));
        return MovimientoInventarioMapper.aDominio(guardado);
    }
}
