# Documentacion del proyecto

Este proyecto parte de `arquitecttura_2_t3` (Sistema POS Farmacia en Arquitectura Hexagonal,
Java + Spring Boot) y lo lleva un paso mas alla: la misma logica de negocio, reorganizada como
Clean Architecture, con Apache Kafka y RabbitMQ agregados para dos necesidades reales que el
sistema no cubria antes.

Indice de esta carpeta:

- `ARQUITECTURA.md` — que cambio entre Hexagonal (t3) y Clean Architecture (t5), y como quedan
  organizados los modulos del backend.
- `KAFKA_Y_RABBITMQ.md` — por que se agregaron dos brokers de mensajeria en vez de uno, que
  problema resuelve cada uno, y como estan conectados con el resto del sistema.
- `COMO_EJECUTAR.md` — como levantar todo con Docker y como comprobar, paso a paso, que
  funciona (incluye lo que se verifico en el navegador durante el desarrollo).
- `ESTADO_MIGRACION.md` — estado actual del proyecto: que se porto de t3, que se agrego, y que
  queda pendiente si alguien quiere continuar desde aqui.
