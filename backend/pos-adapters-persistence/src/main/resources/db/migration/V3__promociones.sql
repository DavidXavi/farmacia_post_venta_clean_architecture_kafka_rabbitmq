-- Contexto: Promociones (RN07-RN13, RF06).
-- producto_id no lleva REFERENCES: la tabla productos se crea en la migracion V2 de otro
-- contexto, y la integridad referencial entre contextos es responsabilidad de la aplicacion.

CREATE TABLE promociones (
    id                UUID PRIMARY KEY,
    nombre            VARCHAR(150)  NOT NULL,
    descripcion       VARCHAR(500),
    tipo_beneficio    VARCHAR(30)   NOT NULL,
    valor_beneficio   NUMERIC(12,2) NOT NULL,
    requiere_cliente  BOOLEAN       NOT NULL DEFAULT FALSE,
    cantidad_minima   INTEGER       NOT NULL DEFAULT 1,
    vigencia_inicio   DATE,
    vigencia_fin      DATE,
    activa            BOOLEAN       NOT NULL DEFAULT TRUE
);

CREATE TABLE promocion_condiciones (
    id            UUID PRIMARY KEY,
    promocion_id  UUID NOT NULL REFERENCES promociones(id) ON DELETE CASCADE,
    producto_id   UUID NOT NULL
);

CREATE INDEX idx_promocion_condiciones_promocion ON promocion_condiciones(promocion_id);
CREATE INDEX idx_promocion_condiciones_producto ON promocion_condiciones(producto_id);
