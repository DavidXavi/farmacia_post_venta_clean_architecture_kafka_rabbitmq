package com.posfarmacia.domain.model.catalogo;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import java.util.UUID;

/** Laboratorio o fabricante del producto (RF03). Agregado simple, sin invariantes adicionales. */
public final class Laboratorio extends Entidad {

    private String nombre;

    public Laboratorio(String nombre) {
        super();
        establecerNombre(nombre);
    }

    private Laboratorio(UUID id, String nombre) {
        super(id);
        establecerNombre(nombre);
    }

    public static Laboratorio reconstruir(UUID id, String nombre) {
        return new Laboratorio(id, nombre);
    }

    private void establecerNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValorInvalidoException("El nombre del laboratorio no puede ser vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }
}
