package com.posfarmacia.adapters.web.security;

import com.posfarmacia.usecases.port.out.identidad.TotpPort;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Locale;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TOTP de 6 digitos cada 30 segundos sobre HMAC-SHA1 (RFC 6238), que es exactamente lo que
 * espera Google Authenticator. Se implementa con javax.crypto en vez de traer una libreria:
 * el algoritmo son dos operaciones de la JDK mas Base32 (RFC 4648), que la JDK no trae.
 */
@Component
public class TotpAdapter implements TotpPort {

    private static final String ALFABETO_BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int BYTES_SECRETO = 20;
    private static final int DIGITOS = 6;
    private static final long PERIODO_SEGUNDOS = 30;
    /** Tolerancia de un periodo hacia atras y hacia adelante por el desfase de reloj del telefono. */
    private static final int VENTANA = 1;

    private final SecureRandom aleatorio = new SecureRandom();
    private final String emisor;

    public TotpAdapter(@Value("${pos-farmacia.mfa.emisor:POS Farmacia}") String emisor) {
        this.emisor = emisor;
    }

    @Override
    public String generarSecreto() {
        byte[] secreto = new byte[BYTES_SECRETO];
        aleatorio.nextBytes(secreto);
        return codificarBase32(secreto);
    }

    @Override
    public boolean validar(String secreto, String codigo) {
        if (secreto == null || codigo == null) {
            return false;
        }
        String limpio = codigo.strip();
        if (limpio.length() != DIGITOS || !limpio.chars().allMatch(Character::isDigit)) {
            return false;
        }

        long periodoActual = System.currentTimeMillis() / 1000 / PERIODO_SEGUNDOS;
        int esperado = Integer.parseInt(limpio);
        byte[] clave = decodificarBase32(secreto);

        for (int desfase = -VENTANA; desfase <= VENTANA; desfase++) {
            if (calcular(clave, periodoActual + desfase) == esperado) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String uriOtpauth(String secreto, String cuenta) {
        String etiqueta = codificar(emisor) + ":" + codificar(cuenta);
        return "otpauth://totp/" + etiqueta
                + "?secret=" + secreto
                + "&issuer=" + codificar(emisor)
                + "&algorithm=SHA1&digits=" + DIGITOS + "&period=" + PERIODO_SEGUNDOS;
    }

    private static String codificar(String valor) {
        return URLEncoder.encode(valor, StandardCharsets.UTF_8);
    }

    /** HOTP (RFC 4226) sobre el contador de tiempo: el nucleo de TOTP. */
    static int calcular(byte[] clave, long contador) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(clave, "HmacSHA1"));
            byte[] hash = mac.doFinal(ByteBuffer.allocate(8).putLong(contador).array());

            int inicio = hash[hash.length - 1] & 0x0F;
            int truncado = ((hash[inicio] & 0x7F) << 24)
                    | ((hash[inicio + 1] & 0xFF) << 16)
                    | ((hash[inicio + 2] & 0xFF) << 8)
                    | (hash[inicio + 3] & 0xFF);
            return truncado % (int) Math.pow(10, DIGITOS);
        } catch (java.security.GeneralSecurityException ex) {
            throw new IllegalStateException("HmacSHA1 no disponible en esta JVM", ex);
        }
    }

    static String codificarBase32(byte[] datos) {
        StringBuilder salida = new StringBuilder();
        int acumulador = 0;
        int bits = 0;
        for (byte dato : datos) {
            acumulador = (acumulador << 8) | (dato & 0xFF);
            bits += 8;
            while (bits >= 5) {
                salida.append(ALFABETO_BASE32.charAt((acumulador >> (bits - 5)) & 0x1F));
                bits -= 5;
            }
        }
        if (bits > 0) {
            salida.append(ALFABETO_BASE32.charAt((acumulador << (5 - bits)) & 0x1F));
        }
        return salida.toString();
    }

    static byte[] decodificarBase32(String texto) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        int acumulador = 0;
        int bits = 0;
        for (char caracter : texto.toUpperCase(Locale.ROOT).toCharArray()) {
            int valor = ALFABETO_BASE32.indexOf(caracter);
            if (valor < 0) {
                continue; // ignora espacios y el relleno '='
            }
            acumulador = (acumulador << 5) | valor;
            bits += 5;
            if (bits >= 8) {
                salida.write((acumulador >> (bits - 8)) & 0xFF);
                bits -= 8;
            }
        }
        return salida.toByteArray();
    }
}
