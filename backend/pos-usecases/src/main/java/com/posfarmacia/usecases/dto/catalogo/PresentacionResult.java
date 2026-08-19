package com.posfarmacia.usecases.dto.catalogo;

import java.util.UUID;

/** Proyeccion de lectura del agregado Presentacion para los adaptadores de entrada. */
public record PresentacionResult(UUID id, String nombre, String unidadMedida) {
}
