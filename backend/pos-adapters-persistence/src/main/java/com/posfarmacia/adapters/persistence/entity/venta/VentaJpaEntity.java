package com.posfarmacia.adapters.persistence.entity.venta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ventas")
public class VentaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "caja_id", nullable = false)
    private UUID cajaId;

    @Column(name = "sesion_caja_id", nullable = false)
    private UUID sesionCajaId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "cliente_id")
    private UUID clienteId;

    @Column(name = "convenio_seguro_id")
    private UUID convenioSeguroId;

    @Column(name = "linea_credito_id")
    private UUID lineaCreditoId;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "numero_correlativo")
    private Long numeroCorrelativo;

    protected VentaJpaEntity() {
    }

    public VentaJpaEntity(UUID id, UUID cajaId, UUID sesionCajaId, UUID usuarioId, UUID clienteId,
            UUID convenioSeguroId, UUID lineaCreditoId, Instant fecha, String estado, Long numeroCorrelativo) {
        this.id = id;
        this.cajaId = cajaId;
        this.sesionCajaId = sesionCajaId;
        this.usuarioId = usuarioId;
        this.clienteId = clienteId;
        this.convenioSeguroId = convenioSeguroId;
        this.lineaCreditoId = lineaCreditoId;
        this.fecha = fecha;
        this.estado = estado;
        this.numeroCorrelativo = numeroCorrelativo;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCajaId() {
        return cajaId;
    }

    public UUID getSesionCajaId() {
        return sesionCajaId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public UUID getConvenioSeguroId() {
        return convenioSeguroId;
    }

    public UUID getLineaCreditoId() {
        return lineaCreditoId;
    }

    public Instant getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public Long getNumeroCorrelativo() {
        return numeroCorrelativo;
    }
}
