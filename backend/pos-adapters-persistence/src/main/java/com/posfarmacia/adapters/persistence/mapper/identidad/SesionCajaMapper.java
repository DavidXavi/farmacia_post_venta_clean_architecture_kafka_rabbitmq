package com.posfarmacia.adapters.persistence.mapper.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.SesionCajaJpaEntity;
import com.posfarmacia.domain.enums.EstadoCaja;
import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.valueobject.Dinero;
import java.math.BigDecimal;

public final class SesionCajaMapper {

    private SesionCajaMapper() {
    }

    public static SesionCaja aDominio(SesionCajaJpaEntity entity) {
        return new SesionCaja(
                entity.getId(),
                entity.getCajaId(),
                entity.getUsuarioId(),
                entity.getFechaApertura(),
                new Dinero(entity.getMontoInicial()),
                entity.getFechaCierre(),
                aDinero(entity.getMontoEsperado()),
                aDinero(entity.getMontoDeclarado()),
                aDinero(entity.getDiferencia()),
                entity.getObservacionCierre(),
                EstadoCaja.valueOf(entity.getEstado()));
    }

    public static SesionCajaJpaEntity aEntidad(SesionCaja sesion) {
        return new SesionCajaJpaEntity(
                sesion.getId(),
                sesion.getCajaId(),
                sesion.getUsuarioId(),
                sesion.getFechaApertura(),
                sesion.getMontoInicial().monto(),
                sesion.getFechaCierre(),
                aMonto(sesion.getMontoEsperado()),
                aMonto(sesion.getMontoDeclarado()),
                aMonto(sesion.getDiferencia()),
                sesion.getObservacionCierre(),
                sesion.getEstado().name());
    }

    private static Dinero aDinero(BigDecimal monto) {
        return monto == null ? null : new Dinero(monto);
    }

    private static BigDecimal aMonto(Dinero dinero) {
        return dinero == null ? null : dinero.monto();
    }
}
