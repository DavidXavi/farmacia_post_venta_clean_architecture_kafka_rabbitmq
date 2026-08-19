# Por que Kafka y RabbitMQ, y no solo uno de los dos

Es tentador pensar que un solo broker de mensajeria alcanza. Pero Kafka y RabbitMQ estan
optimizados para dos problemas distintos, y este proyecto tiene ambos problemas al mismo tiempo:

- **Kafka**: un registro de hechos que ya ocurrieron, que uno o varios interesados pueden leer
  despues, sin que publicarlos dependa de que alguien los este escuchando en ese momento. Es un
  log de eventos, no una cola de trabajo: los mensajes no "se consumen y desaparecen" para el
  topico, cada grupo de consumidores lee su propia copia a su propio ritmo.
- **RabbitMQ**: una tarea puntual que un unico trabajador debe ejecutar una vez, con reintentos
  y colas de trabajo (work queues) como patron natural. Es el modelo clasico de "encola esto y
  que alguien lo procese", no un registro que otros vayan a releer.

En este sistema aparecen los dos casos:

## Kafka: eventos de dominio (`pos-adapters-messaging/.../kafka`)

| Topico | Se publica cuando... | Quien lo consume hoy |
|---|---|---|
| `pos.ventas.confirmadas` | `ConfirmarVentaUseCaseImpl` confirma una venta (RN01-RN06) | `VentaConfirmadaConsumer`, alimenta el feed de Actividad reciente |
| `pos.auditoria` | Una operacion sensible ocurre (RF19): hoy, la emision de una nota de credito en `EmitirNotaCreditoUseCaseImpl` | `AuditoriaConsumer`, que recien alli persiste el `RegistroAuditoria` |

El caso de `pos.auditoria` es el mas revelador de por que Kafka encaja aqui. En t3 ya existia
todo el circuito de auditoria RF19 (`RegistrarAuditoriaUseCase`, la tabla `registro_auditoria`,
el endpoint `GET /api/auditoria`) pero **nada lo invocaba**: el puerto de entrada estaba definido
y cableado, listo para usarse, pero ningun caso de uso que dispara una operacion sensible lo
llamaba todavia. Kafka fue la pieza que le dio uso: ahora el caso de uso que dispara la operacion
sensible no llama directo al repositorio de auditoria (eso lo acoplaria a esa tabla y a esa
transaccion); publica el hecho en el topico `pos.auditoria`, y es un consumidor separado el que
decide como dejar la evidencia. Si mas adelante se agrega un segundo interesado en esas mismas
operaciones sensibles (por ejemplo, un panel de cumplimiento en tiempo real), se suscribe al
mismo topico sin tocar el caso de uso original.

## RabbitMQ: tareas asincronas (`pos-adapters-messaging/.../rabbit`)

| Cola | Se encola cuando... | Quien la consume |
|---|---|---|
| `pos.comprobantes.emitir` | `ConfirmarVentaUseCaseImpl` confirma una venta | `ComprobanteEmisionConsumer`, que simula la llamada al emisor de comprobante electronico externo |

La emision del comprobante electronico (el equivalente a un servicio tipo SUNAT) es exactamente
el caso de uso clasico de una cola de trabajo: es una llamada a un tercero que puede tardar, que
puede fallar y reintentarse, y que no tiene sentido que el cliente en caja espere para poder
seguir cobrando. Antes de este cambio esa llamada hubiera sido sincrona dentro de la misma
transaccion de la venta (como cualquier otra integracion externa en `pos-adapters-external`);
ahora se encola y se procesa aparte, sin alargar el tiempo de respuesta del checkout.

## Por que la venta no espera a ninguno de los dos

`ConfirmarVentaUseCaseImpl` guarda la venta confirmada primero, y solo despues publica el evento
en Kafka y encola la tarea en RabbitMQ (ver el metodo `publicarEventoYEncolarComprobante`). Si el
broker de mensajeria no respondiera, la venta ya quedo grabada; lo que se pierde es la
notificacion, no la operacion de negocio. Esa es una decision deliberada: los efectos secundarios
de la mensajeria nunca deben poder revertir ni bloquear la transaccion de negocio que los origino.

## Como verificar que ambos funcionan, sin mirar los logs del backend

El feed **Actividad reciente** del frontend (`/actividad-reciente`) muestra en vivo lo que los
consumidores de Kafka y RabbitMQ ya procesaron, sin necesidad de abrir una consola. Ademas:

- **Kafka UI** (`http://localhost:8090` en el `docker-compose` local) permite ver los topicos,
  sus particiones y los mensajes publicados.
- **RabbitMQ Management** (`http://localhost:15673`) permite ver las colas, cuantos mensajes
  entraron y cuantos ya fueron confirmados (`ack`).
- La pagina **Auditoria** (`/auditoria`, ya existia en t3) ahora muestra registros reales la
  primera vez que se emite una nota de credito, porque el evento de Kafka `pos.auditoria` llego
  a `AuditoriaConsumer` y este llamo a `RegistrarAuditoriaUseCase`.

El detalle de los pasos exactos que se siguieron para comprobar esto durante el desarrollo esta
en `COMO_EJECUTAR.md`.
