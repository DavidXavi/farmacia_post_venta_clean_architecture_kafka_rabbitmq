package com.posfarmacia.infrastructure.configuration.venta;

import com.posfarmacia.usecases.port.in.credito.ValidarLineaCreditoUseCase;
import com.posfarmacia.usecases.port.in.promocion.EvaluarPromocionesUseCase;
import com.posfarmacia.usecases.port.in.promocion.SeleccionarPromocionUseCase;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaUseCase;
import com.posfarmacia.usecases.port.in.seguro.CalcularCopagoUseCase;
import com.posfarmacia.usecases.port.in.venta.AgregarProductoAVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.AnularVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.AplicarConvenioAVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.ConfirmarVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.ConsultarFormasPagoUseCase;
import com.posfarmacia.usecases.port.in.venta.ConsultarVentasDiariasUseCase;
import com.posfarmacia.usecases.port.in.venta.EvaluarPromocionesVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.IdentificarClienteEnVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.IniciarVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.ObtenerVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.RegistrarPagoUseCase;
import com.posfarmacia.usecases.port.in.venta.SeleccionarPromocionVentaUseCase;
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
import com.posfarmacia.usecases.usecase.venta.AgregarProductoAVentaUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.AnularVentaUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.AplicarConvenioAVentaUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.ConfirmarVentaUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.ConsultarFormasPagoUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.ConsultarVentasDiariasUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.EvaluarPromocionesVentaUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.IdentificarClienteEnVentaUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.IniciarVentaUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.ObtenerVentaUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.RegistrarPagoUseCaseImpl;
import com.posfarmacia.usecases.usecase.venta.SeleccionarPromocionVentaUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cablea los casos de uso del contexto Ventas/Pagos, el orquestador de los 5 contextos ya
 * migrados (identidad/caja, catalogo/inventario, promociones, recetas, clientes/seguros/credito).
 * Los casos de uso son POJOs de pos-usecases (sin anotaciones de Spring, para que el nucleo
 * sea probable sin framework); pos-infrastructure es el unico modulo que conoce las implementaciones
 * concretas y las conecta mediante @Bean (mismo patron que IdentidadUseCaseConfiguration/RecetaUseCaseConfig).
 */
@Configuration
public class VentaUseCaseConfiguration {

    @Bean
    public IniciarVentaUseCase iniciarVentaUseCase(VentaRepositoryPort ventas, SesionCajaRepositoryPort sesionesCaja,
            ClienteRepositoryPort clientes, ProductoRepositoryPort productos, ClockPort clock) {
        return new IniciarVentaUseCaseImpl(ventas, sesionesCaja, clientes, productos, clock);
    }

    @Bean
    public AgregarProductoAVentaUseCase agregarProductoAVentaUseCase(VentaRepositoryPort ventas,
            ProductoRepositoryPort productos) {
        return new AgregarProductoAVentaUseCaseImpl(ventas, productos);
    }

    @Bean
    public EvaluarPromocionesVentaUseCase evaluarPromocionesVentaUseCase(VentaRepositoryPort ventas,
            EvaluarPromocionesUseCase evaluarPromociones) {
        return new EvaluarPromocionesVentaUseCaseImpl(ventas, evaluarPromociones);
    }

    @Bean
    public SeleccionarPromocionVentaUseCase seleccionarPromocionVentaUseCase(VentaRepositoryPort ventas,
            SeleccionarPromocionUseCase seleccionarPromocion, ProductoRepositoryPort productos) {
        return new SeleccionarPromocionVentaUseCaseImpl(ventas, seleccionarPromocion, productos);
    }

    @Bean
    public IdentificarClienteEnVentaUseCase identificarClienteEnVentaUseCase(VentaRepositoryPort ventas,
            ClienteRepositoryPort clientes, ProductoRepositoryPort productos) {
        return new IdentificarClienteEnVentaUseCaseImpl(ventas, clientes, productos);
    }

    @Bean
    public AplicarConvenioAVentaUseCase aplicarConvenioAVentaUseCase(VentaRepositoryPort ventas,
            ConvenioSeguroRepositoryPort convenios, AfiliacionClienteRepositoryPort afiliaciones,
            CalcularCopagoUseCase calcularCopago, ClockPort clock) {
        return new AplicarConvenioAVentaUseCaseImpl(ventas, convenios, afiliaciones, calcularCopago, clock);
    }

    @Bean
    public RegistrarPagoUseCase registrarPagoUseCase(VentaRepositoryPort ventas, FormaPagoRepositoryPort formasPago,
            ProductoRepositoryPort productos, ClockPort clock) {
        return new RegistrarPagoUseCaseImpl(ventas, formasPago, productos, clock);
    }

    @Bean
    public ConfirmarVentaUseCase confirmarVentaUseCase(VentaRepositoryPort ventas,
            SesionCajaRepositoryPort sesionesCaja, ProductoRepositoryPort productos, LoteRepositoryPort lotes,
            ExistenciaLoteRepositoryPort existencias, MovimientoInventarioRepositoryPort movimientosInventario,
            ValidarRecetaUseCase validarReceta, ConvenioSeguroRepositoryPort convenios,
            AfiliacionClienteRepositoryPort afiliaciones, CalcularCopagoUseCase calcularCopago,
            ClienteRepositoryPort clientes, ValidarLineaCreditoUseCase validarLineaCredito,
            LineaCreditoRepositoryPort lineasCredito, MovimientoCreditoRepositoryPort movimientosCredito,
            FormaPagoRepositoryPort formasPago, ClockPort clock, EventoDominioPublisherPort eventos,
            TareaAsincronaPublisherPort tareas) {
        return new ConfirmarVentaUseCaseImpl(ventas, sesionesCaja, productos, lotes, existencias,
                movimientosInventario, validarReceta, convenios, afiliaciones, calcularCopago, clientes,
                validarLineaCredito, lineasCredito, movimientosCredito, formasPago, clock, eventos, tareas);
    }

    @Bean
    public AnularVentaUseCase anularVentaUseCase(VentaRepositoryPort ventas, LoteRepositoryPort lotes,
            ExistenciaLoteRepositoryPort existencias, MovimientoInventarioRepositoryPort movimientosInventario,
            ProductoRepositoryPort productos, ClockPort clock) {
        return new AnularVentaUseCaseImpl(ventas, lotes, existencias, movimientosInventario, productos, clock);
    }

    @Bean
    public ObtenerVentaUseCase obtenerVentaUseCase(VentaRepositoryPort ventas, ProductoRepositoryPort productos) {
        return new ObtenerVentaUseCaseImpl(ventas, productos);
    }

    @Bean
    public ConsultarVentasDiariasUseCase consultarVentasDiariasUseCase(VentaRepositoryPort ventas,
            ProductoRepositoryPort productos) {
        return new ConsultarVentasDiariasUseCaseImpl(ventas, productos);
    }

    @Bean
    public ConsultarFormasPagoUseCase consultarFormasPagoUseCase(FormaPagoRepositoryPort formasPago) {
        return new ConsultarFormasPagoUseCaseImpl(formasPago);
    }
}
