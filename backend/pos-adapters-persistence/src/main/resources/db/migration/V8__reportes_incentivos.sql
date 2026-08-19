-- Contexto: Reportes e Incentivos (RF17, RF18). Contexto de solo lectura sobre datos de otros
-- contextos (ventas, lotes); las dos unicas tablas propias son reglas_incentivo e
-- incentivos_venta. Sin REFERENCES hacia otros contextos (producto_id, categoria_id, usuario_id,
-- venta_id, detalle_venta_id se dejan como columna simple); la integridad referencial entre
-- contextos es responsabilidad de la aplicacion (ver convenciones-migracion-java.md).

CREATE TABLE reglas_incentivo (
    id uuid PRIMARY KEY,
    nombre varchar(150) NOT NULL,
    producto_id uuid,
    categoria_id uuid,
    monto_por_unidad numeric(12,2) NOT NULL,
    vigencia_inicio date,
    vigencia_fin date,
    activa boolean NOT NULL DEFAULT true
);

CREATE TABLE incentivos_venta (
    id uuid PRIMARY KEY,
    regla_incentivo_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    venta_id uuid NOT NULL,
    detalle_venta_id uuid NOT NULL,
    cantidad integer NOT NULL,
    monto_calculado numeric(12,2) NOT NULL,
    fecha timestamptz NOT NULL
);

CREATE INDEX idx_incentivos_venta_fecha ON incentivos_venta(fecha);
CREATE INDEX idx_incentivos_venta_usuario ON incentivos_venta(usuario_id);
