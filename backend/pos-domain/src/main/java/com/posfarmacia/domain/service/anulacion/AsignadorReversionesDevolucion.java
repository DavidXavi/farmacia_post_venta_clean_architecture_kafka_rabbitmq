package com.posfarmacia.domain.service.anulacion;

import com.posfarmacia.domain.exception.DevolucionInvalidaException;
import com.posfarmacia.domain.model.venta.DetalleVentaLote;
import com.posfarmacia.domain.service.venta.ReversionStock;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de dominio puro, equivalente a PosFarmacia.Domain.Services.AsignadorReversionesDevolucion
 * (.NET): distribuye la cantidad devuelta en una devolucion parcial entre los mismos lotes que
 * surtieron originalmente la linea de venta (RN42), respetando el orden en que fueron asignados por
 * FEFO. No existia aun en el contexto de Ventas (que solo cubre la reversion de una anulacion total
 * en {@code ServicioAnulacionVenta}), asi que se crea aqui; reutiliza el record {@link ReversionStock}
 * ya definido por Ventas para no duplicar tipo.
 */
public final class AsignadorReversionesDevolucion {

    public List<ReversionStock> asignar(List<DetalleVentaLote> lotesOriginales, Cantidad cantidadADevolver) {
        int restante = cantidadADevolver.valor();
        List<ReversionStock> resultado = new ArrayList<>();

        for (DetalleVentaLote loteAsignado : lotesOriginales) {
            if (restante <= 0) {
                break;
            }
            int tomar = Math.min(restante, loteAsignado.getCantidadTomada().valor());
            resultado.add(new ReversionStock(loteAsignado.getLoteId(), new Cantidad(tomar)));
            restante -= tomar;
        }

        if (restante > 0) {
            throw new DevolucionInvalidaException(
                    "No se pudo distribuir la cantidad a devolver entre los lotes originales de la venta.");
        }

        return resultado;
    }
}
