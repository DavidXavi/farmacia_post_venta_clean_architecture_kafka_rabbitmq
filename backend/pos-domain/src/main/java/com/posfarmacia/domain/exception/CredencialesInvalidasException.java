package com.posfarmacia.domain.exception;

/**
 * Usuario o contrasena invalidos, o cuenta inactiva/suspendida (RF01).
 * Vive en el paquete compartido porque es un concepto reutilizable por
 * cualquier flujo de autenticacion, no exclusivo del contexto identidad.
 */
public final class CredencialesInvalidasException extends DomainException {
    public CredencialesInvalidasException() {
        this("Usuario o contrasena invalidos.");
    }

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
