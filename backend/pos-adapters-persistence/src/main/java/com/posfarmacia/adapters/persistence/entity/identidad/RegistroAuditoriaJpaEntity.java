package com.posfarmacia.adapters.persistence.entity.identidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auditoria_operaciones")
public class RegistroAuditoriaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "accion", nullable = false)
    private String accion;

    @Column(name = "entidad", nullable = false)
    private String entidad;

    @Column(name = "entidad_id", nullable = false)
    private String entidadId;

    @Column(name = "detalle", nullable = false)
    private String detalle;

    @Column(name = "datos_anteriores")
    private String datosAnteriores;

    @Column(name = "datos_nuevos")
    private String datosNuevos;

    protected RegistroAuditoriaJpaEntity() {
    }

    public RegistroAuditoriaJpaEntity(UUID id, Instant fecha, UUID usuarioId, String accion, String entidad,
                                       String entidadId, String detalle, String datosAnteriores, String datosNuevos) {
        this.id = id;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.detalle = detalle;
        this.datosAnteriores = datosAnteriores;
        this.datosNuevos = datosNuevos;
    }

    public UUID getId() {
        return id;
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
