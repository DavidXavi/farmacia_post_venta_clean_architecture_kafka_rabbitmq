package com.posfarmacia.adapters.web.exception.cliente;

import com.posfarmacia.domain.exception.ConsultaSeguroCentralFallidaException;
import com.posfarmacia.domain.exception.ConvenioNoDisponibleException;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.LineaCreditoInvalidaException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de dominio de Clientes/Seguros/Credito a codigos HTTP
 * (Word, seccion 10). Scoped solo a los controllers de este contexto para no chocar
 * con manejadores de otros contextos migrados en paralelo.
 */
@RestControllerAdvice(basePackages = {
        "com.posfarmacia.adapters.web.controller.cliente",
        "com.posfarmacia.adapters.web.controller.seguro",
        "com.posfarmacia.adapters.web.controller.credito"
})
public class ClienteSeguroCreditoExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(EntidadNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("ENTIDAD_NO_ENCONTRADA", ex.getMessage()));
    }

    @ExceptionHandler({ConvenioNoDisponibleException.class, LineaCreditoInvalidaException.class})
    public ResponseEntity<ErrorResponse> manejarReglaDeNegocio(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse("REGLA_DE_NEGOCIO_INCUMPLIDA", ex.getMessage()));
    }

    @ExceptionHandler(ConsultaSeguroCentralFallidaException.class)
    public ResponseEntity<ErrorResponse> manejarFallaConsultaCentral(ConsultaSeguroCentralFallidaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("CONSULTA_CENTRAL_FALLIDA", ex.getMessage()));
    }

    @ExceptionHandler(ValorInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejarValorInvalido(ValorInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("VALOR_INVALIDO", ex.getMessage()));
    }

    public record ErrorResponse(String codigo, String mensaje) {
    }
}
