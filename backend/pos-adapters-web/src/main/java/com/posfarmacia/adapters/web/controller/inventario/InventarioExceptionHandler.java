package com.posfarmacia.adapters.web.controller.inventario;

import com.posfarmacia.adapters.web.response.inventario.ErrorResponse;
import com.posfarmacia.domain.exception.DomainException;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.StockInsuficienteException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.exception.inventario.EstadoLoteInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce excepciones de dominio a codigos HTTP (Word, seccion 10), acotado a los controllers de
 * catalogo/inventario para no interferir con el manejo de errores de otros contextos.
 */
@RestControllerAdvice(basePackages = {
        "com.posfarmacia.adapters.web.controller.inventario",
        "com.posfarmacia.adapters.web.controller.catalogo"
})
public class InventarioExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrada(EntidadNoEncontradaException ex) {
        return responder(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", ex);
    }

    @ExceptionHandler(ValorInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejarValorInvalido(ValorInvalidoException ex) {
        return responder(HttpStatus.BAD_REQUEST, "VALOR_INVALIDO", ex);
    }

    @ExceptionHandler({StockInsuficienteException.class, EstadoLoteInvalidoException.class})
    public ResponseEntity<ErrorResponse> manejarConflicto(DomainException ex) {
        return responder(HttpStatus.CONFLICT, "CONFLICTO", ex);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> manejarReglaDeNegocio(DomainException ex) {
        return responder(HttpStatus.UNPROCESSABLE_ENTITY, "REGLA_DE_NEGOCIO_INCUMPLIDA", ex);
    }

    private static ResponseEntity<ErrorResponse> responder(HttpStatus status, String codigo, DomainException ex) {
        return ResponseEntity.status(status).body(new ErrorResponse(codigo, ex.getMessage()));
    }
}
