package com.posfarmacia.adapters.messaging;

/** Nombres de los topicos de Kafka y las colas de RabbitMQ usados por este modulo. */
public final class MensajeriaDestinos {

    public static final String TOPIC_VENTAS_CONFIRMADAS = "pos.ventas.confirmadas";
    public static final String TOPIC_AUDITORIA = "pos.auditoria";
    public static final String QUEUE_COMPROBANTES_EMITIR = "pos.comprobantes.emitir";

    private MensajeriaDestinos() {
    }
}
