package com.posfarmacia.domain.model.incentivo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReglaIncentivoTest {

    private final LocalDate hoy = LocalDate.of(2026, 7, 27);
    private final UUID productoId = UUID.randomUUID();
    private final UUID categoriaId = UUID.randomUUID();
    private final UUID otroProductoId = UUID.randomUUID();
    private final UUID otraCategoriaId = UUID.randomUUID();

    @Test
    void aplicaAlProductoCuandoCoincideElProductoYEstaVigente() {
        ReglaIncentivo regla = new ReglaIncentivo("Incentivo jarabe", productoId, null, Dinero.de(1.0),
                new PeriodoVigencia(null, null));

        assertThat(regla.aplicaA(productoId, otraCategoriaId, hoy)).isTrue();
    }

    @Test
    void aplicaALaCategoriaCuandoCoincideLaCategoriaYEstaVigente() {
        ReglaIncentivo regla = new ReglaIncentivo("Incentivo analgesicos", null, categoriaId, Dinero.de(1.0),
                new PeriodoVigencia(null, null));

        assertThat(regla.aplicaA(otroProductoId, categoriaId, hoy)).isTrue();
    }

    @Test
    void noAplicaCuandoNoCoincideNiProductoNiCategoria() {
        ReglaIncentivo regla = new ReglaIncentivo("Incentivo jarabe", productoId, null, Dinero.de(1.0),
                new PeriodoVigencia(null, null));

        assertThat(regla.aplicaA(otroProductoId, otraCategoriaId, hoy)).isFalse();
    }

    @Test
    void noAplicaCuandoLaReglaEstaFueraDeVigencia() {
        ReglaIncentivo regla = new ReglaIncentivo("Incentivo jarabe", productoId, null, Dinero.de(1.0),
                new PeriodoVigencia(hoy.plusDays(1), hoy.plusDays(10)));

        assertThat(regla.aplicaA(productoId, otraCategoriaId, hoy)).isFalse();
    }

    @Test
    void noAplicaCuandoLaReglaEstaDesactivadaAunSiCoincideYEstaVigente() {
        ReglaIncentivo regla = new ReglaIncentivo("Incentivo jarabe", productoId, null, Dinero.de(1.0),
                new PeriodoVigencia(null, null));
        regla.desactivar();

        assertThat(regla.aplicaA(productoId, otraCategoriaId, hoy)).isFalse();
    }

    @Test
    void noSePuedeCrearUnaReglaSinProductoNiCategoria() {
        assertThatThrownBy(() -> new ReglaIncentivo("Incentivo invalido", null, null, Dinero.de(1.0),
                new PeriodoVigencia(null, null)))
                .isInstanceOf(ValorInvalidoException.class);
    }

    @Test
    void noSePuedeCrearUnaReglaConProductoYCategoriaALaVez() {
        assertThatThrownBy(() -> new ReglaIncentivo("Incentivo invalido", productoId, categoriaId, Dinero.de(1.0),
                new PeriodoVigencia(null, null)))
                .isInstanceOf(ValorInvalidoException.class);
    }

    @Test
    void actualizarCambiaLosDatosDeLaReglaExistente() {
        ReglaIncentivo regla = new ReglaIncentivo("Incentivo jarabe", productoId, null, Dinero.de(1.0),
                new PeriodoVigencia(null, null));

        regla.actualizar("Incentivo jarabe v2", null, categoriaId, Dinero.de(2.5), new PeriodoVigencia(null, null),
                false);

        assertThat(regla.getNombre()).isEqualTo("Incentivo jarabe v2");
        assertThat(regla.getProductoId()).isNull();
        assertThat(regla.getCategoriaId()).isEqualTo(categoriaId);
        assertThat(regla.getMontoPorUnidad()).isEqualTo(Dinero.de(2.5));
        assertThat(regla.isActiva()).isFalse();
    }
}
