package com.posfarmacia.adapters.web.controller.promocion;

import com.posfarmacia.adapters.web.request.promocion.ActualizarPromocionRequest;
import com.posfarmacia.adapters.web.request.promocion.CrearPromocionRequest;
import com.posfarmacia.adapters.web.request.promocion.EvaluarPromocionesRequest;
import com.posfarmacia.adapters.web.request.promocion.SeleccionarPromocionRequest;
import com.posfarmacia.adapters.web.response.promocion.PromocionAplicableResponse;
import com.posfarmacia.adapters.web.response.promocion.PromocionErrorResponse;
import com.posfarmacia.adapters.web.response.promocion.PromocionResponse;
import com.posfarmacia.adapters.web.response.promocion.PromocionSeleccionadaResponse;
import com.posfarmacia.usecases.dto.promocion.ActualizarPromocionCommand;
import com.posfarmacia.usecases.dto.promocion.CrearPromocionCommand;
import com.posfarmacia.usecases.dto.promocion.EvaluarPromocionesQuery;
import com.posfarmacia.usecases.dto.promocion.SeleccionarPromocionCommand;
import com.posfarmacia.usecases.port.in.promocion.EvaluarPromocionesUseCase;
import com.posfarmacia.usecases.port.in.promocion.GestionarPromocionUseCase;
import com.posfarmacia.usecases.port.in.promocion.SeleccionarPromocionUseCase;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.PromocionInvalidaException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada primario (RF06): traduce HTTP a los casos de uso de promociones. No
 * calcula ninguna regla de negocio, solo mapea request/response y delega. Ruta base sin
 * prefijo {@code /v1} ({@code /api/promociones}) para igualar el contrato real que consume
 * {@code frontend/src/pages/PromocionesPage.jsx} (CRUD) y {@code VentaPage.jsx} (que aplica
 * promociones durante una venta a traves del contexto Ventas, {@code VentasController}).
 * {@code evaluar}/{@code seleccionar} se conservan aqui como sub-rutas por si algun otro
 * consumidor necesita invocar esos casos de uso via HTTP directamente.
 */
@RestController
@RequestMapping("/api/promociones")
public class PromocionesController {

    private final EvaluarPromocionesUseCase evaluarPromociones;
    private final SeleccionarPromocionUseCase seleccionarPromocion;
    private final GestionarPromocionUseCase gestionarPromocion;

    public PromocionesController(EvaluarPromocionesUseCase evaluarPromociones,
            SeleccionarPromocionUseCase seleccionarPromocion, GestionarPromocionUseCase gestionarPromocion) {
        this.evaluarPromociones = evaluarPromociones;
        this.seleccionarPromocion = seleccionarPromocion;
        this.gestionarPromocion = gestionarPromocion;
    }

    /** GET /api/promociones: listado completo (activas e inactivas) para el CRUD de administracion. */
    @GetMapping
    public List<PromocionResponse> listar() {
        return gestionarPromocion.listar().stream().map(PromocionResponse::desde).toList();
    }

    /** POST /api/promociones: alta de una promocion (solo Administrador, ver SecurityConfig). */
    @PostMapping
    public ResponseEntity<PromocionResponse> crear(@Valid @RequestBody CrearPromocionRequest request) {
        CrearPromocionCommand command = new CrearPromocionCommand(
                request.nombre(),
                request.descripcion(),
                TipoBeneficioPromocionTexto.aEnum(request.tipoBeneficio()),
                request.valorBeneficio(),
                request.requiereCliente(),
                request.cantidadMinima(),
                request.fechaInicio(),
                request.fechaFin(),
                request.productosParticipantes());
        PromocionResponse creada = PromocionResponse.desde(gestionarPromocion.crear(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /** PUT /api/promociones/{id}: edicion de una promocion existente (solo Administrador). */
    @PutMapping("/{id}")
    public PromocionResponse actualizar(@PathVariable UUID id, @Valid @RequestBody ActualizarPromocionRequest request) {
        ActualizarPromocionCommand command = new ActualizarPromocionCommand(
                request.nombre(),
                request.descripcion(),
                TipoBeneficioPromocionTexto.aEnum(request.tipoBeneficio()),
                request.valorBeneficio(),
                request.requiereCliente(),
                request.cantidadMinima(),
                request.fechaInicio(),
                request.fechaFin(),
                request.productosParticipantes());
        return PromocionResponse.desde(gestionarPromocion.actualizar(id, command));
    }

    /** PATCH /api/promociones/{id}/desactivar (solo Administrador). */
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        gestionarPromocion.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    /** POST /api/promociones/evaluar (RF06). */
    @PostMapping("/evaluar")
    public ResponseEntity<List<PromocionAplicableResponse>> evaluar(@Valid @RequestBody EvaluarPromocionesRequest request) {
        EvaluarPromocionesQuery query = new EvaluarPromocionesQuery(
                request.productoId(), request.cantidad(), request.clienteIdentificado());
        List<PromocionAplicableResponse> aplicables = evaluarPromociones.evaluar(query).stream()
                .map(PromocionAplicableResponse::desde)
                .toList();
        return ResponseEntity.ok(aplicables);
    }

    /**
     * Registra la promocion que el cajero eligio para una linea (RN07-RN12). La seleccion real
     * durante una venta la expone el contexto de Ventas
     * ({@code PATCH /api/ventas/{ventaId}/detalles/{detalleId}/promocion} en
     * {@code VentasController}, que invoca su propio {@code SeleccionarPromocionVentaUseCase});
     * esta sub-ruta se conserva para que otro consumidor pueda invocar directamente el caso de
     * uso de este contexto via HTTP.
     */
    @PostMapping("/seleccionar")
    public ResponseEntity<PromocionSeleccionadaResponse> seleccionar(@Valid @RequestBody SeleccionarPromocionRequest request) {
        SeleccionarPromocionCommand command = new SeleccionarPromocionCommand(
                request.promocionId(),
                request.ventaId(),
                request.detalleVentaId(),
                request.productoId(),
                request.cantidad(),
                request.precioUnitario(),
                request.clienteIdentificado(),
                request.promocionesYaAplicadasEnComprobante() == null ? Set.of() : request.promocionesYaAplicadasEnComprobante());
        PromocionSeleccionadaResponse response = PromocionSeleccionadaResponse.desde(seleccionarPromocion.seleccionar(command));
        return ResponseEntity.ok(response);
    }

    /** RN09 / RN10 / RN11 / RN12 incumplidas -> 422 (Word, seccion 10). */
    @ExceptionHandler(PromocionInvalidaException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public PromocionErrorResponse manejarPromocionInvalida(PromocionInvalidaException ex) {
        return new PromocionErrorResponse("PROMOCION_INVALIDA", ex.getMessage());
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public PromocionErrorResponse manejarEntidadNoEncontrada(EntidadNoEncontradaException ex) {
        return new PromocionErrorResponse("ENTIDAD_NO_ENCONTRADA", ex.getMessage());
    }
}
