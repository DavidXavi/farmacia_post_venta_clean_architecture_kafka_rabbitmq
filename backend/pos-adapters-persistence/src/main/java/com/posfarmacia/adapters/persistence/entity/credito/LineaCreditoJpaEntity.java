package com.posfarmacia.adapters.persistence.entity.credito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lineas_credito")
public class LineaCreditoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "monto_autorizado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoAutorizado;

    @Column(name = "saldo_disponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(name = "vigencia_inicio")
    private LocalDate vigenciaInicio;

    @Column(name = "vigencia_fin")
    private LocalDate vigenciaFin;

    @Column(nullable = false, length = 20)
    private String estado;

    protected LineaCreditoJpaEntity() {
    }

    public LineaCreditoJpaEntity(UUID id, UUID clienteId, BigDecimal montoAutorizado, BigDecimal saldoDisponible,
                                  LocalDate vigenciaInicio, LocalDate vigenciaFin, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.montoAutorizado = montoAutorizado;
        this.saldoDisponible = saldoDisponible;
        this.vigenciaInicio = vigenciaInicio;
        this.vigenciaFin = vigenciaFin;
        this.estado = estado;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public BigDecimal getMontoAutorizado() {
        return montoAutorizado;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public LocalDate getVigenciaInicio() {
        return vigenciaInicio;
    }

    public LocalDate getVigenciaFin() {
        return vigenciaFin;
    }

    public String getEstado() {
        return estado;
    }
}
