package com.posfarmacia.usecases.usecase.promocion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.posfarmacia.usecases.dto.promocion.SeleccionarPromocionCommand;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.promocion.PromocionRepositoryPort;
import com.posfarmacia.domain.enums.TipoBeneficioPromocion;
import com.posfarmacia.domain.exception.PromocionInvalidaException;
import com.posfarmacia.domain.model.promocion.AplicacionPromocion;
import com.posfarmacia.domain.model.promocion.Promocion;
import com.posfarmacia.domain.service.promocion.EvaluadorPromociones;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeleccionarPromocionUseCaseImplTest {

    private final PromocionRepositoryPort promociones = mock(PromocionRepositoryPort.class);
    private final ClockPort clock = mock(ClockPort.class);
    private final EvaluadorPromociones evaluador = new EvaluadorPromociones();
    private SeleccionarPromocionUseCaseImpl useCase;

    private final UUID productoId = UUID.randomUUID();
    private final UUID ventaId = UUID.randomUUID();
    private final UUID detalleVentaId = UUID.randomUUID();
    private final LocalDate hoy = LocalDate.of(2026, 7, 27);

    @BeforeEach
    void configurar() {
        useCase = new SeleccionarPromocionUseCaseImpl(promociones, clock, evaluador);
        when(clock.hoy()).thenReturn(hoy);
    }

    private Promocion nuevaPromocion(String nombre) {
        Promocion p = Promocion.crear(
                nombre, "desc", TipoBeneficioPromocion.DESCUENTO_PORCENTAJE, BigDecimal.valueOf(10),
                false, new Cantidad(1), new PeriodoVigencia(null, null));
        p.agregarProductoParticipante(productoId);
        return p;
    }

    // Prueba minima 3 (Word 11.1) / RN07: aunque el producto cumpla varias promociones (RN08),
    // el caso de uso solo registra la que el cajero eligio explicitamente para esa linea.
    @Test
    void registraUnaSolaAplicacionAunqueHayaVariasPromocionesAplicables() {
        Promocion elegida = nuevaPromocion("10% descuento");
        Promocion otraAplicable = nuevaPromocion("Lleva 2 y la 3ra gratis");
        when(promociones.buscarPorId(elegida.getId())).thenReturn(java.util.Optional.of(elegida));

        SeleccionarPromocionCommand command = new SeleccionarPromocionCommand(
                elegida.getId(), ventaId, detalleVentaId, productoId, 1,
                BigDecimal.valueOf(100), false, Set.of());

        AplicacionPromocion resultado = useCase.seleccionar(command);

        assertThat(resultado.getPromocionId()).isEqualTo(elegida.getId());
        assertThat(resultado.getPromocionId()).isNotEqualTo(otraAplicable.getId());
        assertThat(resultado.getVentaId()).isEqualTo(ventaId);
        assertThat(resultado.getDetalleVentaId()).isEqualTo(detalleVentaId);
    }

    // Prueba minima 4 (Word 11.1) / RN09: una promocion no puede aplicarse dos veces en el
    // mismo comprobante, aunque cumpla todas las demas condiciones.
    @Test
    void rechazaLaSeleccionSiLaPromocionYaFueAplicadaEnElComprobante() {
        Promocion promocion = nuevaPromocion("10% descuento");
        when(promociones.buscarPorId(promocion.getId())).thenReturn(java.util.Optional.of(promocion));

        SeleccionarPromocionCommand command = new SeleccionarPromocionCommand(
                promocion.getId(), ventaId, detalleVentaId, productoId, 1,
                BigDecimal.valueOf(100), false, Set.of(promocion.getId()));

        assertThatThrownBy(() -> useCase.seleccionar(command))
                .isInstanceOf(PromocionInvalidaException.class);
    }

    // Prueba minima 5 (Word 11.1) / RN10: una promocion que exige cliente registrado no se
    // aplica sin un cliente identificado.
    @Test
    void rechazaLaSeleccionDeUnaPromocionQueExigeClienteSinClienteIdentificado() {
        Promocion promocion = Promocion.crear(
                "Requiere DNI", "desc", TipoBeneficioPromocion.DESCUENTO_PORCENTAJE, BigDecimal.valueOf(10),
                true, new Cantidad(1), new PeriodoVigencia(null, null));
        promocion.agregarProductoParticipante(productoId);
        when(promociones.buscarPorId(promocion.getId())).thenReturn(java.util.Optional.of(promocion));

        SeleccionarPromocionCommand command = new SeleccionarPromocionCommand(
                promocion.getId(), ventaId, detalleVentaId, productoId, 1,
                BigDecimal.valueOf(100), false, Set.of());

        assertThatThrownBy(() -> useCase.seleccionar(command))
                .isInstanceOf(PromocionInvalidaException.class)
                .hasMessageContaining("DNI");
    }
}
