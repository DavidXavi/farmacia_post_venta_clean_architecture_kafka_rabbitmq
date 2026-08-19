package com.posfarmacia.usecases.port.out.seguro;

import com.posfarmacia.domain.exception.ConsultaSeguroCentralFallidaException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Puerto de salida hacia la central/servicio del seguro (Word, seccion 6.4): consulta
 * la afiliacion del cliente a un convenio y la cobertura vigente para un producto.
 *
 * RN27: si la central no puede confirmar la cobertura (falla de comunicacion, timeout,
 * etc.), la implementacion debe lanzar {@link ConsultaSeguroCentralFallidaException} en
 * vez de devolver un resultado que el caso de uso pudiera interpretar como "no cubierto".
 */
public interface SeguroCentralPort {

    CoberturaCentralResult consultarCobertura(UUID clienteId, UUID convenioId, UUID productoId, LocalDate hoy)
            throws ConsultaSeguroCentralFallidaException;
}
