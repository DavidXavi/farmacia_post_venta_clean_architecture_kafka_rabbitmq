package com.posfarmacia.domain.service.anulacion;

import com.posfarmacia.domain.enums.EstadoVenta;
import com.posfarmacia.domain.exception.DevolucionInvalidaException;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Servicio de dominio puro (sin I/O), equivalente a PosFarmacia.Domain.Services.ValidadorDevolucion
 * (.NET): valida que una linea de venta pueda devolverse. Recibe todo por parametro (nunca importa
 * {@code Venta} ni {@code Producto} de otros contextos, solo los datos primitivos que necesita) para
 * mantenerse desacoplado y comprobable con fechas fijas en tests.
 */
public final class ValidadorDevolucion {

    // ponytail: plazo fijo de 30 dias (RF16); si la farmacia necesita un plazo configurable por
    // producto/categoria, se agrega como parametro cuando exista ese requerimiento real.
    private static final int DIAS_PLAZO_DEVOLUCION = 30;

    /**
     * @param estadoVenta        estado actual de la venta (solo una CONFIRMADA admite devolucion).
     * @param fechaVenta         fecha/hora en que se confirmo la venta.
     * @param productoControlado si el producto de la linea es controlado (RN14: nunca se admite devolucion).
     * @param cantidadVendida    cantidad original de la linea de venta.
     * @param cantidadYaDevuelta cantidad ya devuelta en devoluciones previas de esa misma linea.
     * @param cantidadSolicitada cantidad que se quiere devolver ahora.
     * @param hoy                fecha actual, provista por {@code ClockPort} en la capa de aplicacion.
     */
    public void validar(EstadoVenta estadoVenta, Instant fechaVenta, boolean productoControlado,
            Cantidad cantidadVendida, Cantidad cantidadYaDevuelta, Cantidad cantidadSolicitada, LocalDate hoy) {

        if (estadoVenta != EstadoVenta.CONFIRMADA) {
            throw new DevolucionInvalidaException("Solo se puede devolver una venta confirmada.");
        }

        if (productoControlado) {
            throw new DevolucionInvalidaException("El producto es controlado y no admite devolucion.");
        }

        LocalDate fechaVentaLocal = fechaVenta.atZone(ZoneId.systemDefault()).toLocalDate();
        long diasTranscurridos = ChronoUnit.DAYS.between(fechaVentaLocal, hoy);
        if (diasTranscurridos > DIAS_PLAZO_DEVOLUCION) {
            throw new DevolucionInvalidaException(
                    "La venta supera el plazo de " + DIAS_PLAZO_DEVOLUCION + " dias permitido para devoluciones.");
        }

        int disponibleParaDevolver = cantidadVendida.valor() - cantidadYaDevuelta.valor();
        if (cantidadSolicitada.valor() <= 0 || cantidadSolicitada.valor() > disponibleParaDevolver) {
            throw new DevolucionInvalidaException(
                    "La cantidad a devolver supera lo vendido menos lo ya devuelto en esa linea.");
        }
    }
}
