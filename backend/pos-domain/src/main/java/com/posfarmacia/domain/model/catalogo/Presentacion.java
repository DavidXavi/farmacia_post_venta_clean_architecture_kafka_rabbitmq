package com.posfarmacia.domain.model.catalogo;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import java.util.UUID;

/** Presentacion y unidad de medida del producto (RF03). */
public final class Presentacion extends Entidad {

    private String nombre;
    private String unidadMedida;

    public Presentacion(String nombre, String unidadMedida) {
        super();
        establecerDatos(nombre, unidadMedida);
    }

    private Presentacion(UUID id, String nombre, String unidadMedida) {
        super(id);
        establecerDatos(nombre, unidadMedida);
    }

    public static Presentacion reconstruir(UUID id, String nombre, String unidadMedida) {
        return new Presentacion(id, nombre, unidadMedida);
    }

    private void establecerDatos(String nombre, String unidadMedida) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValorInvalidoException("El nombre de la presentacion no puede ser vacio.");
        }
        if (unidadMedida == null || unidadMedida.isBlank()) {
            throw new ValorInvalidoException("La unidad de medida no puede ser vacia.");
        }
        this.nombre = nombre.trim();
        this.unidadMedida = unidadMedida.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }
}
