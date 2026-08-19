package com.posfarmacia.adapters.persistence.entity.catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "presentaciones")
public class PresentacionJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "unidad_medida", nullable = false)
    private String unidadMedida;

    protected PresentacionJpaEntity() {
    }

    public PresentacionJpaEntity(UUID id, String nombre, String unidadMedida) {
        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }
}
