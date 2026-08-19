package com.posfarmacia.domain.model.identidad;

import com.posfarmacia.domain.model.Entidad;
import java.time.Instant;
import java.util.UUID;

/**
 * Evidencia de una operacion sensible (RF19): anulaciones, notas de credito, cambios
 * de precio, ajustes de stock, validaciones de receta y modificaciones de promocion.
 * Equivalente a PosFarmacia.Domain.Entities.Auditoria.
 */
public final class RegistroAuditoria extends Entidad {

    private final Instant fecha;
    private final UUID usuarioId;
    private final String accion;
    private final String entidad;
    private final String entidadId;
    private final String detalle;
    private final String datosAnteriores;
    private final String datosNuevos;

    public RegistroAuditoria(UUID usuarioId, String accion, String entidad, String entidadId, String detalle,
                              String datosAnteriores, String datosNuevos, Instant ahora) {
        super();
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.detalle = detalle;
        this.datosAnteriores = datosAnteriores;
        this.datosNuevos = datosNuevos;
        this.fecha = ahora;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public RegistroAuditoria(UUID id, Instant fecha, UUID usuarioId, String accion, String entidad, String entidadId,
                              String detalle, String datosAnteriores, String datosNuevos) {
        super(id);
        this.fecha = fecha;
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.detalle = detalle;
        this.datosAnteriores = datosAnteriores;
        this.datosNuevos = datosNuevos;
    }

    public Instant getFecha() {
        return fecha;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getAccion() {
        return accion;
    }

    public String getEntidad() {
        return entidad;
    }

    public String getEntidadId() {
        return entidadId;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getDatosAnteriores() {
        return datosAnteriores;
    }

    public String getDatosNuevos() {
        return datosNuevos;
    }
}
