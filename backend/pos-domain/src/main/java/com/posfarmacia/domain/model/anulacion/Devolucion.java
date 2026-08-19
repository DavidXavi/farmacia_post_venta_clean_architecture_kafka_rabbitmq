package com.posfarmacia.domain.model.anulacion;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Devolucion parcial de una venta ya confirmada (RN42, RF16): una o mas lineas devueltas, cada una
 * con su propia cantidad y monto. Referencia a la {@code Venta} solo por UUID (regla de oro de
 * migracion: los agregados de otros contextos nunca se importan como clase). Equivalente a
 * PosFarmacia.Domain.Entities.Devolucion (.NET).
 *
 * <p>RN41: el motivo es obligatorio (deja constancia de la razon de la devolucion); la
 * verificacion de que el usuario tenga el permiso especial correspondiente se resuelve en el
 * adaptador REST a partir del contexto de seguridad (JWT), no aqui (ver convenciones-migracion-java.md).
 */
public final class Devolucion extends Entidad {

    private final List<DetalleDevolucion> detalles = new ArrayList<>();

    private final UUID ventaId;
    private final UUID usuarioId;
    private final String motivo;
    private final Instant fecha;

    public Devolucion(UUID ventaId, UUID usuarioId, String motivo, Instant fecha) {
        super();
        this.ventaId = requireNoNulo(ventaId, "La venta devuelta es obligatoria.");
        this.usuarioId = requireNoNulo(usuarioId, "El usuario que registra la devolucion es obligatorio.");
        this.motivo = requireMotivo(motivo);
        this.fecha = requireNoNulo(fecha, "La fecha de la devolucion es obligatoria.");
    }

    /** Constructor de reconstruccion usado por el mapper de persistencia (preserva el id y las lineas existentes). */
    private Devolucion(UUID id, UUID ventaId, UUID usuarioId, String motivo, Instant fecha,
            List<DetalleDevolucion> detallesExistentes) {
        super(id);
        this.ventaId = ventaId;
        this.usuarioId = usuarioId;
        this.motivo = motivo;
        this.fecha = fecha;
        if (detallesExistentes != null) {
            this.detalles.addAll(detallesExistentes);
        }
    }

    /** Usado por el mapper de persistencia para reconstruir el agregado desde su estado guardado. */
    public static Devolucion reconstruir(UUID id, UUID ventaId, UUID usuarioId, String motivo, Instant fecha,
            List<DetalleDevolucion> detalles) {
        return new Devolucion(id, ventaId, usuarioId, motivo, fecha, detalles);
    }

    /** RN42: agrega una linea devuelta (cantidad y monto ya calculados por el caso de uso). */
    public DetalleDevolucion agregarLinea(UUID detalleVentaId, UUID productoId, Cantidad cantidad, Dinero montoDevuelto) {
        DetalleDevolucion detalle = new DetalleDevolucion(getId(), detalleVentaId, productoId, cantidad, montoDevuelto);
        detalles.add(detalle);
        return detalle;
    }

    public Dinero getTotal() {
        return detalles.stream().map(DetalleDevolucion::getMontoDevuelto).reduce(Dinero.CERO, Dinero::sumar);
    }

    private static <T> T requireNoNulo(T valor, String mensaje) {
        if (valor == null) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    /** RN41: la devolucion exige una razon registrada. */
    private static String requireMotivo(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new ValorInvalidoException("El motivo de la devolucion es obligatorio.");
        }
        return motivo;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getMotivo() {
        return motivo;
    }

    public Instant getFecha() {
        return fecha;
    }

    public List<DetalleDevolucion> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }
}
