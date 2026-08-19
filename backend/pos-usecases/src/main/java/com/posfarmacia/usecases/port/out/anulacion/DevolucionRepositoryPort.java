package com.posfarmacia.usecases.port.out.anulacion;

import com.posfarmacia.domain.model.anulacion.Devolucion;
import java.util.List;
import java.util.UUID;

/** Puerto de salida: persistencia del agregado Devolucion (RF16). */
public interface DevolucionRepositoryPort {

    Devolucion guardar(Devolucion devolucion);

    /** RF16: historial de devoluciones de una venta, usado tambien para calcular lo ya devuelto por linea. */
    List<Devolucion> buscarPorVenta(UUID ventaId);
}
