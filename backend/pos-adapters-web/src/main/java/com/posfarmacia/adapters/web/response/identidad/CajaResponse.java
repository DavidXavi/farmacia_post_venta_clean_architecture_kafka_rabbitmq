package com.posfarmacia.adapters.web.response.identidad;

import com.posfarmacia.domain.model.identidad.Caja;
import java.util.UUID;

public record CajaResponse(UUID id, String nombre, UUID localId, boolean activa) {

    public static CajaResponse desde(Caja caja) {
        return new CajaResponse(caja.getId(), caja.getNombre(), caja.getLocalId(), caja.isActiva());
    }
}
