package com.posfarmacia.usecases.dto.catalogo;

import java.util.UUID;

/** Proyeccion de lectura del agregado Categoria para los adaptadores de entrada. */
public record CategoriaResult(UUID id, String nombre) {
}
