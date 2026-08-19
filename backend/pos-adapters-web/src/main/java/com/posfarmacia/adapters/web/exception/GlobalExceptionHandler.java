package com.posfarmacia.adapters.web.exception;

import com.posfarmacia.domain.exception.CajaCerradaException;
import com.posfarmacia.domain.exception.CredencialesInvalidasException;
import com.posfarmacia.domain.exception.DomainException;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Traduce excepciones de dominio a codigos HTTP (Word, seccion 10). Transversal a todos los
 * controllers del modulo, no exclusivo del contexto identidad.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String CABECERA_CORRELACION = "X-Correlation-Id";

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> manejar(CredencialesInvalidasException ex, HttpServletRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, "CREDENCIALES_INVALIDAS", ex, request);
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> manejar(EntidadNoEncontradaException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "ENTIDAD_NO_ENCONTRADA", ex, request);
    }

    @ExceptionHandler(CajaCerradaException.class)
    public ResponseEntity<ErrorResponse> manejar(CajaCerradaException ex, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, "CONFLICTO_SESION_CAJA", ex, request);
    }

    @ExceptionHandler(ValorInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejar(ValorInvalidoException ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, "VALOR_INVALIDO", ex, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejar(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .orElse("Datos de entrada invalidos.");
        return construirConMensaje(HttpStatus.BAD_REQUEST, "FORMATO_INVALIDO", mensaje, request);
    }

    /** Falta un @RequestParam obligatorio (ej. GET /api/inventarios sin localId): error del cliente, no del servidor. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> manejar(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return construirConMensaje(HttpStatus.BAD_REQUEST, "PARAMETRO_FALTANTE",
                "El parametro obligatorio '" + ex.getParameterName() + "' no fue enviado.", request);
    }

    /** Un parametro (ej. un UUID de path/query) no tiene el formato esperado: error del cliente, no del servidor. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejar(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return construirConMensaje(HttpStatus.BAD_REQUEST, "PARAMETRO_INVALIDO",
                "El parametro '" + ex.getName() + "' no tiene un formato valido.", request);
    }

    /** El cuerpo de la peticion no se pudo parsear (JSON malformado, enum invalido, etc.). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejar(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return construirConMensaje(HttpStatus.BAD_REQUEST, "CUERPO_INVALIDO",
                "El cuerpo de la peticion no tiene un formato valido.", request);
    }

    /** Cualquier otra regla de negocio incumplida no cubierta arriba (Word: 422 Unprocessable Entity). */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> manejar(DomainException ex, HttpServletRequest request) {
        return construir(HttpStatus.UNPROCESSABLE_ENTITY, "REGLA_DE_NEGOCIO_INCUMPLIDA", ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejar(Exception ex, HttpServletRequest request) {
        return construirConMensaje(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO",
                "Ocurrio un error inesperado. Contacte al administrador.", request);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String codigo, DomainException ex,
                                                      HttpServletRequest request) {
        return construirConMensaje(status, codigo, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> construirConMensaje(HttpStatus status, String codigo, String mensaje,
                                                               HttpServletRequest request) {
        String correlationId = obtenerOGenerarCorrelationId(request);
        return ResponseEntity.status(status)
                .header(CABECERA_CORRELACION, correlationId)
                .body(ErrorResponse.de(codigo, mensaje, correlationId));
    }

    private String obtenerOGenerarCorrelationId(HttpServletRequest request) {
        String existente = request.getHeader(CABECERA_CORRELACION);
        return existente != null && !existente.isBlank() ? existente : UUID.randomUUID().toString();
    }
}
