package com.posfarmacia.usecases.dto.catalogo;

import java.util.UUID;

/** Proyeccion de lectura del agregado Laboratorio para los adaptadores de entrada. */
public record LaboratorioResult(UUID id, String nombre) {
}
