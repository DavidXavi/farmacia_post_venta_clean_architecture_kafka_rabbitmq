package com.posfarmacia.domain.model.catalogo;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import java.util.UUID;

/** Categoria de producto (RF03). Agregado simple, sin invariantes adicionales. */
public final class Categoria extends Entidad {

    private String nombre;

    public Categoria(String nombre) {
        super();
        establecerNombre(nombre);
    }

    private Categoria(UUID id, String nombre) {
        super(id);
        establecerNombre(nombre);
    }

    public static Categoria reconstruir(UUID id, String nombre) {
        return new Categoria(id, nombre);
    }

    private void establecerNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValorInvalidoException("El nombre de la categoria no puede ser vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }
}
