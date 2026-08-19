package com.posfarmacia.adapters.persistence.entity.catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "laboratorios")
public class LaboratorioJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    protected LaboratorioJpaEntity() {
    }

    public LaboratorioJpaEntity(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
