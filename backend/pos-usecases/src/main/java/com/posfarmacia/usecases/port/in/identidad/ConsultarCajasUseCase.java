package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.Caja;
import java.util.List;

/** RF02: lista las cajas registradas, usado por el frontend para elegir la caja a operar. */
public interface ConsultarCajasUseCase {

    List<Caja> consultar();
}
