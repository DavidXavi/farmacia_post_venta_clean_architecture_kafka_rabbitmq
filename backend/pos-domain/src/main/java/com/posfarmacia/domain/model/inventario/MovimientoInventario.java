package com.posfarmacia.domain.model.inventario;

import com.posfarmacia.domain.enums.TipoMovimientoStock;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.Instant;
import java.util.UUID;

/**
 * Movimiento de inventario sobre un lote (RF04/RF15): ingreso, salida, ajustes y reversiones.
 * La fecha se recibe como parametro (via ClockPort en la capa de aplicacion), nunca Instant.now() en el dominio.
 */
public final class MovimientoInventario extends Entidad {

    private final UUID loteId;
    private final TipoMovimientoStock tipo;
    private final Cantidad cantidad;
    private final UUID usuarioId;
    private final String referencia;
    private final Instant fecha;

    public MovimientoInventario(UUID loteId, TipoMovimientoStock tipo, Cantidad cantidad, UUID usuarioId,
            String referencia, Instant fecha) {
        super();
        this.loteId = requireNoNulo(loteId, "El lote del movimiento es obligatorio.");
        this.tipo = requireNoNulo(tipo, "El tipo de movimiento es obligatorio.");
        this.cantidad = requireNoNulo(cantidad, "La cantidad del movimiento es obligatoria.");
        this.usuarioId = requireNoNulo(usuarioId, "El usuario responsable del movimiento es obligatorio.");
        this.referencia = referencia;
        this.fecha = requireNoNulo(fecha, "La fecha del movimiento es obligatoria.");
    }

    private MovimientoInventario(UUID id, UUID loteId, TipoMovimientoStock tipo, Cantidad cantidad, UUID usuarioId,
            String referencia, Instant fecha) {
        super(id);
        this.loteId = loteId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.usuarioId = usuarioId;
        this.referencia = referencia;
        this.fecha = fecha;
    }

    /** Usado por el mapper de persistencia para reconstruir el agregado desde su estado guardado. */
    public static MovimientoInventario reconstruir(UUID id, UUID loteId, TipoMovimientoStock tipo, Cantidad cantidad,
            UUID usuarioId, String referencia, Instant fecha) {
        return new MovimientoInventario(id, loteId, tipo, cantidad, usuarioId, referencia, fecha);
    }

    private static <T> T requireNoNulo(T valor, String mensaje) {
        if (valor == null) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public TipoMovimientoStock getTipo() {
        return tipo;
    }

    public Cantidad getCantidad() {
        return cantidad;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getReferencia() {
        return referencia;
    }

    public Instant getFecha() {
        return fecha;
    }
}
