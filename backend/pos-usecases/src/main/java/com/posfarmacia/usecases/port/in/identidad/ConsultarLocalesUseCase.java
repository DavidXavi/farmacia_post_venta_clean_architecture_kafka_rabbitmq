package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.Local;
import java.util.List;

/** Lista los locales/sedes registrados, usado por el frontend al registrar usuarios y cajas. */
public interface ConsultarLocalesUseCase {

    List<Local> consultar();
}
