# De Arquitectura Hexagonal a Clean Architecture

## Por que cambiar de estilo si el resultado se parece tanto

Hexagonal (Ports & Adapters) y Clean Architecture resuelven el mismo problema de fondo —que el
negocio no dependa de Spring, de la base de datos ni del framework HTTP— pero lo explican y lo
organizan distinto:

- **Hexagonal** piensa en un hexagono con puertos de entrada y de salida: el nucleo se comunica
  con el mundo exterior a traves de interfaces (puertos) que los adaptadores implementan. La
  pregunta que responde es "¿por donde entra o sale la informacion?".
- **Clean Architecture** piensa en circulos concentricos con una unica regla explicita: las
  dependencias del codigo fuente solo pueden apuntar hacia adentro. Las capas se nombran por lo
  que contienen (Entidades, Casos de Uso, Adaptadores de Interfaz, Frameworks y Drivers), no por
  si son de entrada o de salida. La pregunta que responde es "¿que tan cerca esta esto de una
  decision de negocio, y que tan cerca de un detalle tecnico?".

En este proyecto esa diferencia de enfoque se hizo explicita reorganizando y renombrando los
modulos Maven para que el nombre de cada uno diga a que capa de Clean Architecture pertenece, en
vez de si es un adaptador "de entrada" o "de salida".

## Mapeo de modulos: t3 (Hexagonal) -> t5 (Clean Architecture)

| Modulo en t3 (Hexagonal) | Modulo en t5 (Clean Architecture) | Capa de Clean Architecture |
|---|---|---|
| `pos-domain` | `pos-domain` | Entidades (reglas de negocio empresariales) |
| `pos-application` | `pos-usecases` | Casos de uso (reglas de negocio de la aplicacion) |
| `pos-adapter-in-rest` | `pos-adapters-web` | Adaptadores de interfaz (controladores) |
| `pos-adapter-out-persistence` | `pos-adapters-persistence` | Adaptadores de interfaz (gateways hacia la base de datos) |
| `pos-adapter-out-external` | `pos-adapters-external` | Adaptadores de interfaz (gateways hacia servicios externos) |
| *(no existia)* | `pos-adapters-messaging` | Adaptadores de interfaz (gateways hacia Kafka y RabbitMQ) |
| `pos-bootstrap` | `pos-infrastructure` | Frameworks y Drivers (arranque de Spring Boot, wiring de beans) |

El paquete Java se renombro en el mismo sentido: `com.posfarmacia.application` paso a
`com.posfarmacia.usecases`, `com.posfarmacia.adapter.in.rest` paso a `com.posfarmacia.adapters.web`,
`com.posfarmacia.adapter.out.persistence` paso a `com.posfarmacia.adapters.persistence`,
`com.posfarmacia.adapter.out.external` paso a `com.posfarmacia.adapters.external`, y
`com.posfarmacia.bootstrap` paso a `com.posfarmacia.infrastructure`.

## La regla de dependencia, ahora explicita

```
                    ┌─────────────────────────────────────────┐
                    │     Frameworks y Drivers                 │
                    │     pos-infrastructure                   │
                    │  (Spring Boot, wiring de beans, config)  │
                    │                                           │
                    │   ┌───────────────────────────────────┐  │
                    │   │   Adaptadores de Interfaz          │  │
                    │   │   pos-adapters-web                 │  │
                    │   │   pos-adapters-persistence          │  │
                    │   │   pos-adapters-external             │  │
                    │   │   pos-adapters-messaging (Kafka/MQ) │  │
                    │   │                                     │  │
                    │   │   ┌─────────────────────────────┐   │  │
                    │   │   │   Casos de Uso              │   │  │
                    │   │   │   pos-usecases               │   │  │
                    │   │   │                               │   │  │
                    │   │   │   ┌───────────────────────┐   │   │  │
                    │   │   │   │   Entidades           │   │   │  │
                    │   │   │   │   pos-domain           │   │   │  │
                    │   │   │   └───────────────────────┘   │   │  │
                    │   │   └─────────────────────────────┘   │  │
                    │   └───────────────────────────────────┘  │
                    └─────────────────────────────────────────┘

        Las flechas de dependencia del codigo fuente solo apuntan hacia adentro.
```

Lo que esto significa en la practica, y que se mantuvo igual que en t3:

- `pos-domain` no depende de nada mas que del propio dominio: sin Spring, sin JPA, sin JSON, sin
  Kafka ni RabbitMQ.
- `pos-usecases` solo depende de `pos-domain` y de sus propios puertos (interfaces de entrada y
  de salida que ella misma define). Nunca importa una entidad JPA, un `KafkaTemplate`, un
  `RabbitTemplate` ni un controlador REST.
- Los adaptadores (`pos-adapters-web`, `pos-adapters-persistence`, `pos-adapters-external`,
  `pos-adapters-messaging`) implementan esos puertos. Cada uno conoce un detalle tecnico
  concreto (HTTP, JPA, un cliente externo, Kafka o RabbitMQ) y el resto del sistema no.
- `pos-infrastructure` es el unico modulo que conoce todas las implementaciones concretas a la
  vez: alli se conectan los casos de uso con sus adaptadores mediante `@Bean`, igual que en t3.
  Los casos de uso nunca crean un adaptador con `new`.

## Un puerto y adaptador nuevo: mensajeria

La pieza que no existia en t3 es `pos-adapters-messaging`. Desde el punto de vista de
`pos-usecases`, Kafka y RabbitMQ son exactamente eso: un adaptador mas, detras de un puerto de
salida que el caso de uso no sabe que esta implementado con un broker de mensajeria:

- `EventoDominioPublisherPort` — el caso de uso publica un hecho de negocio ya ocurrido
  (una venta confirmada, una operacion sensible auditada). No sabe que hay un Kafka detras.
- `TareaAsincronaPublisherPort` — el caso de uso encola un trabajo puntual para que se procese
  despues (emitir un comprobante). No sabe que hay un RabbitMQ detras.

El detalle de como se explica esta decision y por que se separaron en dos brokers distintos esta
en `KAFKA_Y_RABBITMQ.md`.
