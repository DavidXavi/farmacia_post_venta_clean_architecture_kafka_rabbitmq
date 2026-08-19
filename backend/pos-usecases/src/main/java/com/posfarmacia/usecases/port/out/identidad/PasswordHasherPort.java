package com.posfarmacia.usecases.port.out.identidad;

/**
 * Puerto de salida para el hash/verificacion de contrasenas. La aplicacion solo conoce
 * este contrato; el adaptador REST lo implementa con BCrypt (Spring Security Crypto).
 */
public interface PasswordHasherPort {

    String hash(String password);

    boolean verificar(String password, String hash);
}
