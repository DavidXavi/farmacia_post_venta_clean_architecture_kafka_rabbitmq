package com.posfarmacia.adapters.persistence.entity.promocion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

/** Entidad JPA de {@code promocion_condiciones}: producto participante de una promocion (RN12). */
@Entity
@Table(name = "promocion_condiciones")
public class CondicionPromocionJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promocion_id", nullable = false)
    private PromocionJpaEntity promocion;

    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    protected CondicionPromocionJpaEntity() {
        // JPA
    }

    public CondicionPromocionJpaEntity(UUID id, UUID productoId) {
        this.id = id;
        this.productoId = productoId;
    }

    void asignarPromocion(PromocionJpaEntity promocion) {
        this.promocion = promocion;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductoId() {
        return productoId;
    }
}
