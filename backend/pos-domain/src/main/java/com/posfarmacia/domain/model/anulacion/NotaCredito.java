package com.posfarmacia.domain.model.anulacion;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.Instant;
import java.util.UUID;

/**
 * Nota de credito emitida sobre una venta confirmada de un dia anterior (RN39/RN40, RF16): la venta
 * no se anula ni se borra (RN05), queda vinculada a este documento por su monto total. Referencias a
 * Venta y Comprobante solo por UUID (regla de oro de migracion). Equivalente a
 * PosFarmacia.Domain.Entities.NotaCredito (.NET).
 *
 * <p>RN41: el motivo es obligatorio; el permiso especial (EMITIR_NOTA_CREDITO) se verifica en el
 * adaptador REST a partir del contexto de seguridad (JWT), no aqui.
 */
public final class NotaCredito extends Entidad {

    private final UUID ventaId;
    private final UUID comprobanteId;
    private final UUID usuarioId;
    private final String motivo;
    private final Dinero montoTotal;
    private final Instant fecha;

    public NotaCredito(UUID ventaId, UUID comprobanteId, UUID usuarioId, String motivo, Dinero montoTotal, Instant fecha) {
        super();
        this.ventaId = requireNoNulo(ventaId, "La venta de la nota de credito es obligatoria.");
        this.comprobanteId = requireNoNulo(comprobanteId, "El comprobante original es obligatorio.");
        this.usuarioId = requireNoNulo(usuarioId, "El usuario que emite la nota de credito es obligatorio.");
        this.motivo = requireMotivo(motivo);
        this.montoTotal = requireNoNulo(montoTotal, "El monto total de la nota de credito es obligatorio.");
        this.fecha = requireNoNulo(fecha, "La fecha de la nota de credito es obligatoria.");
    }

    /** Constructor de reconstruccion usado por el mapper de persistencia (preserva el id existente). */
    private NotaCredito(UUID id, UUID ventaId, UUID comprobanteId, UUID usuarioId, String motivo, Dinero montoTotal,
            Instant fecha) {
        super(id);
        this.ventaId = ventaId;
        this.comprobanteId = comprobanteId;
        this.usuarioId = usuarioId;
        this.motivo = motivo;
        this.montoTotal = montoTotal;
        this.fecha = fecha;
    }

    /** Usado por el mapper de persistencia para reconstruir el agregado desde su estado guardado. */
    public static NotaCredito reconstruir(UUID id, UUID ventaId, UUID comprobanteId, UUID usuarioId, String motivo,
            Dinero montoTotal, Instant fecha) {
        return new NotaCredito(id, ventaId, comprobanteId, usuarioId, motivo, montoTotal, fecha);
    }

    private static <T> T requireNoNulo(T valor, String mensaje) {
        if (valor == null) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    private static String requireMotivo(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new ValorInvalidoException("El motivo de la nota de credito es obligatorio.");
        }
        return motivo;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getComprobanteId() {
        return comprobanteId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getMotivo() {
        return motivo;
    }

    public Dinero getMontoTotal() {
        return montoTotal;
    }

    public Instant getFecha() {
        return fecha;
    }
}
