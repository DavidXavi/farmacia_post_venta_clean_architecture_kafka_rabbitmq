package com.posfarmacia.domain.service.promocion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.enums.TipoBeneficioPromocion;
import com.posfarmacia.domain.exception.PromocionInvalidaException;
import com.posfarmacia.domain.model.promocion.Promocion;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EvaluadorPromocionesTest {

    private final EvaluadorPromociones evaluador = new EvaluadorPromociones();
    private final LocalDate hoy = LocalDate.of(2026, 7, 27);
    private final UUID productoId = UUID.randomUUID();

    private Promocion promocionLlevaNPagaM() {
        Promocion p = Promocion.crear(
                "Lleva 2 y la 3ra gratis",
                "Promocion por volumen",
                TipoBeneficioPromocion.LLEVA_N_PAGA_M,
                BigDecimal.valueOf(3),
                false,
                new Cantidad(3),
                new PeriodoVigencia(null, null));
        p.agregarProductoParticipante(productoId);
        return p;
    }

    private Promocion promocionDescuentoPorcentaje() {
        Promocion p = Promocion.crear(
                "10% por 1 unidad",
                "Descuento simple",
                TipoBeneficioPromocion.DESCUENTO_PORCENTAJE,
                BigDecimal.valueOf(10),
                false,
                new Cantidad(1),
                new PeriodoVigencia(null, null));
        p.agregarProductoParticipante(productoId);
        return p;
    }

    private Promocion promocionQueExigeCliente() {
        Promocion p = Promocion.crear(
                "5% clientes registrados",
                "Requiere DNI",
                TipoBeneficioPromocion.DESCUENTO_PORCENTAJE,
                BigDecimal.valueOf(5),
                true,
                new Cantidad(1),
                new PeriodoVigencia(null, null));
        p.agregarProductoParticipante(productoId);
        return p;
    }

    // Prueba minima 3 (Word 11.1): solo se aplica una promocion por linea de venta (RN07).
    // El motor puede devolver varias aplicables (RN08); la eleccion de una sola es
    // responsabilidad del cajero/caso de uso, cubierta a nivel de caso de uso.
    @Test
    void unProductoPuedeTenerVariasPromocionesAplicablesALaVez() {
        Promocion llevaNPagaM = promocionLlevaNPagaM();
        Promocion descuento = promocionDescuentoPorcentaje();
        DatosProductoPromocion datos = new DatosProductoPromocion(productoId, new Cantidad(3), false);

        List<Promocion> aplicables = evaluador.obtenerAplicables(List.of(llevaNPagaM, descuento), datos, hoy);

        assertThat(aplicables).containsExactlyInAnyOrder(llevaNPagaM, descuento);
    }

    // Prueba minima 4 (Word 11.1): una promocion solo se aplica una vez por comprobante (RN09).
    @Test
    void validarSeleccionRechazaUnaPromocionYaAplicadaEnElComprobante() {
        Promocion promocion = promocionDescuentoPorcentaje();
        DatosProductoPromocion datos = new DatosProductoPromocion(productoId, new Cantidad(1), false);
        Set<UUID> yaAplicadas = Set.of(promocion.getId());

        assertThatThrownBy(() -> evaluador.validarSeleccion(promocion, datos, hoy, yaAplicadas))
                .isInstanceOf(PromocionInvalidaException.class)
                .hasMessageContaining("ya fue aplicada");
    }

    @Test
    void validarSeleccionAceptaUnaPromocionNoAplicadaAunEnOtroComprobante() {
        Promocion promocion = promocionDescuentoPorcentaje();
        DatosProductoPromocion datos = new DatosProductoPromocion(productoId, new Cantidad(1), false);
        Set<UUID> yaAplicadas = Set.of(UUID.randomUUID());

        assertThatCode(() -> evaluador.validarSeleccion(promocion, datos, hoy, yaAplicadas))
                .doesNotThrowAnyException();
    }

    // Prueba minima 5 (Word 11.1): una promocion que exige cliente registrado no se aplica
    // sin cliente identificado (RN10).
    @Test
    void promocionQueExigeClienteNoEsAplicableSinClienteIdentificado() {
        Promocion promocion = promocionQueExigeCliente();
        DatosProductoPromocion sinCliente = new DatosProductoPromocion(productoId, new Cantidad(1), false);

        List<Promocion> aplicables = evaluador.obtenerAplicables(List.of(promocion), sinCliente, hoy);

        assertThat(aplicables).isEmpty();
    }

    @Test
    void promocionQueExigeClienteEsAplicableConClienteIdentificado() {
        Promocion promocion = promocionQueExigeCliente();
        DatosProductoPromocion conCliente = new DatosProductoPromocion(productoId, new Cantidad(1), true);

        List<Promocion> aplicables = evaluador.obtenerAplicables(List.of(promocion), conCliente, hoy);

        assertThat(aplicables).containsExactly(promocion);
    }

    @Test
    void validarSeleccionRechazaPromocionQueExigeClienteSinClienteIdentificado() {
        Promocion promocion = promocionQueExigeCliente();
        DatosProductoPromocion sinCliente = new DatosProductoPromocion(productoId, new Cantidad(1), false);

        assertThatThrownBy(() -> evaluador.validarSeleccion(promocion, sinCliente, hoy, Set.of()))
                .isInstanceOf(PromocionInvalidaException.class)
                .hasMessageContaining("DNI");
    }
}
