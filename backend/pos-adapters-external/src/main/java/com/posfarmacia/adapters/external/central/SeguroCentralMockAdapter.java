package com.posfarmacia.adapters.external.central;

import com.posfarmacia.usecases.port.out.seguro.CoberturaCentralResult;
import com.posfarmacia.usecases.port.out.seguro.SeguroCentralPort;
import com.posfarmacia.domain.exception.ConsultaSeguroCentralFallidaException;
import com.posfarmacia.domain.valueobject.CodigoAutorizacionSeguro;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * MOCK/STUB de la central/servicio de seguros (Word, seccion 12: "servicios simulados/mocks"
 * para ambientes de prueba). Pendiente de reemplazo por la integracion real con la central
 * cuando este disponible; basta con sustituir esta clase por otra implementacion de
 * {@link SeguroCentralPort}, el resto del nucleo no se ve afectado.
 *
 * Para poder demostrar/probar manualmente las reglas RN22-RN27 sin una central real, este
 * mock reconoce un puñado de UUID "sentinela" documentados abajo; cualquier otra combinacion
 * de cliente/convenio/producto se resuelve como una afiliacion activa y vigente con cobertura
 * por defecto.
 */
@Component
public class SeguroCentralMockAdapter implements SeguroCentralPort {

    /** Simula una falla de comunicacion con la central (RN27): usar este convenioId para probar el caso 409. */
    public static final UUID CONVENIO_FALLA_CENTRAL_ID = UUID.fromString("00000000-0000-0000-0000-0000000000fa");

    /** Simula un convenio dado de baja en la central. */
    public static final UUID CONVENIO_INACTIVO_ID = UUID.fromString("00000000-0000-0000-0000-0000000000ba");

    /** Simula un cliente sin afiliacion activa/vigente al convenio consultado. */
    public static final UUID CLIENTE_SIN_AFILIACION_ID = UUID.fromString("00000000-0000-0000-0000-0000000000af");

    /** Simula un producto que el convenio no cubre (RN24). */
    public static final UUID PRODUCTO_NO_CUBIERTO_ID = UUID.fromString("00000000-0000-0000-0000-0000000000c0");

    private static final Porcentaje PORCENTAJE_COBERTURA_DEFECTO = Porcentaje.de(80);

    @Override
    public CoberturaCentralResult consultarCobertura(UUID clienteId, UUID convenioId, UUID productoId, LocalDate hoy) {
        if (CONVENIO_FALLA_CENTRAL_ID.equals(convenioId)) {
            throw new ConsultaSeguroCentralFallidaException(
                    "No se pudo confirmar la cobertura con la central de seguros (simulado).");
        }

        boolean convenioActivo = !CONVENIO_INACTIVO_ID.equals(convenioId);
        boolean afiliacionActivaYVigente = !CLIENTE_SIN_AFILIACION_ID.equals(clienteId);

        if (!convenioActivo || !afiliacionActivaYVigente) {
            return new CoberturaCentralResult(convenioActivo, afiliacionActivaYVigente, null, null);
        }

        boolean productoCubierto = !PRODUCTO_NO_CUBIERTO_ID.equals(productoId);
        Porcentaje porcentajeCubierto = productoCubierto ? PORCENTAJE_COBERTURA_DEFECTO : null;
        CodigoAutorizacionSeguro codigoAutorizacion = productoCubierto
                ? new CodigoAutorizacionSeguro("SEG-" + convenioId.toString().substring(0, 8))
                : null;

        return new CoberturaCentralResult(true, true, porcentajeCubierto, codigoAutorizacion);
    }
}
