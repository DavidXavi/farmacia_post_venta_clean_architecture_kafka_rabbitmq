package com.posfarmacia.usecases.usecase.anulacion;

import com.posfarmacia.usecases.dto.anulacion.DetalleDevolucionResult;
import com.posfarmacia.usecases.dto.anulacion.DevolucionResult;
import com.posfarmacia.domain.model.anulacion.DetalleDevolucion;
import com.posfarmacia.domain.model.anulacion.Devolucion;

/** Traduce el agregado Devolucion a su DTO de salida. */
final class DevolucionResultMapper {

    private DevolucionResultMapper() {
    }

    static DevolucionResult aResultado(Devolucion devolucion) {
        return new DevolucionResult(
                devolucion.getId(),
                devolucion.getVentaId(),
                devolucion.getUsuarioId(),
                devolucion.getMotivo(),
                devolucion.getFecha(),
                devolucion.getTotal().monto(),
                devolucion.getDetalles().stream().map(DevolucionResultMapper::aDetalleResultado).toList());
    }

    private static DetalleDevolucionResult aDetalleResultado(DetalleDevolucion detalle) {
        return new DetalleDevolucionResult(
                detalle.getId(),
                detalle.getDetalleVentaId(),
                detalle.getProductoId(),
                detalle.getCantidad().valor(),
                detalle.getMontoDevuelto().monto());
    }
}
