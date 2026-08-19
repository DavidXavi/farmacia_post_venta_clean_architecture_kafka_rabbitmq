package com.posfarmacia.domain.model.anulacion;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.util.UUID;

/**
 * Linea de una {@link Devolucion}: cuanto se devolvio de una linea de venta puntual y su monto
 * (RN42). Equivalente a PosFarmacia.Domain.Entities.DetalleDevolucion (.NET, clase anidada de
 * Devolucion en el .NET original; aqui se separa en su propio archivo por convencion del proyecto).
 */
public final class DetalleDevolucion extends Entidad {

    private final UUID devolucionId;
    private final UUID detalleVentaId;
    private final UUID productoId;
    private final Cantidad cantidad;
    private final Dinero montoDevuelto;

    DetalleDevolucion(UUID devolucionId, UUID detalleVentaId, UUID productoId, Cantidad cantidad, Dinero montoDevuelto) {
        super();
        this.devolucionId = requireNoNulo(devolucionId, "La devolucion de la linea es obligatoria.");
        this.detalleVentaId = requireNoNulo(detalleVentaId, "La linea de venta devuelta es obligatoria.");
        this.productoId = requireNoNulo(productoId, "El producto de la linea devuelta es obligatorio.");
        this.cantidad = requireNoNulo(cantidad, "La cantidad devuelta es obligatoria.");
        this.montoDevuelto = requireNoNulo(montoDevuelto, "El monto devuelto es obligatorio.");
    }

    /** Constructor de reconstruccion usado por el mapper de persistencia (preserva el id existente). */
    private DetalleDevolucion(UUID id, UUID devolucionId, UUID detalleVentaId, UUID productoId, Cantidad cantidad,
            Dinero montoDevuelto) {
        super(id);
        this.devolucionId = devolucionId;
        this.detalleVentaId = detalleVentaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.montoDevuelto = montoDevuelto;
    }

    public static DetalleDevolucion reconstruir(UUID id, UUID devolucionId, UUID detalleVentaId, UUID productoId,
            Cantidad cantidad, Dinero montoDevuelto) {
        return new DetalleDevolucion(id, devolucionId, detalleVentaId, productoId, cantidad, montoDevuelto);
    }

    private static <T> T requireNoNulo(T valor, String mensaje) {
        if (valor == null) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    public UUID getDevolucionId() {
        return devolucionId;
    }

    public UUID getDetalleVentaId() {
        return detalleVentaId;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public Cantidad getCantidad() {
        return cantidad;
    }

    public Dinero getMontoDevuelto() {
        return montoDevuelto;
    }
}
