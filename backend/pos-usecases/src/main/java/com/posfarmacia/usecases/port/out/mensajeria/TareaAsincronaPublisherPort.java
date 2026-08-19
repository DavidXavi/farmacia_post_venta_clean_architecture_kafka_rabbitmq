package com.posfarmacia.usecases.port.out.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.TareaEmisionComprobante;

/**
 * Puerto de salida hacia RabbitMQ: encola tareas puntuales que un unico consumidor debe
 * ejecutar de forma asincrona (cola de trabajo), a diferencia del flujo de eventos de
 * {@link EventoDominioPublisherPort} sobre Kafka.
 */
public interface TareaAsincronaPublisherPort {

    void encolarEmisionComprobante(TareaEmisionComprobante tarea);
}
