package com.posfarmacia.usecases.port.in.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;
import java.util.List;

/** Puerto de entrada usado por el adaptador REST para mostrar el feed en el navegador. */
public interface ConsultarActividadRecienteUseCase {

    List<ActividadReciente> listar(int limite);
}
