package com.posfarmacia.usecases.usecase.venta;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.posfarmacia.usecases.dto.venta.ConfirmarVentaCommand;
import com.posfarmacia.usecases.port.in.credito.ValidarLineaCreditoUseCase;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaUseCase;
import com.posfarmacia.usecases.port.in.seguro.CalcularCopagoUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.credito.LineaCreditoRepositoryPort;
import com.posfarmacia.usecases.port.out.credito.MovimientoCreditoRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.SesionCajaRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.MovimientoInventarioRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.mensajeria.EventoDominioPublisherPort;
import com.posfarmacia.usecases.port.out.mensajeria.TareaAsincronaPublisherPort;
import com.posfarmacia.usecases.port.out.seguro.AfiliacionClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.seguro.ConvenioSeguroRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.FormaPagoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.enums.TipoComprobante;
import com.posfarmacia.domain.exception.CajaCerradaException;
import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Prueba minima 1 del Word seccion 11.1: no se puede confirmar (vender) si la caja esta cerrada (RN01).
 */
class ConfirmarVentaUseCaseImplTest {

    private final VentaRepositoryPort ventas = mock(VentaRepositoryPort.class);
    private final SesionCajaRepositoryPort sesionesCaja = mock(SesionCajaRepositoryPort.class);
    private final ProductoRepositoryPort productos = mock(ProductoRepositoryPort.class);
    private final LoteRepositoryPort lotes = mock(LoteRepositoryPort.class);
    private final ExistenciaLoteRepositoryPort existencias = mock(ExistenciaLoteRepositoryPort.class);
    private final MovimientoInventarioRepositoryPort movimientosInventario =
            mock(MovimientoInventarioRepositoryPort.class);
    private final ValidarRecetaUseCase validarReceta = mock(ValidarRecetaUseCase.class);
    private final ConvenioSeguroRepositoryPort convenios = mock(ConvenioSeguroRepositoryPort.class);
    private final AfiliacionClienteRepositoryPort afiliaciones = mock(AfiliacionClienteRepositoryPort.class);
    private final CalcularCopagoUseCase calcularCopago = mock(CalcularCopagoUseCase.class);
    private final ClienteRepositoryPort clientes = mock(ClienteRepositoryPort.class);
    private final ValidarLineaCreditoUseCase validarLineaCredito = mock(ValidarLineaCreditoUseCase.class);
    private final LineaCreditoRepositoryPort lineasCredito = mock(LineaCreditoRepositoryPort.class);
    private final MovimientoCreditoRepositoryPort movimientosCredito = mock(MovimientoCreditoRepositoryPort.class);
    private final FormaPagoRepositoryPort formasPago = mock(FormaPagoRepositoryPort.class);
    private final ClockPort clock = mock(ClockPort.class);
    private final EventoDominioPublisherPort eventos = mock(EventoDominioPublisherPort.class);
    private final TareaAsincronaPublisherPort tareas = mock(TareaAsincronaPublisherPort.class);

    private ConfirmarVentaUseCaseImpl useCase;
    private UUID ventaId;
    private UUID cajaId;

    @BeforeEach
    void setUp() {
        useCase = new ConfirmarVentaUseCaseImpl(ventas, sesionesCaja, productos, lotes, existencias,
                movimientosInventario, validarReceta, convenios, afiliaciones, calcularCopago, clientes,
                validarLineaCredito, lineasCredito, movimientosCredito, formasPago, clock, eventos, tareas);

        ventaId = UUID.randomUUID();
        cajaId = UUID.randomUUID();
        UUID sesionCajaId = UUID.randomUUID();
        Venta venta = new Venta(cajaId, sesionCajaId, UUID.randomUUID(), null, Instant.now());
        when(ventas.buscarPorId(ventaId)).thenReturn(Optional.of(venta));
    }

    @Test
    void confirmar_lanza_caja_cerrada_si_no_hay_sesion_de_caja_activa() {
        when(sesionesCaja.buscarSesionActiva(cajaId)).thenReturn(Optional.empty());

        var command = new ConfirmarVentaCommand(ventaId, TipoComprobante.BOLETA, "B001");

        assertThatThrownBy(() -> useCase.confirmar(command)).isInstanceOf(CajaCerradaException.class);
    }

    @Test
    void confirmar_lanza_caja_cerrada_si_la_sesion_activa_ya_no_es_la_misma_de_la_venta() {
        SesionCaja otraSesion = new SesionCaja(cajaId, UUID.randomUUID(), Dinero.CERO, Instant.now());

        when(sesionesCaja.buscarSesionActiva(cajaId)).thenReturn(Optional.of(otraSesion));

        var command = new ConfirmarVentaCommand(ventaId, TipoComprobante.BOLETA, "B001");

        assertThatThrownBy(() -> useCase.confirmar(command)).isInstanceOf(CajaCerradaException.class);
    }
}
