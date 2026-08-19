package com.posfarmacia.adapters.web.response.receta;

import java.util.UUID;

public record ValidarRecetaResponse(
        UUID recetaId,
        String numero,
        String tipo,
        String estado,
        boolean retenidaEnBotica,
        boolean usoRegistrado) {
}
