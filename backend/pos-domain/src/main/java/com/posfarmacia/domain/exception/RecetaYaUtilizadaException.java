package com.posfarmacia.domain.exception;

public final class RecetaYaUtilizadaException extends DomainException {
    public RecetaYaUtilizadaException() {
        this("La receta especial retenida ya fue utilizada en otra venta.");
    }

    public RecetaYaUtilizadaException(String mensaje) {
        super(mensaje);
    }
}
