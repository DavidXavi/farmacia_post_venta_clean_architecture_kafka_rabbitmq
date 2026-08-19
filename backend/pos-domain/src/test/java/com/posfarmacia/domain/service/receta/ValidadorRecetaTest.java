package com.posfarmacia.domain.service.receta;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.exception.RecetaInvalidaException;
import com.posfarmacia.domain.exception.RecetaYaUtilizadaException;
import com.posfarmacia.domain.model.receta.Receta;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.NumeroReceta;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Pruebas minimas 6, 7 y 8 del Word seccion 11.1, mas la prueba de reutilizacion de
 * receta normal (RN16) que sustenta por que RN18/RN20 solo restringen la especial
 * retenida.
 */
class ValidadorRecetaTest {

    private static final LocalDate HOY = LocalDate.of(2026, 7, 27);
    private final UUID productoId = UUID.randomUUID();

    private Receta recetaEspecial(LocalDate fechaVencimiento, TipoReceta tipo) {
        Receta receta = new Receta(
                new NumeroReceta("R-0001"),
                tipo,
                HOY.minusDays(10),
                fechaVencimiento,
                productoId,
                null,
                "Juan Perez",
                "Dr. Gomez",
                "500mg cada 8 horas",
                new Cantidad(20),
                null);
        receta.aprobar();
        return receta;
    }

    @Test
    void medicamentoControladoNoSeVendeSinRecetaAprobada() {
        // RN14: receta pendiente (no aprobada) no ampara la dispensacion.
        Receta receta = new Receta(
                new NumeroReceta("R-0002"),
                TipoReceta.NORMAL,
                HOY.minusDays(1),
                null,
                productoId,
                null,
                "Juan Perez",
                "Dr. Gomez",
                "500mg cada 8 horas",
                new Cantidad(10),
                null);

        assertThatThrownBy(() -> ValidadorReceta.validarParaDispensacion(receta, productoId, new Cantidad(1), HOY))
                .isInstanceOf(RecetaInvalidaException.class);
    }

    @Test
    void recetaEspecialVencidaSeRechaza() {
        // RN17/RN19
        Receta receta = recetaEspecial(HOY.minusDays(1), TipoReceta.ESPECIAL);

        assertThatThrownBy(() -> ValidadorReceta.validarParaDispensacion(receta, productoId, new Cantidad(1), HOY))
                .isInstanceOf(RecetaInvalidaException.class)
                .hasMessageContaining("vencida");
    }

    @Test
    void recetaEspecialRetenidaNoPuedeUtilizarseDosVeces() {
        // RN18/RN20
        Receta receta = recetaEspecial(HOY.plusDays(30), TipoReceta.ESPECIAL_RETENIDA);

        assertThatCode(() -> ValidadorReceta.validarParaDispensacion(receta, productoId, new Cantidad(1), HOY))
                .doesNotThrowAnyException();

        receta.marcarUtilizada();

        assertThatThrownBy(() -> ValidadorReceta.validarParaDispensacion(receta, productoId, new Cantidad(1), HOY))
                .isInstanceOf(RecetaYaUtilizadaException.class);
    }

    @Test
    void recetaNormalNoExigeVigenciaYEsReutilizable() {
        // RN16: no vence y se puede reutilizar en mas de una compra.
        Receta receta = recetaEspecial(null, TipoReceta.NORMAL);

        assertThatCode(() -> ValidadorReceta.validarParaDispensacion(receta, productoId, new Cantidad(1), HOY))
                .doesNotThrowAnyException();

        receta.marcarUtilizada();

        // Una segunda dispensacion, incluso mucho despues, sigue siendo valida.
        assertThatCode(() -> ValidadorReceta.validarParaDispensacion(
                        receta, productoId, new Cantidad(1), HOY.plusYears(5)))
                .doesNotThrowAnyException();
    }

    @Test
    void recetaNoCorrespondeAlMedicamentoSeRechaza() {
        // RN15
        Receta receta = recetaEspecial(HOY.plusDays(10), TipoReceta.ESPECIAL);

        assertThatThrownBy(() -> ValidadorReceta.validarParaDispensacion(
                        receta, UUID.randomUUID(), new Cantidad(1), HOY))
                .isInstanceOf(RecetaInvalidaException.class)
                .hasMessageContaining("no corresponde");
    }

    @Test
    void cantidadSolicitadaMayorALaAutorizadaSeRechaza() {
        // RN15
        Receta receta = recetaEspecial(HOY.plusDays(10), TipoReceta.ESPECIAL);

        assertThatThrownBy(() -> ValidadorReceta.validarParaDispensacion(
                        receta, productoId, new Cantidad(999), HOY))
                .isInstanceOf(RecetaInvalidaException.class)
                .hasMessageContaining("cantidad");
    }
}
