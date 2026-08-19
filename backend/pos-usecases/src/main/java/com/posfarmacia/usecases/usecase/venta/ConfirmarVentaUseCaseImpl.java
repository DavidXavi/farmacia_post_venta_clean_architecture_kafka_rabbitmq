package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.credito.ValidarLineaCreditoCommand;
import com.posfarmacia.usecases.dto.seguro.CalcularCopagoCommand;
import com.posfarmacia.usecases.dto.venta.ConfirmarVentaCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.credito.ValidarLineaCreditoUseCase;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaCommand;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaUseCase;
import com.posfarmacia.usecases.port.in.seguro.CalcularCopagoUseCase;
import com.posfarmacia.usecases.dto.mensajeria.TareaEmisionComprobante;
import com.posfarmacia.usecases.dto.mensajeria.VentaConfirmadaEvent;
import com.posfarmacia.usecases.port.in.venta.ConfirmarVentaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.mensajeria.EventoDominioPublisherPort;
import com.posfarmacia.usecases.port.out.mensajeria.TareaAsincronaPublisherPort;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.credito.LineaCreditoRepositoryPort;
import com.posfarmacia.usecases.port.out.credito.MovimientoCreditoRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.SesionCajaRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.MovimientoInventarioRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.seguro.AfiliacionClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.seguro.ConvenioSeguroRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.FormaPagoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.usecases.usecase.inventario.SincronizadorExistencias;
import com.posfarmacia.domain.enums.TipoFormaPago;
import com.posfarmacia.domain.enums.TipoMovimientoCredito;
import com.posfarmacia.domain.enums.TipoMovimientoStock;
import com.posfarmacia.domain.exception.CajaCerradaException;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.RecetaInvalidaException;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.model.credito.LineaCredito;
import com.posfarmacia.domain.model.credito.MovimientoCredito;
import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.model.inventario.Lote;
import com.posfarmacia.domain.model.inventario.MovimientoInventario;
import com.posfarmacia.domain.model.catalogo.Producto;
import com.posfarmacia.domain.model.seguro.AfiliacionCliente;
import com.posfarmacia.domain.model.seguro.ConvenioSeguro;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.FormaPago;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.service.inventario.AsignacionLote;
import com.posfarmacia.domain.service.inventario.AsignadorLotesFEFO;
import com.posfarmacia.domain.service.seguro.ResultadoCopago;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RN01-RN06, el mas importante del contexto de Ventas: dentro de una unica
 * transaccion, asigna lotes por FEFO a las lineas pendientes, confirma el uso de recetas,
 * recalcula el copago del convenio y el consumo de la linea de credito (RN04: nunca se confia en
 * lo que se mostro en pantalla), descuenta el stock fisico y finalmente confirma el agregado.
 *
 * <p>El copago cubierto por el seguro y el saldo financiado por la linea de credito se registran
 * como pagos adicionales (formas de pago {@code COPAGO_SEGURO}/{@code CREDITO_FARMACIA}, RF12),
 * en vez de introducir un concepto nuevo en el agregado Venta: es la misma solucion que ya usa
 * PosFarmacia.Domain.Entities.Venta (.NET), donde {@code Confirmar} solo compara
 * {@code TotalPagado} contra {@code Total} sin ningun ajuste especial por seguro o credito.
 */
public class ConfirmarVentaUseCaseImpl implements ConfirmarVentaUseCase {

    private final VentaRepositoryPort ventas;
    private final SesionCajaRepositoryPort sesionesCaja;
    private final ProductoRepositoryPort productos;
    private final LoteRepositoryPort lotes;
    private final ExistenciaLoteRepositoryPort existencias;
    private final MovimientoInventarioRepositoryPort movimientosInventario;
    private final ValidarRecetaUseCase validarReceta;
    private final ConvenioSeguroRepositoryPort convenios;
    private final AfiliacionClienteRepositoryPort afiliaciones;
    private final CalcularCopagoUseCase calcularCopago;
    private final ClienteRepositoryPort clientes;
    private final ValidarLineaCreditoUseCase validarLineaCredito;
    private final LineaCreditoRepositoryPort lineasCredito;
    private final MovimientoCreditoRepositoryPort movimientosCredito;
    private final FormaPagoRepositoryPort formasPago;
    private final ClockPort clock;
    private final EventoDominioPublisherPort eventos;
    private final TareaAsincronaPublisherPort tareas;
    private final AsignadorLotesFEFO asignadorFEFO = new AsignadorLotesFEFO();

