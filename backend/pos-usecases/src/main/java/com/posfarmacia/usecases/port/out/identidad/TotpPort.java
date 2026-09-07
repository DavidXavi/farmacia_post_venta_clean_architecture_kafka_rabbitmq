package com.posfarmacia.usecases.port.out.identidad;

/**
 * Puerto de salida para el segundo factor TOTP (RFC 6238, el que usa Google Authenticator).
 * La aplicacion solo conoce este contrato; el algoritmo concreto vive en el adaptador.
 */
public interface TotpPort {

    /** Secreto compartido nuevo, en Base32, listo para entregar a la app de autenticacion. */
    String generarSecreto();

    boolean validar(String secreto, String codigo);

    /** URI {@code otpauth://} que la app de autenticacion lee desde un codigo QR. */
    String uriOtpauth(String secreto, String cuenta);
}
