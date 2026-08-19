package com.posfarmacia.domain.service.credito;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.enums.EstadoLineaCredito;
import com.posfarmacia.domain.exception.LineaCreditoInvalidaException;
import com.posfarmacia.domain.model.credito.LineaCredito;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Prueba minima 10 del Word (seccion 11.1): la compra a credito no supera el saldo
 * disponible (RN29-RN30).
 */
class ValidadorLineaCreditoTest {

    private final ValidadorLineaCredito validador = new ValidadorLineaCredito();
    private final LocalDate hoy = LocalDate.of(2026, 7, 27);

    @Test
    void permiteElConsumoCuandoElMontoNoSuperaElSaldoDisponible() {
        LineaCredito lineaCredito = new LineaCredito(UUID.randomUUID(), Dinero.de(500), null, null);

        assertThatCode(() -> validador.validarParaConsumo(lineaCredito, Dinero.de(300), hoy))
                .doesNotThrowAnyException();
    }

    @Test
    void rechazaElConsumoCuandoElMontoSuperaElSaldoDisponible() {
        LineaCredito lineaCredito = new LineaCredito(UUID.randomUUID(), Dinero.de(500), null, null);

        assertThatThrownBy(() -> validador.validarParaConsumo(lineaCredito, Dinero.de(600), hoy))
                .isInstanceOf(LineaCreditoInvalidaException.class);
    }

    @Test
    void rechazaElConsumoCuandoLaLineaNoEstaActiva() {
        LineaCredito lineaCredito = new LineaCredito(UUID.randomUUID(), UUID.randomUUID(), Dinero.de(500),
                Dinero.de(500), null, null, EstadoLineaCredito.BLOQUEADA);

        assertThatThrownBy(() -> validador.validarParaConsumo(lineaCredito, Dinero.de(100), hoy))
                .isInstanceOf(LineaCreditoInvalidaException.class);
    }

    @Test
    void rechazaElConsumoCuandoLaLineaEstaFueraDeVigencia() {
        LineaCredito lineaCredito = new LineaCredito(UUID.randomUUID(), Dinero.de(500),
                hoy.minusDays(30), hoy.minusDays(1));

        assertThatThrownBy(() -> validador.validarParaConsumo(lineaCredito, Dinero.de(100), hoy))
                .isInstanceOf(LineaCreditoInvalidaException.class);
    }

    @Test
    void reduceElSaldoDisponibleAlConsumir() {
        LineaCredito lineaCredito = new LineaCredito(UUID.randomUUID(), Dinero.de(500), null, null);

        lineaCredito.consumir(Dinero.de(200));

        assertThatCode(() -> validador.validarParaConsumo(lineaCredito, Dinero.de(300), hoy))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> validador.validarParaConsumo(lineaCredito, Dinero.de(301), hoy))
                .isInstanceOf(LineaCreditoInvalidaException.class);
    }
}
