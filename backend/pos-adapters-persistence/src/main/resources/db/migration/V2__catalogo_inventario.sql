-- Contexto: Catalogo (productos) e Inventario (lotes, existencias, movimientos).
-- RF03 productos, RF04 lotes/existencias, RF13 FEFO, RF14 no vendibles, RF15 actualizacion de inventario.
-- No se agregan REFERENCES hacia tablas de otros contextos (locales, usuarios): la integridad
-- referencial entre contextos es responsabilidad de la aplicacion (ver convenciones-migracion-java.md).

CREATE TABLE categorias (
    id uuid PRIMARY KEY,
    nombre varchar(150) NOT NULL
);

CREATE TABLE laboratorios (
    id uuid PRIMARY KEY,
    nombre varchar(150) NOT NULL
);

CREATE TABLE presentaciones (
    id uuid PRIMARY KEY,
    nombre varchar(100) NOT NULL,
    unidad_medida varchar(50) NOT NULL
);

CREATE TABLE productos (
    id uuid PRIMARY KEY,
    codigo_interno varchar(50) NOT NULL UNIQUE,
    codigo_barras varchar(50),
    nombre_comercial varchar(200) NOT NULL,
    descripcion varchar(1000) NOT NULL DEFAULT '',
    tipo_producto varchar(30) NOT NULL,
    categoria_id uuid NOT NULL REFERENCES categorias (id),
    laboratorio_id uuid NOT NULL REFERENCES laboratorios (id),
    presentacion_id uuid NOT NULL REFERENCES presentaciones (id),
    precio_venta numeric(12, 2) NOT NULL,
    es_controlado boolean NOT NULL DEFAULT false,
    requiere_receta boolean NOT NULL DEFAULT false,
    tipo_receta_requerida varchar(30),
    estado varchar(20) NOT NULL DEFAULT 'ACTIVO'
);

CREATE INDEX idx_productos_categoria ON productos (categoria_id);
CREATE INDEX idx_productos_laboratorio ON productos (laboratorio_id);

CREATE TABLE lotes (
    id uuid PRIMARY KEY,
    codigo varchar(50) NOT NULL,
    producto_id uuid NOT NULL REFERENCES productos (id),
    fecha_vencimiento date NOT NULL,
    cantidad_recibida integer NOT NULL,
    cantidad_disponible integer NOT NULL,
    costo numeric(12, 2),
    local_id uuid NOT NULL,
    estado varchar(20) NOT NULL DEFAULT 'DISPONIBLE'
);

CREATE INDEX idx_lotes_producto_local ON lotes (producto_id, local_id);
CREATE INDEX idx_lotes_fecha_vencimiento ON lotes (fecha_vencimiento);

CREATE TABLE existencias_lote (
    id uuid PRIMARY KEY,
    producto_id uuid NOT NULL REFERENCES productos (id),
    local_id uuid NOT NULL,
    cantidad_actual integer NOT NULL DEFAULT 0,
    actualizado_en timestamptz NOT NULL,
    UNIQUE (producto_id, local_id)
);

CREATE TABLE movimientos_inventario (
    id uuid PRIMARY KEY,
    lote_id uuid NOT NULL REFERENCES lotes (id),
    tipo varchar(30) NOT NULL,
    cantidad integer NOT NULL,
    usuario_id uuid NOT NULL,
    referencia varchar(200),
    fecha timestamptz NOT NULL
);

CREATE INDEX idx_movimientos_inventario_lote ON movimientos_inventario (lote_id);
