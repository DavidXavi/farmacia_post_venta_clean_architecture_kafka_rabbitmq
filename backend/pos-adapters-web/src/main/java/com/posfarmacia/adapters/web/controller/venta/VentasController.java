package com.posfarmacia.adapters.web.controller.venta;

import com.posfarmacia.adapters.web.request.venta.AgregarProductoRequest;
import com.posfarmacia.adapters.web.request.venta.AnularVentaRequest;
import com.posfarmacia.adapters.web.request.venta.AplicarConvenioRequest;
import com.posfarmacia.adapters.web.request.venta.ConfirmarVentaRequest;
import com.posfarmacia.adapters.web.request.venta.IdentificarClienteRequest;
import com.posfarmacia.adapters.web.request.venta.IniciarVentaRequest;
import com.posfarmacia.adapters.web.request.venta.RegistrarPagoRequest;
import com.posfarmacia.adapters.web.request.venta.SeleccionarPromocionRequest;
import com.posfarmacia.adapters.web.response.venta.CopagoResponse;
import com.posfarmacia.adapters.web.response.venta.PromocionAplicableResponse;
import com.posfarmacia.adapters.web.response.venta.VentaResponse;
import com.posfarmacia.usecases.dto.venta.AgregarProductoCommand;
import com.posfarmacia.usecases.dto.venta.AplicarConvenioCommand;
import com.posfarmacia.usecases.dto.venta.ConfirmarVentaCommand;
import com.posfarmacia.usecases.dto.venta.ConsultarVentasDiariasQuery;
import com.posfarmacia.usecases.dto.venta.IdentificarClienteCommand;
import com.posfarmacia.usecases.dto.venta.IniciarVentaCommand;
import com.posfarmacia.usecases.dto.venta.RegistrarPagoCommand;
import com.posfarmacia.usecases.dto.venta.SeleccionarPromocionVentaCommand;
import com.posfarmacia.usecases.port.in.venta.AgregarProductoAVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.AnularVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.AplicarConvenioAVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.ConfirmarVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.ConsultarVentasDiariasUseCase;
import com.posfarmacia.usecases.port.in.venta.EvaluarPromocionesVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.IdentificarClienteEnVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.IniciarVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.ObtenerVentaUseCase;
import com.posfarmacia.usecases.port.in.venta.RegistrarPagoUseCase;
import com.posfarmacia.usecases.port.in.venta.SeleccionarPromocionVentaUseCase;
import com.posfarmacia.domain.enums.TipoComprobante;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF05-RF13: solo traduce HTTP &lt;-&gt; casos de uso, no calcula reglas de
 * negocio. Rutas EXACTAS a las que ya consume `frontend/src/pages/VentaPage.jsx` (heredadas de
 * `PosFarmacia.Presentation.Controllers.VentasController` en arquitectura_2_t2): base
 * {@code api/ventas}, sin prefijo {@code /v1} (ver convenciones-migracion-java.md).
 */
@RestController
@RequestMapping("/api/ventas")
public class VentasController {

    private final IniciarVentaUseCase iniciarVenta;
    private final AgregarProductoAVentaUseCase agregarProducto;
    private final EvaluarPromocionesVentaUseCase evaluarPromociones;
    private final SeleccionarPromocionVentaUseCase seleccionarPromocion;
    private final IdentificarClienteEnVentaUseCase identificarCliente;
    private final AplicarConvenioAVentaUseCase aplicarConvenio;
    private final RegistrarPagoUseCase registrarPago;
    private final ConfirmarVentaUseCase confirmarVenta;
    private final AnularVentaUseCase anularVenta;
    private final ObtenerVentaUseCase obtenerVenta;
    private final ConsultarVentasDiariasUseCase consultarVentasDiarias;

    public VentasController(IniciarVentaUseCase iniciarVenta, AgregarProductoAVentaUseCase agregarProducto,
            EvaluarPromocionesVentaUseCase evaluarPromociones, SeleccionarPromocionVentaUseCase seleccionarPromocion,
            IdentificarClienteEnVentaUseCase identificarCliente, AplicarConvenioAVentaUseCase aplicarConvenio,
            RegistrarPagoUseCase registrarPago, ConfirmarVentaUseCase confirmarVenta, AnularVentaUseCase anularVenta,
            ObtenerVentaUseCase obtenerVenta, ConsultarVentasDiariasUseCase consultarVentasDiarias) {
        this.iniciarVenta = iniciarVenta;
        this.agregarProducto = agregarProducto;
        this.evaluarPromociones = evaluarPromociones;
        this.seleccionarPromocion = seleccionarPromocion;
        this.identificarCliente = identificarCliente;
        this.aplicarConvenio = aplicarConvenio;
        this.registrarPago = registrarPago;
        this.confirmarVenta = confirmarVenta;
        this.anularVenta = anularVenta;
        this.obtenerVenta = obtenerVenta;
        this.consultarVentasDiarias = consultarVentasDiarias;
    }

