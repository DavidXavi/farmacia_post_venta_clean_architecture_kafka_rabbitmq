-- Contexto: Clientes, Convenios de seguro (copago) y Linea de credito (RF09-RF11, RN22-RN32).
-- FK solo dentro de este mismo contexto/migracion. Referencias a otros contextos
-- (producto_id, venta_id) se dejan como columna simple sin REFERENCES, la integridad
-- referencial entre contextos es responsabilidad de la aplicacion (ver convenciones-migracion-java.md).

CREATE TABLE clientes (
    id uuid PRIMARY KEY,
    dni varchar(8) NOT NULL UNIQUE,
    nombres varchar(150) NOT NULL,
    apellidos varchar(150) NOT NULL,
    fecha_nacimiento date,
    telefono varchar(30),
    correo varchar(150),
    direccion varchar(250),
    estado varchar(20) NOT NULL
);

CREATE TABLE convenios_seguro (
    id uuid PRIMARY KEY,
    nombre varchar(150) NOT NULL,
    activo boolean NOT NULL DEFAULT true
);

CREATE TABLE coberturas_seguro (
    id uuid PRIMARY KEY,
    convenio_id uuid NOT NULL REFERENCES convenios_seguro(id),
    producto_id uuid NOT NULL,
    porcentaje_cubierto numeric(5,2) NOT NULL
);

CREATE INDEX idx_coberturas_seguro_convenio ON coberturas_seguro(convenio_id);
CREATE UNIQUE INDEX uq_coberturas_seguro_convenio_producto ON coberturas_seguro(convenio_id, producto_id);

CREATE TABLE afiliaciones_cliente (
    id uuid PRIMARY KEY,
    cliente_id uuid NOT NULL REFERENCES clientes(id),
    convenio_id uuid NOT NULL REFERENCES convenios_seguro(id),
    vigencia_inicio date,
    vigencia_fin date,
    estado varchar(20) NOT NULL
);

CREATE INDEX idx_afiliaciones_cliente_cliente ON afiliaciones_cliente(cliente_id);
CREATE UNIQUE INDEX uq_afiliaciones_cliente_convenio ON afiliaciones_cliente(cliente_id, convenio_id);

CREATE TABLE lineas_credito (
    id uuid PRIMARY KEY,
    cliente_id uuid NOT NULL REFERENCES clientes(id),
    monto_autorizado numeric(12,2) NOT NULL,
    saldo_disponible numeric(12,2) NOT NULL,
    vigencia_inicio date,
    vigencia_fin date,
    estado varchar(20) NOT NULL
);

CREATE UNIQUE INDEX uq_lineas_credito_cliente ON lineas_credito(cliente_id);

CREATE TABLE movimientos_credito (
    id uuid PRIMARY KEY,
    linea_credito_id uuid NOT NULL REFERENCES lineas_credito(id),
    venta_id uuid,
    tipo varchar(20) NOT NULL,
    monto numeric(12,2) NOT NULL,
    fecha timestamptz NOT NULL
);

CREATE INDEX idx_movimientos_credito_linea ON movimientos_credito(linea_credito_id);
