package com.posfarmacia.usecases.port.out.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.EventoAuditoria;
import com.posfarmacia.usecases.dto.mensajeria.VentaConfirmadaEvent;

/**
 * Puerto de salida hacia Apache Kafka: publica hechos de negocio ya ocurridos en topicos de
 * solo-anexo (event streaming), para que cualquier consumidor interesado (auditoria,
 * analitica, actividad reciente) los procese de forma independiente al caso de uso que los
 * origino.
 */
public interface EventoDominioPublisherPort {

    void publicarVentaConfirmada(VentaConfirmadaEvent evento);

    void publicarAuditoria(EventoAuditoria evento);
}
