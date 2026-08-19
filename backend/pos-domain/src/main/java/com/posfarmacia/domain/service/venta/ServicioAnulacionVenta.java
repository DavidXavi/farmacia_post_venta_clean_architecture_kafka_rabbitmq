package com.posfarmacia.domain.service.venta;

import com.posfarmacia.domain.model.venta.Venta;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de dominio puro (sin I/O), equivalente a PosFarmacia.Domain.Services.ServicioAnulacionVenta
 * (.NET): decide si una venta requiere nota de credito (RN39/RN40, contexto futuro, no implementado
 * aqui) y calcula las reversiones de stock que corresponden a una anulacion (RN42).
 */
public final class ServicioAnulacionVenta {

    /** RN39/RN40: una venta que no es del mismo dia debe regularizarse con nota de credito, no con anulacion directa. */
    public boolean requiereNotaCredito(Venta venta, LocalDate hoy) {
        return !venta.esDelMismoDia(hoy);
    }

    /** RN42: cuanta cantidad debe devolverse a cada lote original que fue usado para despachar la venta. */
    public List<ReversionStock> obtenerReversionesDeStock(Venta venta) {
        return venta.getDetalles().stream()
                .flatMap(detalle -> detalle.getLotes().stream())
                .map(lote -> new ReversionStock(lote.getLoteId(), lote.getCantidadTomada()))
                .toList();
    }
}
