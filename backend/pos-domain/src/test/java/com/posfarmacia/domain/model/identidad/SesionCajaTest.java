package com.posfarmacia.domain.model.identidad;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.enums.EstadoCaja;
import com.posfarmacia.domain.exception.CajaCerradaException;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SesionCajaTest {

    private static final Instant APERTURA = Instant.parse("2026-07-27T08:00:00Z");
    private static final Instant CIERRE = Instant.parse("2026-07-27T20:00:00Z");

    @Test
    void unaSesionRecienAbiertaEstaAbiertaYNoLanzaAlAsegurarAbierta() {
        var sesion = new SesionCaja(UUID.randomUUID(), UUID.randomUUID(), Dinero.de(100), APERTURA);

        assertThat(sesion.estaAbierta()).isTrue();
        assertThat(sesion.getEstado()).isEqualTo(EstadoCaja.ABIERTA);
        assertThatCode(sesion::asegurarAbierta).doesNotThrowAnyException();
    }

    @Test
    void rn01_unaSesionCerradaNoPermiteRegistrarVenta() {
        var sesion = new SesionCaja(UUID.randomUUID(), UUID.randomUUID(), Dinero.de(100), APERTURA);
        sesion.cerrar(Dinero.de(100), Dinero.de(100), "cuadre exacto", CIERRE);

        assertThat(sesion.estaAbierta()).isFalse();
        assertThatThrownBy(sesion::asegurarAbierta).isInstanceOf(CajaCerradaException.class);
    }

    @Test
    void noSePuedeCerrarDosVecesLaMismaSesion() {
        var sesion = new SesionCaja(UUID.randomUUID(), UUID.randomUUID(), Dinero.de(100), APERTURA);
        sesion.cerrar(Dinero.de(100), Dinero.de(100), null, CIERRE);

        assertThatThrownBy(() -> sesion.cerrar(Dinero.de(100), Dinero.de(100), null, CIERRE))
                .isInstanceOf(CajaCerradaException.class);
    }

    @Test
    void calculaLaDiferenciaEntreMontoEsperadoYDeclaradoCuandoFaltaDinero() {
        var sesion = new SesionCaja(UUID.randomUUID(), UUID.randomUUID(), Dinero.de(50), APERTURA);

        sesion.cerrar(Dinero.de(350), Dinero.de(330), "faltaron S/ 20", CIERRE);

        assertThat(sesion.getMontoEsperado()).isEqualTo(Dinero.de(350));
        assertThat(sesion.getMontoDeclarado()).isEqualTo(Dinero.de(330));
        assertThat(sesion.getDiferencia()).isEqualTo(Dinero.de(20));
        assertThat(sesion.getObservacionCierre()).isEqualTo("faltaron S/ 20");
        assertThat(sesion.getFechaCierre()).isEqualTo(CIERRE);
    }

    @Test
    void calculaLaDiferenciaComoValorAbsolutoCuandoSobraDinero() {
        var sesion = new SesionCaja(UUID.randomUUID(), UUID.randomUUID(), Dinero.de(50), APERTURA);

        sesion.cerrar(Dinero.de(300), Dinero.de(315), "sobraron S/ 15", CIERRE);

        assertThat(sesion.getDiferencia()).isEqualTo(Dinero.de(15));
    }
}
