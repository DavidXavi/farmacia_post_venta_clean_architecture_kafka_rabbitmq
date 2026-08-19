package com.posfarmacia.adapters.persistence.anulacion;

import com.posfarmacia.adapters.persistence.entity.anulacion.DevolucionJpaEntity;
import com.posfarmacia.adapters.persistence.mapper.anulacion.DetalleDevolucionMapper;
import com.posfarmacia.adapters.persistence.mapper.anulacion.DevolucionMapper;
import com.posfarmacia.adapters.persistence.repository.anulacion.DetalleDevolucionJpaRepository;
import com.posfarmacia.adapters.persistence.repository.anulacion.DevolucionJpaRepository;
import com.posfarmacia.usecases.port.out.anulacion.DevolucionRepositoryPort;
import com.posfarmacia.domain.model.anulacion.DetalleDevolucion;
import com.posfarmacia.domain.model.anulacion.Devolucion;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Implementacion de {@link DevolucionRepositoryPort} con Spring Data JPA. Una devolucion no vuelve
 * a modificarse una vez registrada (solo se crea), asi que a diferencia de {@code VentaRepositoryAdapter}
 * no hace falta borrar-y-reinsertar detalles en cada guardado.
 */
@Repository
public class DevolucionRepositoryAdapter implements DevolucionRepositoryPort {

    private final DevolucionJpaRepository devolucionJpaRepository;
    private final DetalleDevolucionJpaRepository detalleDevolucionJpaRepository;

    public DevolucionRepositoryAdapter(DevolucionJpaRepository devolucionJpaRepository,
            DetalleDevolucionJpaRepository detalleDevolucionJpaRepository) {
        this.devolucionJpaRepository = devolucionJpaRepository;
        this.detalleDevolucionJpaRepository = detalleDevolucionJpaRepository;
    }

    @Override
    public Devolucion guardar(Devolucion devolucion) {
        devolucionJpaRepository.save(DevolucionMapper.aEntidad(devolucion));
        for (DetalleDevolucion detalle : devolucion.getDetalles()) {
            detalleDevolucionJpaRepository.save(DetalleDevolucionMapper.aEntidad(detalle));
        }
        return devolucion;
    }

    @Override
    public List<Devolucion> buscarPorVenta(UUID ventaId) {
        return devolucionJpaRepository.findByVentaId(ventaId).stream().map(this::cargarAgregado).toList();
    }

    private Devolucion cargarAgregado(DevolucionJpaEntity entity) {
        List<DetalleDevolucion> detalles = detalleDevolucionJpaRepository.findByDevolucionId(entity.getId()).stream()
                .map(DetalleDevolucionMapper::aDominio)
                .toList();
        return DevolucionMapper.aDominio(entity, detalles);
    }
}
