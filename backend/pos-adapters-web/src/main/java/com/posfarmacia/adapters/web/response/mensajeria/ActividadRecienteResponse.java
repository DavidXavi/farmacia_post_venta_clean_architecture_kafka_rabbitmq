package com.posfarmacia.adapters.web.response.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;
import java.time.Instant;

public record ActividadRecienteResponse(String origen, String tipo, String descripcion, Instant ocurridoEn) {

    public static ActividadRecienteResponse desde(ActividadReciente actividad) {
        return new ActividadRecienteResponse(actividad.origen(), actividad.tipo(), actividad.descripcion(),
                actividad.ocurridoEn());
    }
}
