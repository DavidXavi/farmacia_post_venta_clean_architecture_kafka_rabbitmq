package com.posfarmacia.adapters.web.controller.receta;

import com.posfarmacia.adapters.web.request.receta.RegistrarRecetaRequest;
import com.posfarmacia.adapters.web.request.receta.RevisarRecetaRequest;
import com.posfarmacia.adapters.web.request.receta.ValidarRecetaRequest;
import com.posfarmacia.adapters.web.response.receta.RecetaResponse;
import com.posfarmacia.adapters.web.response.receta.ValidarRecetaResponse;
import com.posfarmacia.usecases.port.in.receta.RegistrarRecetaCommand;
import com.posfarmacia.usecases.port.in.receta.RegistrarRecetaUseCase;
import com.posfarmacia.usecases.port.in.receta.RevisarRecetaCommand;
import com.posfarmacia.usecases.port.in.receta.RevisarRecetaUseCase;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaCommand;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaResultado;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaUseCase;
import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.RecetaInvalidaException;
import com.posfarmacia.domain.exception.RecetaYaUtilizadaException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.valueobject.Cantidad;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada REST del contexto de recetas (RF07). Solo traduce HTTP <-> caso
 * de uso; ninguna regla de negocio vive aqui (RN14-RN20 se resuelven en
 * {@code pos-domain}/{@code pos-usecases}).
 *
 * <p>Rutas alineadas al contrato real que consume el frontend (no reescrito, ver
 * {@code arquitectura_2_t2/frontend/src/pages/RecetasPage.jsx} y el controller .NET
 * equivalente {@code PosFarmacia.Presentation.Controllers.RecetasController}):
 * sin prefijo {@code /v1}, {@code POST /api/recetas} registra una receta nueva y
 * {@code POST /api/recetas/validaciones} es la revision clinica (aprobar/rechazar) del
 * quimico farmaceutico. Se conserva ademas {@code POST /api/recetas/validar} para RF07
 * (validar que la receta ampare una dispensacion concreta), que no tiene un caller
 * directo en el frontend pero ya existia en este backend.
 *
 * <p>Los manejadores de excepcion estan acotados a este controller (no un
 * {@code @ControllerAdvice} global) para no interferir con otros contextos que se
 * implementan en paralelo.
 */
@RestController
@RequestMapping("/api/recetas")
public class RecetasController {

    private static final Pattern PRIMER_NUMERO = Pattern.compile("\\d+");

    private final RegistrarRecetaUseCase registrarRecetaUseCase;
    private final RevisarRecetaUseCase revisarRecetaUseCase;
    private final ValidarRecetaUseCase validarRecetaUseCase;

    public RecetasController(
            RegistrarRecetaUseCase registrarRecetaUseCase,
            RevisarRecetaUseCase revisarRecetaUseCase,
            ValidarRecetaUseCase validarRecetaUseCase) {
        this.registrarRecetaUseCase = Objects.requireNonNull(registrarRecetaUseCase);
        this.revisarRecetaUseCase = Objects.requireNonNull(revisarRecetaUseCase);
        this.validarRecetaUseCase = Objects.requireNonNull(validarRecetaUseCase);
    }

    @PostMapping
    public ResponseEntity<RecetaResponse> registrar(@Valid @RequestBody RegistrarRecetaRequest request) {
        RegistrarRecetaCommand command = new RegistrarRecetaCommand(
                request.numero(),
                parseTipoReceta(request.tipo()),
                request.fechaEmision(),
                request.fechaVencimiento(),
                request.productoId(),
                request.clienteId(),
                request.datosPaciente(),
                request.datosProfesional(),
                request.dosisYCantidadAutorizada(),
                extraerCantidadAutorizada(request.dosisYCantidadAutorizada()),
                request.archivoRespaldoUrl());

        RecetaResponse response = RecetaResponse.de(registrarRecetaUseCase.registrar(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/validaciones")
    public ResponseEntity<RecetaResponse> validaciones(@Valid @RequestBody RevisarRecetaRequest request) {
        RevisarRecetaCommand command = new RevisarRecetaCommand(
                request.recetaId(), request.usuarioValidadorId(), request.aprobar(), request.observaciones());

        return ResponseEntity.ok(RecetaResponse.de(revisarRecetaUseCase.revisar(command)));
    }

    @PostMapping("/validar")
    public ResponseEntity<ValidarRecetaResponse> validar(@Valid @RequestBody ValidarRecetaRequest request) {
        ValidarRecetaCommand command = new ValidarRecetaCommand(
                request.recetaId(), request.productoId(), new Cantidad(request.cantidad()), request.ventaId());

        ValidarRecetaResultado resultado = validarRecetaUseCase.validar(command);

        return ResponseEntity.ok(new ValidarRecetaResponse(
                resultado.recetaId(),
                resultado.numero(),
                resultado.tipo().name(),
                resultado.estado().name(),
                resultado.retenidaEnBotica(),
                resultado.usoRegistrado()));
    }

    /**
     * El frontend (no reescrito) envia el tipo de receta en PascalCase sin guion bajo
     * (p.ej. {@code EspecialRetenida}), mientras el enum Java usa {@code ESPECIAL_RETENIDA}.
     * Esta traduccion de formato no es una regla de negocio.
     */
    private static TipoReceta parseTipoReceta(String valor) {
        return switch (valor) {
            case "Normal" -> TipoReceta.NORMAL;
            case "Especial" -> TipoReceta.ESPECIAL;
            case "EspecialRetenida" -> TipoReceta.ESPECIAL_RETENIDA;
            default -> throw new ValorInvalidoException(
                    "El tipo de receta '" + valor + "' no es valido.");
        };
    }

    /**
     * El frontend (no reescrito) combina dosis y cantidad autorizada en un unico campo
     * de texto libre ({@code dosisYCantidadAutorizada}), mientras el dominio Java separa
     * la cantidad como valor numerico para poder validar RN15 (cantidad solicitada vs.
     * autorizada). Se extrae el primer numero presente en el texto; si no hay ninguno,
     * se asume 1. Esta es una traduccion de formato de entrada, no una regla de negocio.
     */
    private static Cantidad extraerCantidadAutorizada(String dosisYCantidadAutorizada) {
        Matcher matcher = PRIMER_NUMERO.matcher(
                dosisYCantidadAutorizada == null ? "" : dosisYCantidadAutorizada);
        int valor = matcher.find() ? Integer.parseInt(matcher.group()) : 1;
        return new Cantidad(Math.max(valor, 1));
    }

    @ExceptionHandler(ValorInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String manejarValorInvalido(ValorInvalidoException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String manejarNoEncontrada(EntidadNoEncontradaException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({RecetaInvalidaException.class, RecetaYaUtilizadaException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String manejarRecetaInvalida(RuntimeException ex) {
        return ex.getMessage();
    }
}
