package com.posfarmacia.usecases.port.out.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;
import java.util.List;

/** Puerto de salida que guarda y expone el feed de actividad reciente (ver {@link ActividadReciente}). */
public interface ActividadRecienteRepositoryPort {

    void registrar(ActividadReciente actividad);

    List<ActividadReciente> listarRecientes(int limite);
}
