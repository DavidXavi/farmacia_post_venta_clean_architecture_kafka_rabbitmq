# POS Farmacia

Sistema de punto de venta para farmacias construido con **Clean Architecture** en el backend y
mensajería basada en eventos con **Apache Kafka** y **RabbitMQ**.

Cubre el flujo completo de una farmacia: apertura y cierre de caja, ventas con validación de
stock en tiempo real, despacho de lotes por FEFO, control de medicamentos que requieren receta,
convenios de seguro con cálculo de copago, líneas de crédito, devoluciones y notas de crédito,
reportes de ventas e incentivos, y auditoría de operaciones sensibles.

## Características principales

- **Caja y ventas** — apertura/cierre de caja, punto de venta, múltiples formas de pago,
  comprobantes electrónicos.
- **Inventario por lotes (FEFO)** — control de vencimientos, despacho del lote más próximo a
  vencer, bloqueo y retiro de lotes.
- **Recetas médicas** — validación de medicamentos controlados antes de la venta.
- **Seguros y crédito** — convenios de cobertura, cálculo automático de copago, líneas de
  crédito con validación de saldo.
- **Devoluciones y notas de crédito** — reversión de stock y reconciliación de venta.
- **Auditoría** — registro de operaciones sensibles (anulaciones, cambios de precio, ajustes de
  stock, cambios de promoción) publicado como evento y persistido de forma asíncrona.
- **Reportes e incentivos** — ventas diarias y cálculo de incentivos por reglas configurables.

## Arquitectura

El backend sigue Clean Architecture: el dominio y los casos de uso no dependen de ningún
framework, y toda la infraestructura (base de datos, HTTP, mensajería) vive detrás de puertos
que el núcleo del sistema define.

```
backend/
├── pos-domain/                # Entidades y reglas de negocio
├── pos-usecases/              # Casos de uso, puertos de entrada/salida
├── pos-adapters-web/          # Controladores REST
├── pos-adapters-persistence/  # Repositorios JPA y migraciones
├── pos-adapters-external/     # Integraciones externas
├── pos-adapters-messaging/    # Publicadores y consumidores de Kafka y RabbitMQ
└── pos-infrastructure/        # Arranque de Spring Boot y ensamblado de dependencias
```

Detalle completo de las capas y la regla de dependencia en [`docs/ARQUITECTURA.md`](docs/ARQUITECTURA.md).

### Mensajería

- **Apache Kafka** publica hechos de negocio ya confirmados (ventas, operaciones auditables)
  como eventos que uno o más consumidores pueden procesar de forma independiente.
- **RabbitMQ** encola tareas puntuales —como la emisión del comprobante electrónico— para que
  se ejecuten de forma asíncrona sin bloquear el flujo de venta.

Justificación de por qué se usan ambos brokers y no solo uno en
[`docs/KAFKA_Y_RABBITMQ.md`](docs/KAFKA_Y_RABBITMQ.md).

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 17, Spring Boot, Spring Data JPA, Spring Security (JWT) |
| Frontend | React, Vite |
| Base de datos | PostgreSQL, Flyway |
| Mensajería | Apache Kafka, RabbitMQ |
| Documentación de API | springdoc-openapi / Swagger UI |
| Contenedores | Docker, Docker Compose |

## Puesta en marcha

Requisitos: Docker y Docker Compose.

```bash
cp .env.example .env
docker compose up -d --build
```

| Servicio | URL |
|---|---|
| Aplicación web | http://localhost:5174 |
| API REST | http://localhost:8089 |
| Documentación de la API | http://localhost:8089/api/v1/swagger-ui.html |
| Kafka UI | http://localhost:8090 |
| RabbitMQ Management | http://localhost:15673 |

Guía paso a paso, incluida una verificación funcional completa, en
[`docs/COMO_EJECUTAR.md`](docs/COMO_EJECUTAR.md).

## Pruebas

```bash
cd backend
./mvnw test
```

## Documentación

| Documento | Contenido |
|---|---|
| [`docs/ARQUITECTURA.md`](docs/ARQUITECTURA.md) | Capas del backend y regla de dependencia |
| [`docs/KAFKA_Y_RABBITMQ.md`](docs/KAFKA_Y_RABBITMQ.md) | Diseño de la mensajería y sus casos de uso |
| [`docs/COMO_EJECUTAR.md`](docs/COMO_EJECUTAR.md) | Puesta en marcha y verificación funcional |

## Estructura del repositorio

```
.
├── backend/           # API REST (Java + Spring Boot, multimódulo Maven)
├── frontend/           # Aplicación web (React + Vite)
├── docs/               # Documentación técnica
├── base/               # Reglas de negocio y especificación funcional
└── docker-compose.yml  # Orquestación de todos los servicios
```
