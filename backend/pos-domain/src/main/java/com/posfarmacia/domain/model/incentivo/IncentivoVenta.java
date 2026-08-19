package com.posfarmacia.domain.model.incentivo;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.Instant;
import java.util.UUID;

/**
 * Registro calculado de un incentivo ganado por una linea de venta (RF18): referencia por UUID a
 * la regla aplicada, al trabajador y a la venta/linea que lo origino. Equivalente a
 * PosFarmacia.Domain.Entities.IncentivoVenta (.NET); a diferencia del original, {@code fecha} se
 * recibe como parametro (nunca {@code Instant.now()} directo) para que el calculo sea comprobable
 * con fechas fijas en tests, tal como exige ClockPort en este proyecto.
 */
public final class IncentivoVenta extends Entidad {

    private final UUID reglaIncentivoId;
    private final UUID usuarioId;
    private final UUID ventaId;
    private final UUID detalleVentaId;
    private final Cantidad cantidad;
    private final Dinero montoCalculado;
    private final Instant fecha;

    public IncentivoVenta(UUID reglaIncentivoId, UUID usuarioId, UUID ventaId, UUID detalleVentaId,
            Cantidad cantidad, Dinero montoCalculado, Instant fecha) {
        super();
        this.reglaIncentivoId = requireNoNulo(reglaIncentivoId, "La regla de incentivo es obligatoria.");
        this.usuarioId = requireNoNulo(usuarioId, "El usuario del incentivo es obligatorio.");
        this.ventaId = requireNoNulo(ventaId, "La venta del incentivo es obligatoria.");
        this.detalleVentaId = requireNoNulo(detalleVentaId, "La linea de venta del incentivo es obligatoria.");
        this.cantidad = requireNoNulo(cantidad, "La cantidad vendida es obligatoria.");
        this.montoCalculado = requireNoNulo(montoCalculado, "El monto calculado es obligatorio.");
        this.fecha = requireNoNulo(fecha, "La fecha del incentivo es obligatoria.");
    }

    /** Constructor de reconstruccion usado por el mapper de persistencia (preserva el id existente). */
    private IncentivoVenta(UUID id, UUID reglaIncentivoId, UUID usuarioId, UUID ventaId, UUID detalleVentaId,
            Cantidad cantidad, Dinero montoCalculado, Instant fecha) {
        super(id);
        this.reglaIncentivoId = reglaIncentivoId;
        this.usuarioId = usuarioId;
        this.ventaId = ventaId;
        this.detalleVentaId = detalleVentaId;
        this.cantidad = cantidad;
        this.montoCalculado = montoCalculado;
        this.fecha = fecha;
    }

    /** Usado por el mapper de persistencia para reconstruir el registro desde su estado guardado. */
    public static IncentivoVenta reconstruir(UUID id, UUID reglaIncentivoId, UUID usuarioId, UUID ventaId,
            UUID detalleVentaId, Cantidad cantidad, Dinero montoCalculado, Instant fecha) {
        return new IncentivoVenta(id, reglaIncentivoId, usuarioId, ventaId, detalleVentaId, cantidad, montoCalculado,
                fecha);
    }

    private static <T> T requireNoNulo(T valor, String mensaje) {
        if (valor == null) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    public UUID getReglaIncentivoId() {
        return reglaIncentivoId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getDetalleVentaId() {
        return detalleVentaId;
    }

    public Cantidad getCantidad() {
        return cantidad;
    }

    public Dinero getMontoCalculado() {
        return montoCalculado;
    }

    public Instant getFecha() {
        return fecha;
    }
}
