package com.posfarmacia.domain.exception;

/**
 * La cuenta tiene MFA activo (RF01) y el codigo de Google Authenticator falta o es invalido.
 * Se distingue de {@link CredencialesInvalidasException} para que el cliente sepa que debe
 * pedir el codigo en vez de volver a pedir usuario y contrasena.
 */
public final class MfaRequeridaException extends DomainException {
    public MfaRequeridaException() {
        super("Se requiere el codigo de verificacion de Google Authenticator.");
    }
}
