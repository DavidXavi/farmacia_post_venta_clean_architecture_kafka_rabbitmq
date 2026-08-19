-- Contexto: Ventas y pagos (RF05-RF13, RN01-RN09, RN33-RN38). Orquesta los contextos ya
-- migrados (identidad/caja, catalogo/inventario, promociones, recetas, clientes/seguros/credito).
-- FK solo dentro de este mismo contexto/migracion. Referencias a otros contextos (caja_id,
-- sesion_caja_id, usuario_id, cliente_id, convenio_seguro_id, linea_credito_id, producto_id,
-- receta_id, promocion_aplicada_id, lote_id) se dejan como columna simple sin REFERENCES; la
-- integridad referencial entre contextos es responsabilidad de la aplicacion (ver
-- convenciones-migracion-java.md).
--
-- formas_pago no aparece en la lista de tablas minimas de la seccion 9 del Word ni en el resumen
-- de convenciones-migracion-java.md, pero es indispensable para pagos.forma_pago_id (RF12) y no la
-- crea ninguna otra migracion (V1-V5): se agrega aqui, junto con el resto de "Ventas y pagos".

CREATE TABLE formas_pago (
    id uuid PRIMARY KEY,
    nombre varchar(100) NOT NULL,
    tipo varchar(30) NOT NULL,
    activo boolean NOT NULL DEFAULT true
);

CREATE TABLE ventas (
    id uuid PRIMARY KEY,
    caja_id uuid NOT NULL,
    sesion_caja_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    cliente_id uuid,
    convenio_seguro_id uuid,
    linea_credito_id uuid,
    fecha timestamptz NOT NULL,
    estado varchar(20) NOT NULL,
    numero_correlativo bigint
);

CREATE INDEX idx_ventas_fecha ON ventas(fecha);
CREATE INDEX idx_ventas_caja ON ventas(caja_id);
CREATE INDEX idx_ventas_cliente ON ventas(cliente_id);

CREATE TABLE detalles_venta (
    id uuid PRIMARY KEY,
    venta_id uuid NOT NULL REFERENCES ventas(id),
    producto_id uuid NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario numeric(12,2) NOT NULL,
    tasa_impuesto numeric(5,2) NOT NULL,
    promocion_aplicada_id uuid,
    receta_id uuid,
    descuento_monto numeric(12,2) NOT NULL DEFAULT 0
);

CREATE INDEX idx_detalles_venta_venta ON detalles_venta(venta_id);

CREATE TABLE detalle_venta_lotes (
    id uuid PRIMARY KEY,
    detalle_venta_id uuid NOT NULL REFERENCES detalles_venta(id),
    lote_id uuid NOT NULL,
    cantidad_tomada integer NOT NULL
);

CREATE INDEX idx_detalle_venta_lotes_detalle ON detalle_venta_lotes(detalle_venta_id);

CREATE TABLE pagos (
    id uuid PRIMARY KEY,
    venta_id uuid NOT NULL REFERENCES ventas(id),
    forma_pago_id uuid NOT NULL REFERENCES formas_pago(id),
    monto numeric(12,2) NOT NULL,
    codigo_autorizacion varchar(100),
    fecha timestamptz NOT NULL
);

CREATE INDEX idx_pagos_venta ON pagos(venta_id);

CREATE TABLE comprobantes (
    id uuid PRIMARY KEY,
    venta_id uuid NOT NULL UNIQUE REFERENCES ventas(id),
    tipo varchar(20) NOT NULL,
    serie varchar(20) NOT NULL,
    correlativo integer NOT NULL,
    fecha_emision timestamptz NOT NULL
);

-- Semilla de formas de pago (RF12): incluye las dos formas que Ventas registra automaticamente al
-- confirmar (COPAGO_SEGURO, CREDITO_FARMACIA, ver ConfirmarVentaUseCaseImpl) y las de uso habitual
-- en caja, para que el selector de /api/formas-pago del frontend no aparezca vacio.
INSERT INTO formas_pago (id, nombre, tipo, activo) VALUES
    ('00000000-0000-0000-0000-000000000001', 'Efectivo', 'EFECTIVO', true),
    ('00000000-0000-0000-0000-000000000002', 'Tarjeta de debito', 'TARJETA_DEBITO', true),
    ('00000000-0000-0000-0000-000000000003', 'Tarjeta de credito', 'TARJETA_CREDITO', true),
    ('00000000-0000-0000-0000-000000000004', 'Transferencia', 'TRANSFERENCIA', true),
    ('00000000-0000-0000-0000-000000000005', 'Billetera digital', 'BILLETERA_DIGITAL', true),
    ('00000000-0000-0000-0000-000000000006', 'Copago de seguro', 'COPAGO_SEGURO', true),
    ('00000000-0000-0000-0000-000000000007', 'Credito de farmacia', 'CREDITO_FARMACIA', true),
    ('00000000-0000-0000-0000-000000000008', 'Otro', 'OTRO', true);
