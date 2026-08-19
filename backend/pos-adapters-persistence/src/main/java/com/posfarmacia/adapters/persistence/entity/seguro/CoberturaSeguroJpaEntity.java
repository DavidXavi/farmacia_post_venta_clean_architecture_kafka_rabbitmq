package com.posfarmacia.adapters.persistence.entity.seguro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "coberturas_seguro")
public class CoberturaSeguroJpaEntity {

    @Id
    private UUID id;

    @Column(name = "convenio_id", nullable = false)
    private UUID convenioId;

    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    @Column(name = "porcentaje_cubierto", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeCubierto;

    protected CoberturaSeguroJpaEntity() {
    }

    public CoberturaSeguroJpaEntity(UUID id, UUID convenioId, UUID productoId, BigDecimal porcentajeCubierto) {
        this.id = id;
        this.convenioId = convenioId;
        this.productoId = productoId;
        this.porcentajeCubierto = porcentajeCubierto;
    }

    public UUID getId() {
        return id;
    }

    public UUID getConvenioId() {
        return convenioId;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public BigDecimal getPorcentajeCubierto() {
        return porcentajeCubierto;
    }
}
