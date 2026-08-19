package com.posfarmacia.domain.service.seguro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.exception.ConvenioNoDisponibleException;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Porcentaje;
import org.junit.jupiter.api.Test;

/**
 * Prueba minima 9 del Word (seccion 11.1): el copago solo se calcula cuando el convenio
 * y la cobertura/afiliacion estan activos (RN22-RN25).
 */
class CalculadorCopagoTest {

    private final CalculadorCopago calculadorCopago = new CalculadorCopago();

    @Test
    void calculaMontoCubiertoYCopagoCuandoConvenioYAfiliacionEstanActivos() {
        Dinero montoLinea = Dinero.de(100);
        Porcentaje porcentajeCubierto = Porcentaje.de(80);

        ResultadoCopago resultado = calculadorCopago.calcular(montoLinea, true, true, porcentajeCubierto);

        assertThat(resultado.montoCubierto()).isEqualTo(Dinero.de(80));
        assertThat(resultado.copago()).isEqualTo(Dinero.de(20));
    }

    @Test
    void elClientePagaElTotalCuandoElProductoNoEstaCubiertoPorElConvenio() {
        Dinero montoLinea = Dinero.de(50);

        ResultadoCopago resultado = calculadorCopago.calcular(montoLinea, true, true, null);

        assertThat(resultado.montoCubierto()).isEqualTo(Dinero.CERO);
        assertThat(resultado.copago()).isEqualTo(montoLinea);
    }

    @Test
    void noCalculaCopagoSiElConvenioNoEstaActivo() {
        assertThatThrownBy(() -> calculadorCopago.calcular(Dinero.de(100), false, true, Porcentaje.de(50)))
                .isInstanceOf(ConvenioNoDisponibleException.class);
    }

    @Test
    void noCalculaCopagoSiLaAfiliacionNoEstaActivaOVigente() {
        assertThatThrownBy(() -> calculadorCopago.calcular(Dinero.de(100), true, false, Porcentaje.de(50)))
                .isInstanceOf(ConvenioNoDisponibleException.class);
    }
}
