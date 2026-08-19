package com.posfarmacia.domain.service.anulacion;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.posfarmacia.domain.enums.EstadoVenta;
import com.posfarmacia.domain.exception.DevolucionInvalidaException;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

/**
 * Pruebas minimas de dominio para ValidadorDevolucion (Word seccion 11.1): producto controlado no
 * se devuelve, plazo de devolucion y cantidad disponible para devolver.
 */
class ValidadorDevolucionTest {

    private final ValidadorDevolucion validador = new ValidadorDevolucion();

    // Misma zona que usa ValidadorDevolucion.validar() para convertir el Instant de la venta a
    // LocalDate (ZoneId.systemDefault()), evitando que la prueba dependa de la zona horaria del host.
    private static Instant fechaEn(LocalDate fecha) {
        return fecha.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    @Test
    void validar_rechaza_producto_controlado() {
        LocalDate hoy = LocalDate.of(2026, 7, 27);
        Instant fechaVenta = fechaEn(hoy);

        assertThatThrownBy(() -> validador.validar(EstadoVenta.CONFIRMADA, fechaVenta, true,
                new Cantidad(5), Cantidad.CERO, new Cantidad(1), hoy))
                .isInstanceOf(DevolucionInvalidaException.class)
                .hasMessageContaining("controlado");
    }

    @Test
    void validar_rechaza_venta_fuera_del_plazo_de_30_dias() {
        LocalDate hoy = LocalDate.of(2026, 7, 27);
        Instant fechaVenta = fechaEn(hoy.minusDays(31));

        assertThatThrownBy(() -> validador.validar(EstadoVenta.CONFIRMADA, fechaVenta, false,
                new Cantidad(5), Cantidad.CERO, new Cantidad(1), hoy))
                .isInstanceOf(DevolucionInvalidaException.class)
                .hasMessageContaining("plazo");
    }

    @Test
    void validar_rechaza_cantidad_solicitada_mayor_a_lo_disponible() {
        LocalDate hoy = LocalDate.of(2026, 7, 27);
        Instant fechaVenta = fechaEn(hoy);

        assertThatThrownBy(() -> validador.validar(EstadoVenta.CONFIRMADA, fechaVenta, false,
                new Cantidad(5), new Cantidad(3), new Cantidad(3), hoy))
                .isInstanceOf(DevolucionInvalidaException.class)
                .hasMessageContaining("supera");
    }

    @Test
    void validar_rechaza_venta_no_confirmada() {
        LocalDate hoy = LocalDate.of(2026, 7, 27);
        Instant fechaVenta = fechaEn(hoy);

        assertThatThrownBy(() -> validador.validar(EstadoVenta.EN_PROCESO, fechaVenta, false,
                new Cantidad(5), Cantidad.CERO, new Cantidad(1), hoy))
                .isInstanceOf(DevolucionInvalidaException.class);
    }

    @Test
    void validar_acepta_devolucion_valida_dentro_del_plazo_y_cantidad_disponible() {
        LocalDate hoy = LocalDate.of(2026, 7, 27);
        Instant fechaVenta = fechaEn(hoy.minusDays(30));

        assertThatCode(() -> validador.validar(EstadoVenta.CONFIRMADA, fechaVenta, false,
                new Cantidad(5), new Cantidad(2), new Cantidad(3), hoy))
                .doesNotThrowAnyException();
    }
}
