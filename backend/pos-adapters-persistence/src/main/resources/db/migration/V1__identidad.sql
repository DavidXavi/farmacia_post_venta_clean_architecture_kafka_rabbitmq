-- Contexto: Identidad, Caja y Auditoria (RF01, RF02, RF19).
-- No se agregan REFERENCES hacia tablas de otros contextos (aun no existen); las
-- referencias entre tablas de este mismo archivo si usan FK porque son del mismo contexto.

CREATE TABLE roles (
    id          UUID PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE TABLE locales (
    id        UUID PRIMARY KEY,
    nombre    VARCHAR(150) NOT NULL,
    direccion VARCHAR(255),
    activo    BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE usuarios (
    id             UUID PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    estado         VARCHAR(20)  NOT NULL,
    local_id       UUID         NOT NULL,
    permisos       VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE TABLE usuarios_roles (
    usuario_id UUID NOT NULL REFERENCES usuarios (id),
    rol_id     UUID NOT NULL REFERENCES roles (id),
    PRIMARY KEY (usuario_id, rol_id)
);

CREATE TABLE cajas (
    id       UUID PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    local_id UUID         NOT NULL,
    activa   BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE sesiones_caja (
    id                  UUID PRIMARY KEY,
    caja_id             UUID           NOT NULL REFERENCES cajas (id),
    usuario_id          UUID           NOT NULL REFERENCES usuarios (id),
    fecha_apertura      TIMESTAMPTZ    NOT NULL,
    monto_inicial       NUMERIC(12, 2) NOT NULL,
    fecha_cierre        TIMESTAMPTZ,
    monto_esperado      NUMERIC(12, 2),
    monto_declarado     NUMERIC(12, 2),
    diferencia          NUMERIC(12, 2),
    observacion_cierre  VARCHAR(500),
    estado              VARCHAR(20)    NOT NULL
);

CREATE INDEX idx_sesiones_caja_caja_id_estado ON sesiones_caja (caja_id, estado);

CREATE TABLE auditoria_operaciones (
    id               UUID        PRIMARY KEY,
    fecha            TIMESTAMPTZ NOT NULL,
    usuario_id       UUID        NOT NULL REFERENCES usuarios (id),
    accion           VARCHAR(100) NOT NULL,
    entidad          VARCHAR(100) NOT NULL,
    entidad_id       VARCHAR(100) NOT NULL,
    detalle          VARCHAR(1000) NOT NULL,
    datos_anteriores TEXT,
    datos_nuevos     TEXT
);

CREATE INDEX idx_auditoria_fecha ON auditoria_operaciones (fecha);
CREATE INDEX idx_auditoria_entidad ON auditoria_operaciones (entidad);

-- Catalogo fijo de roles (equivalente a RolNombre): datos de referencia, no de negocio.
INSERT INTO roles (id, nombre, descripcion) VALUES
    ('11111111-1111-1111-1111-111111111101', 'ADMINISTRADOR', 'Administra el sistema y supervisa las operaciones'),
    ('11111111-1111-1111-1111-111111111102', 'CAJERO', 'Atiende al cliente y registra ventas en el POS'),
    ('11111111-1111-1111-1111-111111111103', 'QUIMICO_FARMACEUTICO', 'Valida recetas y medicamentos controlados'),
    ('11111111-1111-1111-1111-111111111104', 'ENCARGADO_INVENTARIO', 'Gestiona lotes y existencias'),
    ('11111111-1111-1111-1111-111111111105', 'OPERADOR_CENTRAL', 'Administra informacion compartida entre sedes');
