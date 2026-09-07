package com.posfarmacia.adapters.web.response.identidad;

/** Datos para dar de alta Google Authenticator: el QR (uriOtpauth) y el secreto para carga manual. */
public record RegistroMfaResponse(String secreto, String uriOtpauth) {
}
