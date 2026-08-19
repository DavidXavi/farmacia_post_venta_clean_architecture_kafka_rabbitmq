package com.posfarmacia.adapters.persistence.entity.catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "categorias")
public class CategoriaJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    protected CategoriaJpaEntity() {
    }

    public CategoriaJpaEntity(UUID id, String nombre) {
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
