package com.posfarmacia.usecases.port.out.venta;

import com.posfarmacia.domain.model.venta.Venta;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida: persistencia del agregado Venta (RF05). */
public interface VentaRepositoryPort {

    Venta guardar(Venta venta);

    Optional<Venta> buscarPorId(UUID id);

    /** Siguiente numero correlativo de venta (RF05), usado al confirmar. */
    long siguienteNumeroCorrelativo();

    /** Filtro RF17: cualquier parametro nulo se ignora. */
    List<Venta> buscar(LocalDate fecha, UUID cajaId, UUID usuarioId, UUID clienteId);
}
