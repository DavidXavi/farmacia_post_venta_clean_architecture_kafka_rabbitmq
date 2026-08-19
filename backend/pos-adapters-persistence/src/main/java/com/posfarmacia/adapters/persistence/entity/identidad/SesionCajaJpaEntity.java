package com.posfarmacia.adapters.persistence.entity.identidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sesiones_caja")
public class SesionCajaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "caja_id", nullable = false)
    private UUID cajaId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "fecha_apertura", nullable = false)
    private Instant fechaApertura;

    @Column(name = "monto_inicial", nullable = false)
    private BigDecimal montoInicial;

    @Column(name = "fecha_cierre")
    private Instant fechaCierre;

    @Column(name = "monto_esperado")
    private BigDecimal montoEsperado;

    @Column(name = "monto_declarado")
    private BigDecimal montoDeclarado;

    @Column(name = "diferencia")
    private BigDecimal diferencia;

    @Column(name = "observacion_cierre")
    private String observacionCierre;

    @Column(name = "estado", nullable = false)
    private String estado;

    protected SesionCajaJpaEntity() {
    }

    public SesionCajaJpaEntity(UUID id, UUID cajaId, UUID usuarioId, Instant fechaApertura, BigDecimal montoInicial,
                                Instant fechaCierre, BigDecimal montoEsperado, BigDecimal montoDeclarado,
                                BigDecimal diferencia, String observacionCierre, String estado) {
        this.id = id;
        this.cajaId = cajaId;
        this.usuarioId = usuarioId;
        this.fechaApertura = fechaApertura;
        this.montoInicial = montoInicial;
        this.fechaCierre = fechaCierre;
        this.montoEsperado = montoEsperado;
        this.montoDeclarado = montoDeclarado;
        this.diferencia = diferencia;
        this.observacionCierre = observacionCierre;
        this.estado = estado;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCajaId() {
        return cajaId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public Instant getFechaApertura() {
        return fechaApertura;
    }

    public BigDecimal getMontoInicial() {
        return montoInicial;
    }

    public Instant getFechaCierre() {
        return fechaCierre;
    }

    public BigDecimal getMontoEsperado() {
        return montoEsperado;
    }

    public BigDecimal getMontoDeclarado() {
        return montoDeclarado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public String getObservacionCierre() {
        return observacionCierre;
    }

    public String getEstado() {
        return estado;
    }
}
