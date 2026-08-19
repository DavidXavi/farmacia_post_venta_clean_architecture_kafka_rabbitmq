-- Contexto: Recetas y validacion de medicamentos controlados (RN14-RN21).
-- Numeracion asignada a este contexto: V4 (ver docs/convenciones-migracion-java.md).
-- producto_id/cliente_id/venta_id referencian agregados de otros contextos (producto,
-- cliente, venta) que se implementan en paralelo: se dejan como columna uuid simple,
-- sin REFERENCES, tal como indica la convencion del proyecto.

CREATE TABLE recetas (
    id                    uuid PRIMARY KEY,
    numero                varchar(50) NOT NULL,
    tipo                  varchar(30) NOT NULL,
    fecha_emision         date NOT NULL,
    fecha_vencimiento     date,
    producto_id           uuid NOT NULL,
    cliente_id            uuid,
    datos_paciente        varchar(300) NOT NULL,
    datos_profesional     varchar(300) NOT NULL,
    dosis                 varchar(300),
    cantidad_autorizada   integer NOT NULL,
    archivo_respaldo_url  varchar(500),
    estado                varchar(20) NOT NULL,
    retenida_en_botica    boolean NOT NULL DEFAULT false,
    version               bigint NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX ux_recetas_numero ON recetas (numero);
CREATE INDEX ix_recetas_producto_id ON recetas (producto_id);
CREATE INDEX ix_recetas_cliente_id ON recetas (cliente_id);

CREATE TABLE usos_receta (
    id          uuid PRIMARY KEY,
    receta_id   uuid NOT NULL,
    venta_id    uuid NOT NULL,
    fecha       timestamptz NOT NULL
);

CREATE INDEX ix_usos_receta_receta_id ON usos_receta (receta_id);
CREATE INDEX ix_usos_receta_venta_id ON usos_receta (venta_id);
