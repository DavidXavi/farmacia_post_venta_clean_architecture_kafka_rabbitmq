-- Contexto: Anulaciones y notas de credito (RF16, RN39-RN44). FK solo dentro de este mismo
-- contexto/migracion (devoluciones -> detalle_devoluciones). Referencias a otros contextos
-- (venta_id, usuario_id, producto_id, detalle_venta_id, comprobante_id) se dejan como columna
-- simple sin REFERENCES; la integridad referencial entre contextos es responsabilidad de la
-- aplicacion (ver convenciones-migracion-java.md). La anulacion directa de una venta del mismo
-- dia (RN39) no agrega tabla propia: cambia el estado de la venta ya creada en V6.

CREATE TABLE devoluciones (
    id uuid PRIMARY KEY,
    venta_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    motivo varchar(500) NOT NULL,
    fecha timestamptz NOT NULL
);

CREATE INDEX idx_devoluciones_venta ON devoluciones(venta_id);

CREATE TABLE detalle_devoluciones (
    id uuid PRIMARY KEY,
    devolucion_id uuid NOT NULL REFERENCES devoluciones(id),
    detalle_venta_id uuid NOT NULL,
    producto_id uuid NOT NULL,
    cantidad integer NOT NULL,
    monto_devuelto numeric(12,2) NOT NULL
);

CREATE INDEX idx_detalle_devoluciones_devolucion ON detalle_devoluciones(devolucion_id);

CREATE TABLE notas_credito (
    id uuid PRIMARY KEY,
    venta_id uuid NOT NULL,
    comprobante_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    motivo varchar(500) NOT NULL,
    monto_total numeric(12,2) NOT NULL,
    fecha timestamptz NOT NULL
);

CREATE INDEX idx_notas_credito_venta ON notas_credito(venta_id);