    public ConfirmarVentaUseCaseImpl(VentaRepositoryPort ventas, SesionCajaRepositoryPort sesionesCaja,
            ProductoRepositoryPort productos, LoteRepositoryPort lotes, ExistenciaLoteRepositoryPort existencias,
            MovimientoInventarioRepositoryPort movimientosInventario, ValidarRecetaUseCase validarReceta,
            ConvenioSeguroRepositoryPort convenios, AfiliacionClienteRepositoryPort afiliaciones,
            CalcularCopagoUseCase calcularCopago, ClienteRepositoryPort clientes,
            ValidarLineaCreditoUseCase validarLineaCredito, LineaCreditoRepositoryPort lineasCredito,
            MovimientoCreditoRepositoryPort movimientosCredito, FormaPagoRepositoryPort formasPago, ClockPort clock,
            EventoDominioPublisherPort eventos, TareaAsincronaPublisherPort tareas) {
        this.ventas = ventas;
        this.sesionesCaja = sesionesCaja;
        this.productos = productos;
        this.lotes = lotes;
        this.existencias = existencias;
        this.movimientosInventario = movimientosInventario;
        this.validarReceta = validarReceta;
        this.convenios = convenios;
        this.afiliaciones = afiliaciones;
        this.calcularCopago = calcularCopago;
        this.clientes = clientes;
        this.validarLineaCredito = validarLineaCredito;
        this.lineasCredito = lineasCredito;
        this.movimientosCredito = movimientosCredito;
        this.formasPago = formasPago;
        this.clock = clock;
        this.eventos = eventos;
        this.tareas = tareas;
    }

