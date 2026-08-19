package com.posfarmacia.domain.service.inventario;

import com.posfarmacia.domain.exception.StockInsuficienteException;
import com.posfarmacia.domain.model.inventario.Lote;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de dominio puro (RF13, RN33-RN38): asigna el despacho de una cantidad solicitada de un producto
 * entre sus lotes disponibles, priorizando el vencimiento mas cercano (FEFO), excluyendo lotes vencidos o
 * dentro del periodo preventivo de vencimiento. No hace I/O ni conoce puertos: recibe todos los datos
 * (incluida la fecha "hoy") como parametros.
 */
public final class AsignadorLotesFEFO {

    /**
     * @param productoId        producto para el cual se despacha (filtra los lotes que no le pertenezcan).
     * @param cantidadRequerida cantidad total a despachar.
     * @param hoy                fecha de referencia para evaluar vencimiento y periodo preventivo.
     * @param lotesDisponibles  lotes candidatos del producto, en cualquier estado y orden.
     * @return asignaciones ordenadas por vencimiento mas proximo primero, cubriendo exactamente la cantidad requerida.
     * @throws StockInsuficienteException si el stock vendible total no alcanza la cantidad requerida.
     */
    public List<AsignacionLote> asignar(UUID productoId, Cantidad cantidadRequerida, LocalDate hoy,
            List<Lote> lotesDisponibles) {
        List<Lote> vendiblesOrdenados = lotesDisponibles.stream()
                .filter(lote -> lote.getProductoId().equals(productoId))
                .filter(lote -> lote.esVendible(hoy))
                .sorted(Comparator.comparing(lote -> lote.getFechaVencimiento().valor()))
                .toList();

        List<AsignacionLote> asignaciones = new ArrayList<>();
        int restante = cantidadRequerida.valor();

        for (Lote lote : vendiblesOrdenados) {
            if (restante <= 0) {
                break;
            }
            int disponible = lote.getCantidadDisponible().valor();
            int tomar = Math.min(restante, disponible);
            if (tomar <= 0) {
                continue;
            }
            asignaciones.add(new AsignacionLote(lote.getId(), new Cantidad(tomar)));
            restante -= tomar;
        }

        if (restante > 0) {
            throw new StockInsuficienteException(
                    "No hay stock vendible suficiente entre los lotes disponibles para completar la cantidad solicitada.");
        }

        return List.copyOf(asignaciones);
    }
}
