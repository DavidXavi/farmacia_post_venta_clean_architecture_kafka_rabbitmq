package com.posfarmacia.usecases.port.out.identidad;

import com.posfarmacia.domain.model.identidad.Local;
import java.util.List;

/**
 * Puerto de lectura del catalogo de locales/sedes (parte del contexto Identidad, ver
 * V1__identidad.sql). El frontend lo usa para poblar el selector de local al registrar un
 * usuario o una caja; no hay alta/baja de locales en este entregable.
 */
public interface LocalRepositoryPort {

    List<Local> listarTodos();
}