    @Override
    @Transactional
    public VentaResult confirmar(ConfirmarVentaCommand command) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, command.ventaId());

        // RN01: la sesion de caja bajo la cual se inicio la venta debe seguir abierta.
        SesionCaja sesion = sesionesCaja.buscarSesionActiva(venta.getCajaId())
                .filter(activa -> activa.getId().equals(venta.getSesionCajaId()))
                .orElseThrow(CajaCerradaException::new);
        sesion.asegurarAbierta();

        LocalDate hoy = clock.hoy();
        Instant ahora = clock.ahora();

        for (DetalleVenta detalle : venta.getDetalles()) {
            validarRecetaSiCorresponde(venta, detalle);
            asignarLotesFaltantes(venta, detalle, hoy);
        }

        descontarStockFisico(venta, ahora);
        recalcularYRegistrarCopagoDeSeguro(venta, hoy, ahora);
        recalcularYRegistrarConsumoDeCredito(venta, ahora);

        long correlativo = ventas.siguienteNumeroCorrelativo();
        venta.confirmar(correlativo, command.tipoComprobante(), command.serieComprobante(), ahora);

        Venta guardada = ventas.guardar(venta);
        publicarEventoYEncolarComprobante(guardada, ahora);
        return VentaResultMapper.aResultado(guardada, productos);
    }

    /**
     * RN06: la venta ya quedo confirmada de forma atomica y transaccional arriba; publicar el
     * evento en Kafka y encolar la emision del comprobante en RabbitMQ son efectos posteriores
     * y no bloquean ni condicionan la confirmacion (si el broker de mensajeria no respondiera,
     * la venta ya esta guardada).
     */
    private void publicarEventoYEncolarComprobante(Venta guardada, Instant ahora) {
        String tipoComprobante = guardada.getComprobante().getTipo().name();
        int correlativoComprobante = guardada.getComprobante().getNumero().correlativo();
        String serieComprobante = guardada.getComprobante().getNumero().serie();
        eventos.publicarVentaConfirmada(new VentaConfirmadaEvent(guardada.getId(), correlativoComprobante,
                tipoComprobante, guardada.getTotal().monto(), guardada.getUsuarioId(), ahora));
        tareas.encolarEmisionComprobante(new TareaEmisionComprobante(guardada.getId(), correlativoComprobante,
                tipoComprobante, serieComprobante, guardada.getTotal().monto()));
    }

    private void validarRecetaSiCorresponde(Venta venta, DetalleVenta detalle) {
        Producto producto = productos.buscarPorId(detalle.getProductoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El producto de la linea de venta ya no existe."));

        if (producto.isEsControlado() && detalle.getRecetaId() == null) {
            throw new RecetaInvalidaException(
                    "El producto " + producto.getNombreComercial() + " es controlado y exige una receta valida.");
        }
        if (detalle.getRecetaId() != null) {
            validarReceta.validar(new ValidarRecetaCommand(
                    detalle.getRecetaId(), detalle.getProductoId(), detalle.getCantidad(), venta.getId()));
        }
    }

    private void asignarLotesFaltantes(Venta venta, DetalleVenta detalle, LocalDate hoy) {
        int faltante = detalle.getCantidad().valor() - detalle.getCantidadAsignadaEnLotes().valor();
        if (faltante <= 0) {
            return;
        }
        List<Lote> candidatos = lotes.listar(detalle.getProductoId());
        List<AsignacionLote> asignaciones = asignadorFEFO.asignar(detalle.getProductoId(), new Cantidad(faltante), hoy, candidatos);
        for (AsignacionLote asignacion : asignaciones) {
            venta.asignarLoteADetalle(detalle.getId(), asignacion.loteId(), asignacion.cantidad());
        }
    }

    private void descontarStockFisico(Venta venta, Instant ahora) {
        for (DetalleVenta detalle : venta.getDetalles()) {
            for (var detalleLote : detalle.getLotes()) {
                Lote lote = lotes.buscarPorId(detalleLote.getLoteId())
                        .orElseThrow(() -> new EntidadNoEncontradaException("El lote asignado ya no existe."));
                lote.reservar(detalleLote.getCantidadTomada());
                lotes.guardar(lote);
                movimientosInventario.guardar(new MovimientoInventario(lote.getId(), TipoMovimientoStock.SALIDA,
                        detalleLote.getCantidadTomada(), venta.getUsuarioId(), "Venta " + venta.getId(), ahora));
                SincronizadorExistencias.sincronizar(lote.getProductoId(), lote.getLocalId(), lotes, existencias, clock);
            }
        }
    }

    /** RN04: el copago mostrado en pantalla (AplicarConvenioAVentaUseCaseImpl) nunca se usa; se recalcula aqui. */
    private void recalcularYRegistrarCopagoDeSeguro(Venta venta, LocalDate hoy, Instant ahora) {
        if (venta.getConvenioSeguroId() == null) {
            return;
        }
        ConvenioSeguro convenio = convenios.buscarPorId(venta.getConvenioSeguroId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El convenio de seguro de la venta ya no existe."));
        Optional<AfiliacionCliente> afiliacion = afiliaciones.buscarPorClienteYConvenio(venta.getClienteId(), convenio.getId());
        boolean afiliacionVigente = afiliacion.map(a -> a.estaActivaYVigente(hoy)).orElse(false);

        BigDecimal montoCubiertoTotal = BigDecimal.ZERO;
        for (DetalleVenta detalle : venta.getDetalles()) {
            BigDecimal porcentajeCubierto = convenio.obtenerCoberturaPara(detalle.getProductoId())
                    .map(cobertura -> cobertura.getPorcentajeCubierto().valor())
                    .orElse(null);
            ResultadoCopago resultado = calcularCopago.calcular(new CalcularCopagoCommand(
                    detalle.getSubtotal().monto(), convenio.isActivo(), afiliacionVigente, porcentajeCubierto));
            montoCubiertoTotal = montoCubiertoTotal.add(resultado.montoCubierto().monto());
        }

        if (montoCubiertoTotal.signum() > 0) {
            FormaPago formaPagoSeguro = obtenerFormaPagoPorTipo(TipoFormaPago.COPAGO_SEGURO);
            venta.registrarPago(formaPagoSeguro.getId(), new Dinero(montoCubiertoTotal), null, ahora);
        }
    }

    /** RN29-RN31: revalida la linea de credito con el saldo pendiente real y consume el importe financiado. */
    private void recalcularYRegistrarConsumoDeCredito(Venta venta, Instant ahora) {
        if (venta.getLineaCreditoId() == null) {
            return;
        }
        Dinero pendiente = venta.getTotal().restar(venta.getTotalPagado());
        if (pendiente.monto().signum() <= 0) {
            return;
        }

        Cliente cliente = clientes.buscarPorId(venta.getClienteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente de la venta a credito no existe."));
        LineaCredito lineaCredito = validarLineaCredito.validar(
                new ValidarLineaCreditoCommand(cliente.getDni().valor(), pendiente.monto()));

        lineaCredito.consumir(pendiente);
        lineasCredito.guardar(lineaCredito);
        movimientosCredito.guardar(new MovimientoCredito(
                lineaCredito.getId(), venta.getId(), TipoMovimientoCredito.CONSUMO, pendiente, ahora));

        FormaPago formaPagoCredito = obtenerFormaPagoPorTipo(TipoFormaPago.CREDITO_FARMACIA);
        venta.registrarPago(formaPagoCredito.getId(), pendiente, null, ahora);
    }

    private FormaPago obtenerFormaPagoPorTipo(TipoFormaPago tipo) {
        return formasPago.buscarPorTipo(tipo)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "No existe una forma de pago configurada de tipo " + tipo + "."));
    }
}
