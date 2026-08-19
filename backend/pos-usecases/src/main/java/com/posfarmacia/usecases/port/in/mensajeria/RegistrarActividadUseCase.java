package com.posfarmacia.usecases.port.in.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;

/** Puerto de entrada invocado por los consumidores de Kafka/RabbitMQ al procesar un mensaje. */
public interface RegistrarActividadUseCase {

    void registrar(ActividadReciente actividad);
}
