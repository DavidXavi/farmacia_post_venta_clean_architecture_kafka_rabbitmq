# Estado de la migración

Este proyecto parte de una versión anterior del sistema construida con Arquitectura Hexagonal
(Java + Spring Boot, sin mensajería). Este documento resume la migración a Clean Architecture y
la incorporación de Apache Kafka y RabbitMQ.

## Estado actual

El backend compila limpio (`./mvnw clean test` pasa) y el stack completo levanta con
`docker compose up -d --build`: Postgres, Zookeeper, Kafka, Kafka UI, RabbitMQ, backend y
frontend. Se verificó manualmente un flujo completo de venta (login, apertura de caja, venta
confirmada) y se confirmó que el evento de Kafka y la tarea de RabbitMQ que esa venta dispara
llegan a sus consumidores (visible en `/actividad-reciente`, en Kafka UI y en RabbitMQ
Management).

Para reproducir la verificación:

```
cd backend && ./mvnw clean test
docker compose up -d --build
```

## Cambios realizados

1. **Renombrado de módulos y paquetes de Hexagonal a Clean Architecture** (detalle completo en
   `ARQUITECTURA.md`): `pos-application` → `pos-usecases`, `pos-adapter-in-rest` →
   `pos-adapters-web`, `pos-adapter-out-persistence` → `pos-adapters-persistence`,
   `pos-adapter-out-external` → `pos-adapters-external`, `pos-bootstrap` →
   `pos-infrastructure`. `pos-domain` no cambió de nombre: la capa de Entidades se llama igual
   en ambos estilos.
2. **Módulo nuevo `pos-adapters-messaging`** con los adaptadores de Kafka y RabbitMQ:
   - Kafka: `KafkaEventPublisherAdapter` (implementa `EventoDominioPublisherPort`),
     `VentaConfirmadaConsumer` y `AuditoriaConsumer`.
   - RabbitMQ: `RabbitTaskPublisherAdapter` (implementa `TareaAsincronaPublisherPort`) y
     `ComprobanteEmisionConsumer`.
   - Puertos y DTOs nuevos en `pos-usecases` (paquete `mensajeria`): `EventoDominioPublisherPort`,
     `TareaAsincronaPublisherPort`, `VentaConfirmadaEvent`, `EventoAuditoria`,
     `TareaEmisionComprobante`, más el feed `ActividadReciente` (puerto, caso de uso, repositorio
     en memoria) que expone `/api/actividad-reciente` para comprobar en el navegador que los
     consumidores procesan los mensajes.
3. **`ConfirmarVentaUseCaseImpl` publica un evento de venta confirmada en Kafka y encola la
   emisión del comprobante en RabbitMQ**, después de guardar la venta (no antes, y nunca de
   forma que pueda revertirla). **`EmitirNotaCreditoUseCaseImpl` publica un evento de auditoría
   en Kafka**: el puerto `RegistrarAuditoriaUseCase` (RF19) ya existía con su endpoint
   `GET /api/auditoria` completo, pero ningún caso de uso lo invocaba todavía. Kafka le dio el
   primer productor real a ese circuito.
4. **Página nueva en el frontend**: `/actividad-reciente` (menú "Actividad (Kafka/RabbitMQ)"),
   que hace polling cada 4 segundos y muestra lo último que procesaron ambos consumidores.
5. **`docker-compose.yml` ampliado** con `zookeeper`, `kafka` (Confluent 7.6.0, puerto externo
   `9095`), `kafka-ui` (puerto `8090`) y `rabbitmq` (imagen `3-management-alpine`, puertos `5673`
   AMQP y `15673` panel de administración).
6. **CORS**: se agregó la variable `POS_FARMACIA_CORS_ORIGENES_PERMITIDOS` en
   `docker-compose.yml` para permitir el nuevo origen del frontend sin tocar el valor por
   defecto del código.

## Nota técnica: autoconfiguración de Kafka en Spring Boot 4

Al integrar `spring-kafka`, la aplicación fallaba en el arranque con
`UnsatisfiedDependencyException: No qualifying bean of type 'KafkaTemplate'` a pesar de que la
librería estaba en el classpath. Spring Boot 4.x modularizó la autoconfiguración de Kafka en un
artefacto separado (`spring-boot-kafka`), el mismo patrón que ya aplica a Flyway. La solución fue
declarar esa dependencia explícita en `pos-adapters-messaging/pom.xml`. Al agregar una nueva
dependencia de infraestructura en Spring Boot 4.x conviene verificar si el starter clásico
alcanza por sí solo o si esta versión exige además el módulo `spring-boot-<feature>`
correspondiente.

## Pendiente

1. **El evento de auditoría en Kafka solo se dispara desde `EmitirNotaCreditoUseCaseImpl`.**
   El resto de operaciones sensibles de RF19 (cambios de precio, ajustes de stock, validación de
   recetas, cambios de promoción, anulaciones) todavía no publican a `pos.auditoria`. Extender el
   mismo patrón al resto es mecánico: inyectar `EventoDominioPublisherPort` y llamar
   `publicarAuditoria(...)` donde antes no había ninguna llamada a auditoría.
2. **El feed `ActividadReciente` es en memoria, no persistente** (se pierde si el backend se
   reinicia). Es una decisión deliberada: su único propósito es dejar ver en el navegador que
   los consumidores funcionan, no ser un registro de auditoría (para eso ya existe la tabla
   `registro_auditoria`, que sí es durable).
3. **No hay Dead Letter Queue ni reintentos configurados en RabbitMQ**, ni un manejo explícito de
   errores de deserialización en los `@KafkaListener`/`@RabbitListener` más allá de loguear la
   excepción. Para un entorno productivo real conviene agregar una DLQ para
   `pos.comprobantes.emitir` y una política de reintentos con backoff.
4. **ArchUnit**: la dependencia está declarada en el parent POM pero no hay ninguna regla escrita
   todavía que verifique automáticamente la regla de dependencias entre capas descrita en
   `ARQUITECTURA.md`.

## Referencias

- Backend: `backend/` (ver `ARQUITECTURA.md` para el detalle de cada módulo)
- Frontend: `frontend/`
- Documentación: `docs/` (este archivo, `ARQUITECTURA.md`, `KAFKA_Y_RABBITMQ.md`,
  `COMO_EJECUTAR.md`)
- Docker: `docker-compose.yml` + `.env.example` en la raíz, `backend/Dockerfile`,
  `frontend/Dockerfile`
