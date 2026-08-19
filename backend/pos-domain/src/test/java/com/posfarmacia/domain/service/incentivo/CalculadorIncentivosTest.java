package com.posfarmacia.domain.service.incentivo;

import static org.assertj.core.api.Assertions.assertThat;

import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CalculadorIncentivosTest {

    private final CalculadorIncentivos calculador = new CalculadorIncentivos();

    @Test
    void calculaElMontoComoMontoPorUnidadMultiplicadoPorLaCantidadVendida() {
        ReglaIncentivo regla = new ReglaIncentivo("Incentivo jarabe", UUID.randomUUID(), null,
                Dinero.de(1.5), new PeriodoVigencia(null, null));

        Dinero monto = calculador.calcular(regla, new Cantidad(4));

        assertThat(monto).isEqualTo(new Dinero(BigDecimal.valueOf(6.00).setScale(2)));
    }

    @Test
    void unaCantidadVendidaDeCeroNoGeneraIncentivo() {
        ReglaIncentivo regla = new ReglaIncentivo("Incentivo jarabe", UUID.randomUUID(), null,
                Dinero.de(2.0), new PeriodoVigencia(null, null));

        Dinero monto = calculador.calcular(regla, Cantidad.CERO);

        assertThat(monto).isEqualTo(Dinero.CERO);
    }
}
