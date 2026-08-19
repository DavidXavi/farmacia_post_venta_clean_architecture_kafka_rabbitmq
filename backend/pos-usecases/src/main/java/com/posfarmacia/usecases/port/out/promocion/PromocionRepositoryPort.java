package com.posfarmacia.usecases.port.out.promocion;

import com.posfarmacia.domain.model.promocion.Promocion;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida hacia el repositorio de promociones (implementado en pos-adapters-persistence). */
public interface PromocionRepositoryPort {

    /** Promociones activas y vigentes hoy que aplican al producto indicado. */
    List<Promocion> buscarVigentesPorProducto(UUID productoId, LocalDate hoy);

    Optional<Promocion> buscarPorId(UUID id);

    /** Alta o actualizacion (CRUD de administracion, RF06). */
    Promocion guardar(Promocion promocion);

    /** Todas las promociones registradas, activas e inactivas (CRUD de administracion). */
    List<Promocion> listar();
}
