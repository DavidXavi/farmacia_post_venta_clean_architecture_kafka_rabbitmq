package com.posfarmacia.usecases.dto.identidad;

/** Datos que necesita el usuario para dar de alta Google Authenticator: el secreto y su QR. */
public record RegistroMfa(String secreto, String uriOtpauth) {
}
