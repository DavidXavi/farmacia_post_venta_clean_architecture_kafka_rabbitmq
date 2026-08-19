package com.posfarmacia.usecases.dto.mensajeria;

import java.time.Instant;

/**
 * Entrada del feed de actividad reciente que expone el estado observable de los eventos de
 * Kafka y las tareas de RabbitMQ ya procesadas, para verificar en el navegador que ambos
 * brokers estan realmente entregando mensajes.
 */
public record ActividadReciente(String origen, String tipo, String descripcion, Instant ocurridoEn) {
}
