package com.posfarmacia.domain.model.receta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.enums.EstadoReceta;
import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.exception.RecetaYaUtilizadaException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.NumeroReceta;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RecetaTest {

    private static Receta nueva(TipoReceta tipo, LocalDate fechaVencimiento) {
        return new Receta(
                new NumeroReceta("R-0001"),
                tipo,
                LocalDate.of(2026, 1, 1),
                fechaVencimiento,
                UUID.randomUUID(),
                null,
                "Paciente",
                "Profesional",
                "dosis",
                new Cantidad(5),
                null);
    }

    @Test
    void recetaEspecialSinFechaVencimientoEsInvalida() {
        assertThatThrownBy(() -> nueva(TipoReceta.ESPECIAL, null))
                .isInstanceOf(ValorInvalidoException.class);
    }

    @Test
    void marcarUtilizadaEnRecetaRetenidaQuedaRetenidaEnBotica() {
        Receta receta = nueva(TipoReceta.ESPECIAL_RETENIDA, LocalDate.of(2026, 12, 31));
        receta.aprobar();

        receta.marcarUtilizada();

        assertThat(receta.getEstado()).isEqualTo(EstadoReceta.UTILIZADA);
        assertThat(receta.isRetenidaEnBotica()).isTrue();
    }

    @Test
    void marcarUtilizadaDosVecesEnRecetaRetenidaLanzaExcepcion() {
        Receta receta = nueva(TipoReceta.ESPECIAL_RETENIDA, LocalDate.of(2026, 12, 31));
        receta.aprobar();
        receta.marcarUtilizada();

        assertThatThrownBy(receta::marcarUtilizada).isInstanceOf(RecetaYaUtilizadaException.class);
    }

    @Test
    void marcarUtilizadaEnRecetaNormalNoCambiaEstadoNiRetiene() {
        Receta receta = nueva(TipoReceta.NORMAL, null);
        receta.aprobar();

        receta.marcarUtilizada();
        receta.marcarUtilizada();

        assertThat(receta.getEstado()).isEqualTo(EstadoReceta.APROBADA);
        assertThat(receta.isRetenidaEnBotica()).isFalse();
    }
}
