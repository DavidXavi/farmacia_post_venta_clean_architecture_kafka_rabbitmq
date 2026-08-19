package com.posfarmacia.adapters.web.security;

import com.posfarmacia.usecases.port.out.identidad.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/** Implementa PasswordHasherPort con BCrypt (Spring Security Crypto); la aplicacion solo conoce el puerto. */
@Component
public class BCryptPasswordHasherAdapter implements PasswordHasherPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean verificar(String password, String hash) {
        return hash != null && encoder.matches(password, hash);
    }
}
