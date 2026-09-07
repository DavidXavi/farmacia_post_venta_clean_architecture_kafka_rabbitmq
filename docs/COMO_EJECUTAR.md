# Como levantar el proyecto y comprobar que funciona

## Requisitos

- Docker y Docker Compose (todo el sistema corre en contenedores; no hace falta instalar Java,
  Maven, Node ni Postgres en el equipo).

## Primeros pasos

1. En la raiz del proyecto, copiar `.env.example` a `.env` y completar los valores (o dejar los
   que ya trae el archivo para un entorno local de pruebas).
2. Levantar todo:

   ```
   docker compose up -d --build
   ```

3. Esperar a que los servicios queden saludables:

   ```
   docker compose ps
   ```

   `db`, `kafka` y `rabbitmq` deben mostrar `healthy` antes de que `backend` termine de iniciar
   (el `docker-compose.yml` ya declara esa dependencia con `condition: service_healthy`).

## Servicios expuestos y para que sirve cada uno

| Servicio | URL local | Para que sirve |
|---|---|---|
| Frontend (React) | http://localhost:5174 | La aplicacion POS en si |
| Backend (API REST) | http://localhost:8089 | La API que consume el frontend |
| Documentacion de la API (Swagger) | http://localhost:8089/api/v1/swagger-ui.html | Explorar los endpoints disponibles |
| Base de datos (Postgres) | localhost:5451 | Solo para inspeccionar datos con un cliente SQL si hace falta |
| Kafka (broker) | localhost:9095 | Puerto del broker para un cliente Kafka externo, si se necesita |
| Kafka UI | http://localhost:8090 | Ver topicos, particiones y mensajes de Kafka desde el navegador |
| RabbitMQ (AMQP) | localhost:5673 | Puerto del broker para un cliente AMQP externo, si se necesita |
| RabbitMQ Management | http://localhost:15673 | Ver colas y mensajes de RabbitMQ desde el navegador (usuario y clave: los de `RABBITMQ_USER`/`RABBITMQ_PASSWORD` en `.env`) |

## Verificacion funcional paso a paso (la que se hizo durante el desarrollo)

1. Abrir http://localhost:5174 — carga la pantalla de acceso con Vite/React ya compilado y
   servido por Nginx.
2. Ingresar con el usuario de prueba `admin` / `Admin123!` (usuario sembrado por la migracion
   `V9__seed_datos_prueba.sql`, igual que en t3).
3. Ir a **Caja**, elegir una caja y presionar **Abrir caja** con el monto inicial sugerido.
4. Ir a **Venta (POS)**, elegir la misma caja y presionar **Iniciar venta**.
5. Agregar un producto (por ejemplo, Paracetamol 500mg), registrar un pago en **Efectivo** por el
   total exacto que muestra la venta, y presionar **Confirmar venta**. Debe aparecer
   "Venta confirmada. Comprobante ...".
6. Ir a **Actividad (Kafka/RabbitMQ)** en el menu lateral: a los pocos segundos deben aparecer
   dos filas nuevas — una con origen `KAFKA` (`VENTA_CONFIRMADA`) y otra con origen `RABBITMQ`
   (`COMPROBANTE_EMITIDO`) — que confirman que el evento publicado por la venta llego a su
   consumidor y que la tarea encolada tambien fue procesada.
7. (Opcional, para ver el lado de auditoria) Ir a **Devoluciones** o al endpoint de notas de
   credito y emitir una nota de credito sobre una venta confirmada; luego revisar **Auditoria**:
   el registro aparece alli porque paso primero por Kafka (`pos.auditoria`) y el consumidor lo
   persistio.
8. Para confirmar el estado de los brokers directamente: abrir http://localhost:8090 (Kafka UI)
   y ver los topicos `pos.ventas.confirmadas` y `pos.auditoria` con sus particiones; abrir
   http://localhost:15673 (RabbitMQ Management) y ver la cola `pos.comprobantes.emitir` con sus
   contadores de mensajes entregados y confirmados.

Estos ocho pasos son exactamente los que se ejecutaron para validar el proyecto: el login, la
apertura de caja, la venta completa, y la comprobacion visual de que ambos brokers entregaron sus
mensajes, sin necesidad de leer los logs del contenedor del backend.

## Verificacion del segundo factor (Google Authenticator)

Detalle completo en la carpeta `autenticacion/` (manual de uso, cambios en el codigo y
decisiones de diseno). Comprobacion en el navegador:

1. Con la sesion de `admin` abierta, ir a **Seguridad (MFA)** en el menu lateral y presionar
   **Activar**: aparece el codigo QR y, debajo, la clave para carga manual.
2. Escanear el QR con Google Authenticator (o escribir la clave a mano en la app).
3. Escribir en el formulario el codigo de 6 digitos que muestra la app y presionar
   **Confirmar**. El aviso confirma que quedo activado.
4. Cerrar sesion y volver a entrar con `admin` / `Admin123!`: ahora aparece la pantalla
   **Verificacion en dos pasos** en vez de entrar directo. Escribir el codigo de la app.
5. Un codigo incorrecto responde `MFA_REQUERIDA` y no abre sesion; el token intermedio caduca a
   los 5 minutos.
6. Para volver al estado inicial, en **Seguridad (MFA)** presionar **Desactivar**.

## Verificacion del login social (Google / Facebook)

En la pantalla de acceso, los botones **Continuar con Google** y **Continuar con Facebook**
llevan al consentimiento del proveedor y vuelven al POS con la sesion iniciada.

Sin credenciales reales en `.env`, el flujo llega hasta el proveedor y este responde
`invalid_client` (Google) o el equivalente de Facebook: eso confirma que la redireccion y la URI
de retorno estan bien armadas, pero para completar el login hay que registrar la aplicacion en
cada consola y poner `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` /
`FACEBOOK_CLIENT_ID` / `FACEBOOK_CLIENT_SECRET` en `.env` (el paso a paso esta en
`autenticacion/MANUAL.md`).

## Pruebas automatizadas del backend

```
cd backend
./mvnw test
```

Corre las pruebas unitarias de dominio y casos de uso (sin necesidad de Docker ni de una base de
datos real), incluida la prueba de `ConfirmarVentaUseCaseImpl` que ahora tambien recibe los dos
puertos de mensajeria como mocks.
