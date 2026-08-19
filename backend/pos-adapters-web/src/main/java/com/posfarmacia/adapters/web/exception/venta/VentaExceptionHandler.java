package com.posfarmacia.adapters.web.exception.venta;

import com.posfarmacia.domain.exception.StockInsuficienteException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce {@link StockInsuficienteException} a 409 Conflict (Word, seccion 10) para los
 * controllers de Ventas. El resto de excepciones de dominio de este contexto
 * (PagoInsuficienteException, PromocionInvalidaException, RecetaInvalidaException,
 * RecetaYaUtilizadaException, ConvenioNoDisponibleException, LineaCreditoInvalidaException,
 * AnulacionNoPermitidaException -&gt; 422; EntidadNoEncontradaException -&gt; 404;
 * CajaCerradaException -&gt; 409) ya estan cubiertas correctamente por
 * {@code GlobalExceptionHandler}, que trata cualquier {@code DomainException} no listada
 * explicitamente como 422. Sin este handler especifico, StockInsuficienteException caeria en
 * ese generico 422 en vez del 409 que exige el Word para conflictos de stock.
 *
 * <p>{@code @Order(HIGHEST_PRECEDENCE)} asegura que este advice, mas especifico, se evalue antes
 * que el generico {@code GlobalExceptionHandler} para los controllers de este paquete.
 */
@RestControllerAdvice(basePackages = "com.posfarmacia.adapters.web.controller.venta")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class VentaExceptionHandler {

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> manejarStockInsuficiente(StockInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("STOCK_INSUFICIENTE", ex.getMessage()));
    }

    public record ErrorResponse(String codigo, String mensaje) {
    }
}