    @PostMapping
    public ResponseEntity<VentaResponse> iniciar(@Valid @RequestBody IniciarVentaRequest request) {
        var command = new IniciarVentaCommand(request.cajaId(), request.sesionCajaId(), request.usuarioId(),
                request.clienteDni());
        var resultado = iniciarVenta.iniciar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(VentaResponse.desde(resultado));
    }

    @GetMapping
    public List<VentaResponse> listar(
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) UUID cajaId,
            @RequestParam(required = false) UUID usuarioId,
            @RequestParam(required = false) UUID clienteId) {
        var query = new ConsultarVentasDiariasQuery(fecha, cajaId, usuarioId, clienteId);
        return consultarVentasDiarias.consultar(query).stream().map(VentaResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public VentaResponse obtenerPorId(@PathVariable UUID id) {
        return VentaResponse.desde(obtenerVenta.obtener(id));
    }

    @PostMapping("/{id}/detalles")
    public VentaResponse agregarProducto(@PathVariable UUID id, @Valid @RequestBody AgregarProductoRequest request) {
        var command = new AgregarProductoCommand(request.productoId(), request.cantidad(), request.recetaId());
        return VentaResponse.desde(agregarProducto.agregar(id, command));
    }

    @GetMapping("/{id}/promociones-disponibles")
    public List<PromocionAplicableResponse> promocionesDisponibles(@PathVariable UUID id,
            @RequestParam UUID detalleVentaId) {
        return evaluarPromociones.evaluar(id, detalleVentaId).stream().map(PromocionAplicableResponse::desde).toList();
    }

    @PatchMapping("/{ventaId}/detalles/{detalleId}/promocion")
    public VentaResponse seleccionarPromocion(@PathVariable UUID ventaId, @PathVariable UUID detalleId,
            @Valid @RequestBody SeleccionarPromocionRequest request) {
        var command = new SeleccionarPromocionVentaCommand(ventaId, detalleId, request.promocionId());
        return VentaResponse.desde(seleccionarPromocion.seleccionar(command));
    }

    @PostMapping("/{id}/cliente")
    public VentaResponse identificarCliente(@PathVariable UUID id, @Valid @RequestBody IdentificarClienteRequest request) {
        var command = new IdentificarClienteCommand(id, request.dni());
        return VentaResponse.desde(identificarCliente.identificar(command));
    }

    @PostMapping("/{id}/convenio")
    public CopagoResponse aplicarConvenio(@PathVariable UUID id, @Valid @RequestBody AplicarConvenioRequest request) {
        var command = new AplicarConvenioCommand(id, request.convenioId());
        return CopagoResponse.desde(aplicarConvenio.aplicar(command));
    }

    @PostMapping("/{id}/pagos")
    public VentaResponse registrarPago(@PathVariable UUID id, @Valid @RequestBody RegistrarPagoRequest request) {
        var command = new RegistrarPagoCommand(id, request.formaPagoId(), request.monto(), request.codigoAutorizacion());
        return VentaResponse.desde(registrarPago.registrar(command));
    }

    @PostMapping("/{id}/confirmar")
    public VentaResponse confirmar(@PathVariable UUID id, @Valid @RequestBody ConfirmarVentaRequest request) {
        var command = new ConfirmarVentaCommand(id, parseTipoComprobante(request.tipoComprobante()),
                request.serieComprobante());
        return VentaResponse.desde(confirmarVenta.confirmar(command));
    }

    @PostMapping("/{id}/anular")
    public VentaResponse anular(@PathVariable UUID id, @RequestBody(required = false) AnularVentaRequest request) {
        return VentaResponse.desde(anularVenta.anular(id));
    }

    private static TipoComprobante parseTipoComprobante(String valor) {
        try {
            return TipoComprobante.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ValorInvalidoException("El tipo de comprobante '" + valor + "' no es valido.");
        }
    }
}
