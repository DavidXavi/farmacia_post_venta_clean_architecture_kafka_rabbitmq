package com.posfarmacia.domain.exception.inventario;

import com.posfarmacia.domain.exception.DomainException;

/**
 * Excepcion propia del contexto de inventario (no vive en el kernel compartido, ver convenciones-migracion-java.md).
 * Se lanza cuando se intenta bloquear/retirar un lote que ya se encuentra en un estado terminal (RETIRADO).
 */
public final class EstadoLoteInvalidoException extends DomainException {
    public EstadoLoteInvalidoException(String mensaje) {
        super(mensaje);
    }
}
