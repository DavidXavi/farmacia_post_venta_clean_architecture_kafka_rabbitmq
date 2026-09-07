package com.posfarmacia.adapters.web.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * El TOTP esta escrito a mano sobre javax.crypto, asi que se contrasta con los vectores de
 * prueba del RFC 6238 (clave ASCII "12345678901234567890", SHA1): si la implementacion se
 * desvia, Google Authenticator dejaria de coincidir y nadie podria iniciar sesion.
 */
class TotpAdapterTest {

    private static final byte[] CLAVE_RFC = "12345678901234567890".getBytes(StandardCharsets.US_ASCII);

    private final TotpAdapter totp = new TotpAdapter("POS Farmacia");

    @Test
    void reproduceLosVectoresDePruebaDelRfc6238() {
        // T = instante / 30; los codigos del RFC son de 8 digitos, aqui se comparan los 6 ultimos.
        assertThat(TotpAdapter.calcular(CLAVE_RFC, 59L / 30)).isEqualTo(287082);
        assertThat(TotpAdapter.calcular(CLAVE_RFC, 1111111109L / 30)).isEqualTo(81804);
        assertThat(TotpAdapter.calcular(CLAVE_RFC, 1234567890L / 30)).isEqualTo(5924);
    }

    @Test
    void base32VaYVuelveSinPerderBytes() {
        assertThat(TotpAdapter.codificarBase32(CLAVE_RFC)).isEqualTo("GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ");
        assertThat(TotpAdapter.decodificarBase32("GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ")).isEqualTo(CLAVE_RFC);
    }

    @Test
    void aceptaElCodigoDelPeriodoActualYRechazaOtro() {
        String secreto = totp.generarSecreto();
        long periodo = System.currentTimeMillis() / 1000 / 30;
        String codigo = String.format("%06d", TotpAdapter.calcular(TotpAdapter.decodificarBase32(secreto), periodo));

        assertThat(totp.validar(secreto, codigo)).isTrue();
        assertThat(totp.validar(secreto, "000000".equals(codigo) ? "111111" : "000000")).isFalse();
        assertThat(totp.validar(secreto, "abcdef")).isFalse();
        assertThat(totp.validar(secreto, null)).isFalse();
    }

    @Test
    void laUriOtpauthLlevaElSecretoYElEmisorQueLeeGoogleAuthenticator() {
        String uri = totp.uriOtpauth("JBSWY3DPEHPK3PXP", "admin");

        assertThat(uri).startsWith("otpauth://totp/POS+Farmacia:admin?secret=JBSWY3DPEHPK3PXP");
        assertThat(uri).contains("issuer=POS+Farmacia").contains("digits=6").contains("period=30");
    }
}
