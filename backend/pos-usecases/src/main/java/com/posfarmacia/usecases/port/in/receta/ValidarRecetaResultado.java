package com.posfarmacia.usecases.port.in.receta;

import com.posfarmacia.domain.enums.EstadoReceta;
import com.posfarmacia.domain.enums.TipoReceta;
import java.util.UUID;

/** Salida de {@link ValidarRecetaUseCase}. */
public record ValidarRecetaResultado(
        UUID recetaId,
        String numero,
        TipoReceta tipo,
        EstadoReceta estado,
        boolean retenidaEnBotica,
        boolean usoRegistrado) {
}
