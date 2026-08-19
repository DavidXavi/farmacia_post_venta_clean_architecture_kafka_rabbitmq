-- Seed de datos de prueba: >=10 registros reales por modulo para poder ejercitar el CRUD
-- completo de cada contexto (catalogos, inventario, promociones, recetas, clientes/seguros/
-- credito, ventas, anulaciones, reportes) sin depender de crear todo a mano desde el frontend.
--
-- IMPORTANTE: formas_pago YA fue sembrada por V6__ventas_pagos.sql (8 filas, RF12) -- este
-- archivo NO vuelve a insertarla (la version anterior de este seed lo hacia por error y
-- duplicaba las 7 formas de pago mas comunes con otro UUID; ver docs/ESTADO_MIGRACION.md).
--
-- IDs generados de forma deterministica con uuid5 (namespace fijo + "tabla.n") para que este
-- archivo sea reproducible byte a byte cada vez que se regenera con el script que lo crea.
-- Los IDs de Identidad ya usados en la primera version del seed (locales/cajas/usuarios/roles
-- con prefijo 1111.../2222...) se mantienen intactos para no romper nada que ya los referencie.


-- ============================================================================
-- IDENTIDAD: locales, cajas, usuarios (roles ya sembrados en V1)
-- ============================================================================
INSERT INTO locales (id, nombre, direccion, activo) VALUES
    ('f99e9244-d5a2-58bb-8757-787813be14f3', 'Sede Principal', 'Av. Principal 123, Lima', true),
    ('3314d935-7880-5e41-8bd6-ddd0985e003c', 'Sede Norte', 'Av. Tupac Amaru 456, Los Olivos', true),
    ('e0db1b28-2ae0-5e28-a9b9-13c5bb15a77d', 'Sede Sur', 'Av. Pachacutec 789, Villa El Salvador', true),
    ('eef251fe-7356-56c5-81a9-96ec687e793e', 'Sede Este', 'Av. Nicolas Ayllon 321, Ate', true);
INSERT INTO cajas (id, nombre, local_id, activa) VALUES
    ('5dc4fdd2-e049-51b7-aa34-48f6c3b0b6f5', 'Caja 1', 'f99e9244-d5a2-58bb-8757-787813be14f3', true),
    ('11c25355-4314-51b8-8726-63001d6b88f2', 'Caja 2', 'f99e9244-d5a2-58bb-8757-787813be14f3', true),
    ('99c9fc83-2c09-5098-8e6c-1835a8c04604', 'Caja 3', 'f99e9244-d5a2-58bb-8757-787813be14f3', true),
    ('e139085d-b389-53ba-9a68-a4cd8dab4ce2', 'Caja 1', '3314d935-7880-5e41-8bd6-ddd0985e003c', true),
    ('dbadda87-f847-5379-848a-47622f527da5', 'Caja 2', '3314d935-7880-5e41-8bd6-ddd0985e003c', true),
    ('e72c6616-a1ca-554b-b7e4-43a1ac7d5a23', 'Caja 1', 'e0db1b28-2ae0-5e28-a9b9-13c5bb15a77d', true),
    ('762f7291-be49-52a4-b718-29241a46ba15', 'Caja 2', 'e0db1b28-2ae0-5e28-a9b9-13c5bb15a77d', true),
    ('c8b3de56-7a27-533e-8e6d-9195d5ad2a5e', 'Caja 1', 'eef251fe-7356-56c5-81a9-96ec687e793e', true),
    ('1f820748-bb6f-5070-95e4-0a830dc0b0aa', 'Caja 2', 'eef251fe-7356-56c5-81a9-96ec687e793e', false),
    ('ca2c96c4-6f38-5ad6-aa77-1d1229ab2b8a', 'Caja de respaldo', 'f99e9244-d5a2-58bb-8757-787813be14f3', false);
INSERT INTO usuarios (id, nombre_usuario, password_hash, estado, local_id, permisos) VALUES
    ('cb2ea947-215b-5e31-9864-3b8f7c163a74', 'admin', '$2b$10$NrER4N5SVFEe6Xb1Ij8CdOmctYiDPBnoaCoU1QAUCgDTPEEFK1p86', 'ACTIVO', 'f99e9244-d5a2-58bb-8757-787813be14f3', 'ANULAR_VENTAS,EMITIR_NOTA_CREDITO,VER_AUDITORIA'),
    ('70b442c5-be84-53b6-befe-1fffe647562a', 'cajero1', '$2b$10$AAmYH9C4Qy08gAX5tekmreuZTht4RQrGjpddI96Tl/Jrxv0/qHfzy', 'ACTIVO', 'f99e9244-d5a2-58bb-8757-787813be14f3', ''),
    ('0110c66b-eb7b-55c1-9f8b-304dc50e4430', 'cajero2', '$2b$10$AAmYH9C4Qy08gAX5tekmreuZTht4RQrGjpddI96Tl/Jrxv0/qHfzy', 'ACTIVO', 'f99e9244-d5a2-58bb-8757-787813be14f3', ''),
    ('6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', 'cajero3', '$2b$10$AAmYH9C4Qy08gAX5tekmreuZTht4RQrGjpddI96Tl/Jrxv0/qHfzy', 'ACTIVO', '3314d935-7880-5e41-8bd6-ddd0985e003c', ''),
    ('54677f1b-60d4-5b0f-8b0a-b70744852db5', 'cajero4', '$2b$10$AAmYH9C4Qy08gAX5tekmreuZTht4RQrGjpddI96Tl/Jrxv0/qHfzy', 'SUSPENDIDO', 'e0db1b28-2ae0-5e28-a9b9-13c5bb15a77d', ''),
    ('91663b6b-7c14-5dbb-9525-4eb36a55e37e', 'quimico1', '$2b$10$AAmYH9C4Qy08gAX5tekmreuZTht4RQrGjpddI96Tl/Jrxv0/qHfzy', 'ACTIVO', 'f99e9244-d5a2-58bb-8757-787813be14f3', 'VALIDAR_RECETAS'),
    ('bd10af68-5662-5538-bc1b-13ee06bcf23e', 'quimico2', '$2b$10$AAmYH9C4Qy08gAX5tekmreuZTht4RQrGjpddI96Tl/Jrxv0/qHfzy', 'ACTIVO', '3314d935-7880-5e41-8bd6-ddd0985e003c', 'VALIDAR_RECETAS'),
    ('3c67bcbb-44e5-54b0-af93-1b3319bae6c7', 'inventario1', '$2b$10$AAmYH9C4Qy08gAX5tekmreuZTht4RQrGjpddI96Tl/Jrxv0/qHfzy', 'ACTIVO', 'f99e9244-d5a2-58bb-8757-787813be14f3', 'AJUSTAR_STOCK'),
    ('390070d1-0a7f-55ed-bf44-db1702396fb8', 'central1', '$2b$10$AAmYH9C4Qy08gAX5tekmreuZTht4RQrGjpddI96Tl/Jrxv0/qHfzy', 'ACTIVO', 'eef251fe-7356-56c5-81a9-96ec687e793e', ''),
    ('6b0b6090-f327-5f52-905c-b43f06dadf01', 'admin2', '$2b$10$NrER4N5SVFEe6Xb1Ij8CdOmctYiDPBnoaCoU1QAUCgDTPEEFK1p86', 'ACTIVO', '3314d935-7880-5e41-8bd6-ddd0985e003c', 'ANULAR_VENTAS,EMITIR_NOTA_CREDITO,VER_AUDITORIA');
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES
    ('cb2ea947-215b-5e31-9864-3b8f7c163a74', '11111111-1111-1111-1111-111111111101'),
    ('70b442c5-be84-53b6-befe-1fffe647562a', '11111111-1111-1111-1111-111111111102'),
    ('0110c66b-eb7b-55c1-9f8b-304dc50e4430', '11111111-1111-1111-1111-111111111102'),
    ('6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', '11111111-1111-1111-1111-111111111102'),
    ('54677f1b-60d4-5b0f-8b0a-b70744852db5', '11111111-1111-1111-1111-111111111102'),
    ('91663b6b-7c14-5dbb-9525-4eb36a55e37e', '11111111-1111-1111-1111-111111111103'),
    ('bd10af68-5662-5538-bc1b-13ee06bcf23e', '11111111-1111-1111-1111-111111111103'),
    ('3c67bcbb-44e5-54b0-af93-1b3319bae6c7', '11111111-1111-1111-1111-111111111104'),
    ('390070d1-0a7f-55ed-bf44-db1702396fb8', '11111111-1111-1111-1111-111111111105'),
    ('6b0b6090-f327-5f52-905c-b43f06dadf01', '11111111-1111-1111-1111-111111111101');

-- ============================================================================
-- CATALOGO: categorias, laboratorios, presentaciones, productos
-- ============================================================================
INSERT INTO categorias (id, nombre) VALUES
    ('b726a47e-ca01-5ede-8b91-ba7a84df0372', 'Analgesicos'),
    ('1f4eb2a0-3988-5ec8-b95e-bca7b33f61e3', 'Antibioticos'),
    ('f9909839-4110-5a17-b119-9c7286810e9c', 'Antigripales'),
    ('3cacccde-397e-5935-9d3e-8d9c82a3be21', 'Antialergicos'),
    ('a2b098bc-35c4-55a4-9f4a-d6c3d7a164c6', 'Vitaminas y Suplementos'),
    ('5010ec5f-53fa-53d6-84a5-63df70fd19e8', 'Dermatologicos'),
    ('31aaf8bf-74c6-568b-a7c5-e82a30da1826', 'Gastrointestinales'),
    ('e70bb699-1668-5790-b1a0-4b6e65ddb950', 'Cardiovasculares'),
    ('575d98c4-e8fd-53b4-84e7-99d8dd254f2c', 'Antiinflamatorios'),
    ('0f2cc80d-b21c-5d64-b8d7-9440d9a42a56', 'Cuidado Personal');
INSERT INTO laboratorios (id, nombre) VALUES
    ('f7faedc8-677c-5d41-949b-4eba95ada5a2', 'Laboratorio Generico'),
    ('5266de0f-285f-55f5-a564-1a81b9a3dcd4', 'Bayer'),
    ('006e90bb-5ad0-5d1f-9cce-9c864fdbbf65', 'Pfizer'),
    ('96407a87-6a9c-5f57-8d08-c400e5dff751', 'Genfar'),
    ('98a2c864-a484-59b1-bf9e-5295d7a012ad', 'Farmindustria'),
    ('61a6948b-d4c8-51c3-b4ef-9151d961aeab', 'Roche'),
    ('22af14f4-49a3-53f0-9cb2-43b1e0835c5c', 'Abbott'),
    ('1c40a9fd-4cd3-5a27-b5fa-dcacec736c27', 'MK'),
    ('ea027606-a3e7-511d-ad67-dfc806ff32f1', 'Medifarma'),
    ('ced1d813-e815-509a-8e67-6ca9c14c032d', 'Teva');
INSERT INTO presentaciones (id, nombre, unidad_medida) VALUES
    ('a287e391-d791-56e0-9632-f406e56d5948', 'Tableta x 10', 'Caja'),
    ('d0c03786-54ce-52de-b930-6c63682ab45a', 'Tableta x 20', 'Caja'),
    ('5cdb861a-25dc-5cb8-806c-c9351152537f', 'Jarabe 120ml', 'Frasco'),
    ('6bb17e00-dd51-577b-a221-55e03f35f8c7', 'Ampolla x 1', 'Unidad'),
    ('644b1965-bf79-5570-bca4-1fb9725035f3', 'Crema 30g', 'Tubo'),
    ('7aa0ee64-c39d-5b11-b3f5-7dd4013e580b', 'Capsula x 10', 'Caja'),
    ('d0ade409-9cb4-5f0c-b538-9fee514022f1', 'Suspension 60ml', 'Frasco'),
    ('9f7d5c86-4b6d-5070-b114-c92b741a0519', 'Gotas 15ml', 'Frasco'),
    ('880f1ed2-80a2-5943-b62c-39386b0b7159', 'Sobre x 1', 'Unidad'),
    ('c82bf686-9aee-53ca-a47b-6e2e0b467d9e', 'Frasco 100 tabletas', 'Frasco');
INSERT INTO productos (id, codigo_interno, codigo_barras, nombre_comercial, descripcion, tipo_producto, categoria_id, laboratorio_id, presentacion_id, precio_venta, es_controlado, requiere_receta, tipo_receta_requerida, estado) VALUES
    ('a81f4913-8184-5751-9322-c2f7a1807445', 'P0001', '7750001000019', 'Paracetamol 500mg', 'Analgesico y antipiretico de venta libre', 'OTC', 'b726a47e-ca01-5ede-8b91-ba7a84df0372', 'f7faedc8-677c-5d41-949b-4eba95ada5a2', 'a287e391-d791-56e0-9632-f406e56d5948', 12.5, false, false, null, 'ACTIVO'),
    ('350a61ff-1f06-5f0e-930c-9e8850332b0a', 'P0002', '7750001000026', 'Amoxicilina 500mg', 'Antibiotico de amplio espectro', 'MEDICAMENTO', '1f4eb2a0-3988-5ec8-b95e-bca7b33f61e3', '5266de0f-285f-55f5-a564-1a81b9a3dcd4', '7aa0ee64-c39d-5b11-b3f5-7dd4013e580b', 25.9, false, true, 'NORMAL', 'ACTIVO'),
    ('a203229b-3f3d-51b7-8f7b-beb729aefab0', 'P0003', '7750001000033', 'Loratadina 10mg', 'Antihistaminico para alergias', 'OTC', '3cacccde-397e-5935-9d3e-8d9c82a3be21', '96407a87-6a9c-5f57-8d08-c400e5dff751', 'a287e391-d791-56e0-9632-f406e56d5948', 8.5, false, false, null, 'ACTIVO'),
    ('23ceed9c-b0de-5ac7-8e1c-c72633d47266', 'P0004', '7750001000040', 'Ibuprofeno 400mg', 'Antiinflamatorio no esteroideo', 'OTC', '575d98c4-e8fd-53b4-84e7-99d8dd254f2c', 'f7faedc8-677c-5d41-949b-4eba95ada5a2', 'a287e391-d791-56e0-9632-f406e56d5948', 15.0, false, false, null, 'ACTIVO'),
    ('a1b00829-c7df-5dae-89b5-b66712d99d49', 'P0005', '7750001000057', 'Omeprazol 20mg', 'Inhibidor de la bomba de protones', 'OTC', '31aaf8bf-74c6-568b-a7c5-e82a30da1826', '98a2c864-a484-59b1-bf9e-5295d7a012ad', '7aa0ee64-c39d-5b11-b3f5-7dd4013e580b', 18.9, false, false, null, 'ACTIVO'),
    ('5d2fe11e-e6b8-5c32-970d-e8930d556d18', 'P0006', '7750001000064', 'Losartan 50mg', 'Antihipertensivo', 'MEDICAMENTO', 'e70bb699-1668-5790-b1a0-4b6e65ddb950', '006e90bb-5ad0-5d1f-9cce-9c864fdbbf65', 'a287e391-d791-56e0-9632-f406e56d5948', 22.0, false, true, 'NORMAL', 'ACTIVO'),
    ('b5018120-11e1-59d1-839a-d50f4122ce09', 'P0007', '7750001000071', 'Vitamina C 1g', 'Suplemento vitaminico efervescente', 'OTC', 'a2b098bc-35c4-55a4-9f4a-d6c3d7a164c6', 'ea027606-a3e7-511d-ad67-dfc806ff32f1', '880f1ed2-80a2-5943-b62c-39386b0b7159', 14.5, false, false, null, 'ACTIVO'),
    ('120e971e-060e-518b-91b9-4acff22a00ab', 'P0008', '7750001000088', 'Clonazepam 2mg', 'Ansiolitico, medicamento controlado', 'MEDICAMENTO', '1f4eb2a0-3988-5ec8-b95e-bca7b33f61e3', '61a6948b-d4c8-51c3-b4ef-9151d961aeab', 'a287e391-d791-56e0-9632-f406e56d5948', 9.9, true, true, 'ESPECIAL_RETENIDA', 'ACTIVO'),
    ('e8782adf-f18b-51b7-96ec-6c6f38ddd4dc', 'P0009', '7750001000095', 'Diazepam 10mg', 'Ansiolitico, medicamento controlado', 'MEDICAMENTO', '1f4eb2a0-3988-5ec8-b95e-bca7b33f61e3', '22af14f4-49a3-53f0-9cb2-43b1e0835c5c', 'a287e391-d791-56e0-9632-f406e56d5948', 11.2, true, true, 'ESPECIAL_RETENIDA', 'ACTIVO'),
    ('c387809a-8780-501f-8c81-6d371e9a19bf', 'P0010', '7750001000101', 'Metformina 850mg', 'Antidiabetico oral', 'MEDICAMENTO', 'e70bb699-1668-5790-b1a0-4b6e65ddb950', '1c40a9fd-4cd3-5a27-b5fa-dcacec736c27', 'a287e391-d791-56e0-9632-f406e56d5948', 19.9, false, true, 'NORMAL', 'ACTIVO'),
    ('586a5aa6-3933-5159-987a-8a64762d74d7', 'P0011', '7750001000118', 'Cetirizina 10mg', 'Antihistaminico de segunda generacion', 'OTC', '3cacccde-397e-5935-9d3e-8d9c82a3be21', 'ced1d813-e815-509a-8e67-6ca9c14c032d', 'a287e391-d791-56e0-9632-f406e56d5948', 10.5, false, false, null, 'ACTIVO'),
    ('ce17d1ee-264c-5f12-8c9c-02308fe9fbb1', 'P0012', '7750001000125', 'Salbutamol Inhalador', 'Broncodilatador para crisis asmatica', 'MEDICAMENTO', '575d98c4-e8fd-53b4-84e7-99d8dd254f2c', '5266de0f-285f-55f5-a564-1a81b9a3dcd4', '6bb17e00-dd51-577b-a221-55e03f35f8c7', 32.0, false, true, 'NORMAL', 'SUSPENDIDO');
INSERT INTO lotes (id, codigo, producto_id, fecha_vencimiento, cantidad_recibida, cantidad_disponible, costo, local_id, estado) VALUES
    ('ec22db25-d17d-5335-800e-7466c1d0d709', 'L0001', 'a81f4913-8184-5751-9322-c2f7a1807445', '2026-09-11', 60, 60, 8.0, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('ac3f0e20-61ac-5fe7-a78e-32a0d8742d44', 'L0002', 'a81f4913-8184-5751-9322-c2f7a1807445', '2027-09-01', 100, 100, 8.2, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('74ae3fa5-bc22-5e87-b3a3-94d22c9bc66b', 'L0003', '350a61ff-1f06-5f0e-930c-9e8850332b0a', '2027-05-24', 40, 40, 15.0, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('4d47262a-36f2-50ec-8de1-583c76e0d155', 'L0004', '350a61ff-1f06-5f0e-930c-9e8850332b0a', '2026-08-17', 30, 30, 15.5, '3314d935-7880-5e41-8bd6-ddd0985e003c', 'DISPONIBLE'),
    ('4472e3e9-003f-5f21-a3dd-9e90b0235187', 'L0005', 'a203229b-3f3d-51b7-8f7b-beb729aefab0', '2027-02-13', 80, 80, 5.0, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('4c046251-e60a-5e3c-bbc5-31e03fa418cb', 'L0006', '23ceed9c-b0de-5ac7-8e1c-c72633d47266', '2027-04-04', 70, 70, 9.0, '3314d935-7880-5e41-8bd6-ddd0985e003c', 'DISPONIBLE'),
    ('02b6cd8f-a067-586f-a5d4-7e902cbaca30', 'L0007', 'a1b00829-c7df-5dae-89b5-b66712d99d49', '2027-01-24', 50, 50, 11.0, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('1e2d9416-53e8-50c2-8cc8-3c5283a3fa26', 'L0008', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', '2027-03-05', 45, 45, 13.5, 'e0db1b28-2ae0-5e28-a9b9-13c5bb15a77d', 'DISPONIBLE'),
    ('92e31d84-d764-516d-876e-e2531be07666', 'L0009', 'b5018120-11e1-59d1-839a-d50f4122ce09', '2027-05-24', 90, 90, 8.0, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('9ea735d3-41a9-5eb3-b2c3-4d9a59bce0ee', 'L0010', '120e971e-060e-518b-91b9-4acff22a00ab', '2026-09-26', 20, 20, 5.5, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('88f38cc6-2b93-549f-b97f-c6d0c6d522a7', 'L0011', '120e971e-060e-518b-91b9-4acff22a00ab', '2027-12-10', 30, 30, 5.8, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('2af68e15-9190-5583-b5ec-d1c430b6a14f', 'L0012', 'e8782adf-f18b-51b7-96ec-6c6f38ddd4dc', '2027-09-01', 25, 25, 6.2, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('b004c2ae-44d2-51fb-ae8f-4279b1f3c480', 'L0013', 'c387809a-8780-501f-8c81-6d371e9a19bf', '2026-08-12', 40, 40, 10.5, 'eef251fe-7356-56c5-81a9-96ec687e793e', 'DISPONIBLE'),
    ('a53006f3-5e18-56e0-a7af-a48faf0d537e', 'L0014', 'c387809a-8780-501f-8c81-6d371e9a19bf', '2027-05-24', 60, 60, 10.8, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('153299a9-2688-557f-8c0e-e75c28ac30a7', 'L0015', '586a5aa6-3933-5159-987a-8a64762d74d7', '2027-04-14', 55, 55, 6.0, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'DISPONIBLE'),
    ('74974091-96c1-57ab-a82a-8da6f86f2585', 'L0016', 'ce17d1ee-264c-5f12-8c9c-02308fe9fbb1', '2026-11-05', 15, 0, 20.0, 'f99e9244-d5a2-58bb-8757-787813be14f3', 'BLOQUEADO');
INSERT INTO existencias_lote (id, producto_id, local_id, cantidad_actual, actualizado_en) VALUES
    ('c96414b9-4570-5177-b7a6-b14df441fadc', 'a81f4913-8184-5751-9322-c2f7a1807445', 'f99e9244-d5a2-58bb-8757-787813be14f3', 160, '2026-07-28 08:00:00+00'),
    ('e5da719d-77fd-52ce-88f5-3491edc7ce62', '350a61ff-1f06-5f0e-930c-9e8850332b0a', 'f99e9244-d5a2-58bb-8757-787813be14f3', 40, '2026-07-28 08:00:00+00'),
    ('42fd1304-926a-5c9d-8017-0c35ccb2aec0', '350a61ff-1f06-5f0e-930c-9e8850332b0a', '3314d935-7880-5e41-8bd6-ddd0985e003c', 30, '2026-07-28 08:00:00+00'),
    ('e0a7dbe4-4e26-59de-bf4c-72c15332a04d', 'a203229b-3f3d-51b7-8f7b-beb729aefab0', 'f99e9244-d5a2-58bb-8757-787813be14f3', 80, '2026-07-28 08:00:00+00'),
    ('f67c6aca-8a8a-5c8a-b622-660555723c9a', '23ceed9c-b0de-5ac7-8e1c-c72633d47266', '3314d935-7880-5e41-8bd6-ddd0985e003c', 70, '2026-07-28 08:00:00+00'),
    ('69f4a2cc-30d1-5c69-909d-26e86cf381b5', 'a1b00829-c7df-5dae-89b5-b66712d99d49', 'f99e9244-d5a2-58bb-8757-787813be14f3', 50, '2026-07-28 08:00:00+00'),
    ('408b0376-62a4-5742-bb39-dffacb2ee99a', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', 'e0db1b28-2ae0-5e28-a9b9-13c5bb15a77d', 45, '2026-07-28 08:00:00+00'),
    ('da94a59d-042d-56fb-98f4-98977cc6deee', 'b5018120-11e1-59d1-839a-d50f4122ce09', 'f99e9244-d5a2-58bb-8757-787813be14f3', 90, '2026-07-28 08:00:00+00'),
    ('7c1ed413-6606-59c2-b16a-5c3f16c83ed4', '120e971e-060e-518b-91b9-4acff22a00ab', 'f99e9244-d5a2-58bb-8757-787813be14f3', 50, '2026-07-28 08:00:00+00'),
    ('39b81e7d-72c0-52af-b113-79f1d4f965c3', 'e8782adf-f18b-51b7-96ec-6c6f38ddd4dc', 'f99e9244-d5a2-58bb-8757-787813be14f3', 25, '2026-07-28 08:00:00+00'),
    ('1ffb4b96-7be4-5d50-a0de-3e94b5c7370a', 'c387809a-8780-501f-8c81-6d371e9a19bf', 'eef251fe-7356-56c5-81a9-96ec687e793e', 40, '2026-07-28 08:00:00+00'),
    ('41419ecc-1bc7-5104-8591-4e78aac33944', 'c387809a-8780-501f-8c81-6d371e9a19bf', 'f99e9244-d5a2-58bb-8757-787813be14f3', 60, '2026-07-28 08:00:00+00'),
    ('d71d1ee1-6041-551e-99bb-f186e7be9035', '586a5aa6-3933-5159-987a-8a64762d74d7', 'f99e9244-d5a2-58bb-8757-787813be14f3', 55, '2026-07-28 08:00:00+00'),
    ('1acb139f-7c7e-50f9-a4bd-a34eba6e4dfe', 'ce17d1ee-264c-5f12-8c9c-02308fe9fbb1', 'f99e9244-d5a2-58bb-8757-787813be14f3', 0, '2026-07-28 08:00:00+00');

-- ============================================================================
-- CLIENTES, CONVENIOS DE SEGURO, COBERTURAS, AFILIACIONES, LINEAS DE CREDITO
-- ============================================================================
INSERT INTO clientes (id, dni, nombres, apellidos, fecha_nacimiento, telefono, correo, direccion, estado) VALUES
    ('5730402c-8a8a-5313-9342-3d3c323841bb', '45678912', 'Juan Carlos', 'Perez Gomez', '1985-03-14', '987654321', 'jperez@example.com', 'Jr. Union 100, Lima', 'ACTIVO'),
    ('a6552978-7e8c-51d4-8b74-5a6718fe3987', '41234567', 'Maria Fernanda', 'Lopez Torres', '1990-07-22', '987654322', 'mlopez@example.com', 'Av. Arequipa 200, Lima', 'ACTIVO'),
    ('9a9fdbb4-5788-533d-9f61-70009063cabb', '42345678', 'Carlos Alberto', 'Ramirez Silva', '1978-11-05', '987654323', 'cramirez@example.com', 'Jr. Cusco 300, Lima', 'ACTIVO'),
    ('64edcc36-30ab-5369-973e-5ff70f94bd2f', '43456789', 'Ana Lucia', 'Fernandez Vega', '1995-01-30', '987654324', 'afernandez@example.com', 'Av. Brasil 400, Lima', 'ACTIVO'),
    ('770d8af1-6a03-5d45-9c2d-931bf5016044', '44567890', 'Luis Miguel', 'Torres Castro', '1982-09-18', '987654325', 'ltorres@example.com', 'Jr. Ica 500, Lima', 'ACTIVO'),
    ('6bc54e9a-6cc2-54a8-ac3f-428a6c4750d4', '46789123', 'Rosa Elena', 'Vasquez Mendoza', '1970-05-02', '987654326', 'rvasquez@example.com', 'Av. Salaverry 600, Lima', 'ACTIVO'),
    ('6a29a8bf-b7ff-55f4-aab6-d69ff8a7d243', '47891234', 'Jorge Luis', 'Huaman Quispe', '1988-12-25', '987654327', 'jhuaman@example.com', 'Jr. Puno 700, Lima', 'ACTIVO'),
    ('45b69675-01ec-554f-a046-52a380b10995', '48912345', 'Patricia Isabel', 'Rojas Diaz', '1993-04-11', '987654328', 'projas@example.com', 'Av. Colonial 800, Lima', 'ACTIVO'),
    ('acf795dc-15d3-52ce-b5ab-e81f6fbe50df', '49123456', 'Miguel Angel', 'Sanchez Flores', '1965-08-08', '987654329', 'msanchez@example.com', 'Jr. Tacna 900, Lima', 'INACTIVO'),
    ('8a0c1d3f-b9d3-5879-90df-e0b90d5bfc3e', '40123456', 'Carmen Rosa', 'Chavez Paredes', '1975-02-19', '987654330', 'cchavez@example.com', 'Av. Grau 1000, Lima', 'INACTIVO');
INSERT INTO convenios_seguro (id, nombre, activo) VALUES
    ('812a400b-b984-514a-b4a0-041d0ec0d0d1', 'Rimac Salud', true),
    ('ee4d11a9-f8b0-5eb9-9d37-bd8e8b7262db', 'Pacifico Seguros', true),
    ('70594cb0-7bda-5460-8450-389b0e31dee5', 'Mapfre Salud', true),
    ('60b77870-3c7d-5185-88c6-e04c2980d0ae', 'La Positiva Salud', true),
    ('b382c530-cd23-55f0-8f2d-7ec65fdc0c24', 'Sanitas Peru', true),
    ('610a6e70-ec40-574f-bab7-227cb77e053c', 'EsSalud Convenio', true),
    ('8a019b0d-88b6-5496-872a-fbef56dfdc06', 'Interseguro Salud', true),
    ('f2ad1e50-4e9f-5ded-8bcb-903f7999a886', 'Protecta Salud', false),
    ('8d4d3eb5-8366-54cb-bf40-d427796bd7a9', 'Sanna Convenio', true),
    ('d68c726a-64e0-5b83-8d2c-e2accaf4641e', 'Auna Salud', false);
INSERT INTO coberturas_seguro (id, convenio_id, producto_id, porcentaje_cubierto) VALUES
    ('5498bbaa-128d-58cb-959f-bb2ba6494b0e', '812a400b-b984-514a-b4a0-041d0ec0d0d1', '350a61ff-1f06-5f0e-930c-9e8850332b0a', 70.0),
    ('aafa02d4-7828-5e48-9587-72a7e5e1c6dd', '812a400b-b984-514a-b4a0-041d0ec0d0d1', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', 60.0),
    ('7fb9547b-e782-5f66-83d1-4cf97c74d8a8', 'ee4d11a9-f8b0-5eb9-9d37-bd8e8b7262db', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', 80.0),
    ('eada7056-a414-532e-b393-2de7256e6230', 'ee4d11a9-f8b0-5eb9-9d37-bd8e8b7262db', 'c387809a-8780-501f-8c81-6d371e9a19bf', 65.0),
    ('72c64713-497a-5535-b8ea-5d85b3252508', '70594cb0-7bda-5460-8450-389b0e31dee5', 'a81f4913-8184-5751-9322-c2f7a1807445', 50.0),
    ('3e64b8da-59c9-506a-b727-5ccf027f3f18', '60b77870-3c7d-5185-88c6-e04c2980d0ae', '23ceed9c-b0de-5ac7-8e1c-c72633d47266', 55.0),
    ('f59fec31-3db1-52c3-804a-90e174d18257', 'b382c530-cd23-55f0-8f2d-7ec65fdc0c24', 'a1b00829-c7df-5dae-89b5-b66712d99d49', 60.0),
    ('990a4026-d674-5e90-811c-e08cd79d3a50', '610a6e70-ec40-574f-bab7-227cb77e053c', 'b5018120-11e1-59d1-839a-d50f4122ce09', 40.0),
    ('7310d800-c439-5d17-96c3-f225ef8c453d', '8a019b0d-88b6-5496-872a-fbef56dfdc06', '586a5aa6-3933-5159-987a-8a64762d74d7', 45.0),
    ('e759397f-ed8c-5213-b394-d0d81cc8ee1d', '8d4d3eb5-8366-54cb-bf40-d427796bd7a9', 'c387809a-8780-501f-8c81-6d371e9a19bf', 75.0);
INSERT INTO afiliaciones_cliente (id, cliente_id, convenio_id, vigencia_inicio, vigencia_fin, estado) VALUES
    ('fc2db497-1408-51a1-9cb2-97e3d0e032c8', '5730402c-8a8a-5313-9342-3d3c323841bb', '812a400b-b984-514a-b4a0-041d0ec0d0d1', '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('a0cf7e23-a73d-500b-9973-d5d9551f562a', 'a6552978-7e8c-51d4-8b74-5a6718fe3987', 'ee4d11a9-f8b0-5eb9-9d37-bd8e8b7262db', '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('83545160-afb5-5aeb-8803-e4f9356f5b39', '9a9fdbb4-5788-533d-9f61-70009063cabb', '70594cb0-7bda-5460-8450-389b0e31dee5', '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('8782f00a-62ea-5097-8ad4-2fb53a91e502', '64edcc36-30ab-5369-973e-5ff70f94bd2f', '60b77870-3c7d-5185-88c6-e04c2980d0ae', '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('61fae9ab-03eb-5f35-b786-7c1b6311f4f2', '770d8af1-6a03-5d45-9c2d-931bf5016044', 'b382c530-cd23-55f0-8f2d-7ec65fdc0c24', '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('b48f99d9-1208-5c17-a018-a1e91540e13e', '6bc54e9a-6cc2-54a8-ac3f-428a6c4750d4', '610a6e70-ec40-574f-bab7-227cb77e053c', '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('2295c791-ba31-5fd2-9587-7f6d7e5876ea', '6a29a8bf-b7ff-55f4-aab6-d69ff8a7d243', '8a019b0d-88b6-5496-872a-fbef56dfdc06', '2026-01-09', '2027-05-24', 'SUSPENDIDA'),
    ('dbf76d4d-29a6-530d-9925-0002a9baec21', '45b69675-01ec-554f-a046-52a380b10995', 'f2ad1e50-4e9f-5ded-8bcb-903f7999a886', '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('7d017d43-53bb-53d8-8052-c3dc527a4213', 'acf795dc-15d3-52ce-b5ab-e81f6fbe50df', '8d4d3eb5-8366-54cb-bf40-d427796bd7a9', '2026-01-09', '2027-05-24', 'INACTIVA'),
    ('98000948-85b1-53b5-9984-d7bb94254f0f', '8a0c1d3f-b9d3-5879-90df-e0b90d5bfc3e', 'd68c726a-64e0-5b83-8d2c-e2accaf4641e', '2026-01-09', '2027-05-24', 'INACTIVA');
INSERT INTO lineas_credito (id, cliente_id, monto_autorizado, saldo_disponible, vigencia_inicio, vigencia_fin, estado) VALUES
    ('cd8e63d0-864e-5eee-9f5a-87bb4eabcac1', '5730402c-8a8a-5313-9342-3d3c323841bb', 500.0, 500.0, '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('9ecaa8dc-e832-5ad6-9e60-af087434fcdc', 'a6552978-7e8c-51d4-8b74-5a6718fe3987', 800.0, 620.0, '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('92f41840-bf04-5d04-83de-d6cd32dcf491', '9a9fdbb4-5788-533d-9f61-70009063cabb', 300.0, 300.0, '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('ac179c5d-ae47-5595-a3fd-ae218960e1da', '64edcc36-30ab-5369-973e-5ff70f94bd2f', 1000.0, 750.0, '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('81d9e5ab-9bce-5d09-a3a7-898d278a2ebc', '770d8af1-6a03-5d45-9c2d-931bf5016044', 600.0, 587.61, '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('c4886b60-4a3c-5888-a4f3-93e2065136d1', '6bc54e9a-6cc2-54a8-ac3f-428a6c4750d4', 400.0, 400.0, '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('7fc3bfb4-b353-570b-b7a8-9422536987a8', '6a29a8bf-b7ff-55f4-aab6-d69ff8a7d243', 700.0, 250.0, '2026-01-09', '2027-05-24', 'ACTIVA'),
    ('358dfc95-01ef-5f9a-a73a-a28cb80ebeb9', '45b69675-01ec-554f-a046-52a380b10995', 250.0, 250.0, '2026-01-09', '2027-05-24', 'BLOQUEADA'),
    ('25cf4d14-7ea6-5a42-8914-b59c323c4e3f', 'acf795dc-15d3-52ce-b5ab-e81f6fbe50df', 900.0, 900.0, '2026-01-09', '2027-05-24', 'INACTIVA'),
    ('eb3e60c6-d022-5f72-a06a-24bd5f0a38f3', '8a0c1d3f-b9d3-5879-90df-e0b90d5bfc3e', 350.0, 350.0, '2026-01-09', '2027-05-24', 'ACTIVA');

-- ============================================================================
-- PROMOCIONES + CONDICIONES (producto por promocion)
-- ============================================================================
INSERT INTO promociones (id, nombre, descripcion, tipo_beneficio, valor_beneficio, requiere_cliente, cantidad_minima, vigencia_inicio, vigencia_fin, activa) VALUES
    ('4be4d8ef-c0ad-53d4-9c16-c9d3ec60be96', 'Descuento 10% Paracetamol', '10% de descuento en Paracetamol 500mg', 'DESCUENTO_PORCENTAJE', 10.0, false, 1, '2026-06-28', '2026-09-26', true),
    ('e630542d-e22d-5a4b-a714-36a39cbd8f25', 'Descuento S/5 Ibuprofeno', 'S/5 de descuento fijo en Ibuprofeno 400mg', 'DESCUENTO_MONTO', 5.0, false, 1, '2026-06-28', '2026-09-26', true),
    ('6d9c5d64-ba0d-56cf-8f8c-26288338db67', 'Lleva 3 paga 2 Vitamina C', 'En la compra de 3 unidades, la de menor valor es gratis', 'LLEVA_N_PAGA_M', 1.0, false, 3, '2026-07-13', '2026-09-11', true),
    ('f487a9ba-8aa1-503d-9f09-ea3607141901', 'Fidelidad clientes 15% Loratadina', '15% de descuento para clientes registrados', 'DESCUENTO_PORCENTAJE', 15.0, true, 1, '2026-06-28', '2026-10-26', true),
    ('5f7024d4-5753-5de9-8ef6-65302b69aef3', 'Promo verano vencida', 'Descuento de temporada ya vencido', 'DESCUENTO_PORCENTAJE', 20.0, false, 1, '2026-03-30', '2026-06-28', false),
    ('f3d85a94-89d4-5596-9111-6e7a13ce20f2', 'Promo Dia de la Madre', 'Descuento programado para una fecha futura', 'DESCUENTO_MONTO', 3.0, false, 1, '2026-08-27', '2026-09-11', true),
    ('5620f463-3e18-567a-8762-a23dbb1c6e3e', 'Descuento Omeprazol 8%', '8% de descuento por 2 o mas unidades', 'DESCUENTO_PORCENTAJE', 8.0, false, 2, '2026-07-18', '2026-10-16', true),
    ('b8ee45d8-8295-5b08-a7c5-c021dd46f243', '2x1 Cetirizina', 'Lleva 2 y paga 1', 'LLEVA_N_PAGA_M', 1.0, false, 2, '2026-07-08', '2026-10-06', true),
    ('90642f3a-01b9-55ea-b689-22d1b0bba311', 'Descuento Losartan adultos mayores', '12% de descuento para clientes registrados', 'DESCUENTO_PORCENTAJE', 12.0, true, 1, '2026-06-28', '2026-10-26', true),
    ('cd0fc2ab-5c96-5cf0-826e-ea254df4fbab', 'Promo desactivada manualmente', 'Se desactivo antes de que expirara su vigencia', 'DESCUENTO_MONTO', 2.0, false, 1, '2026-07-18', '2026-09-16', false);
INSERT INTO promocion_condiciones (id, promocion_id, producto_id) VALUES
    ('a915f48c-35e0-51ab-9314-29d1d4951cb3', '4be4d8ef-c0ad-53d4-9c16-c9d3ec60be96', 'a81f4913-8184-5751-9322-c2f7a1807445'),
    ('ff41f8bf-db00-5e62-b665-e41cad52ae58', 'e630542d-e22d-5a4b-a714-36a39cbd8f25', '23ceed9c-b0de-5ac7-8e1c-c72633d47266'),
    ('fe4afde2-ea97-5fb7-9002-1288fd752ec1', '6d9c5d64-ba0d-56cf-8f8c-26288338db67', 'b5018120-11e1-59d1-839a-d50f4122ce09'),
    ('e7969db5-63a1-5ec0-ad7a-19d55f7328b9', 'f487a9ba-8aa1-503d-9f09-ea3607141901', 'a203229b-3f3d-51b7-8f7b-beb729aefab0'),
    ('572ad0e7-b6f3-55ae-ae6e-525792a99111', '5f7024d4-5753-5de9-8ef6-65302b69aef3', 'a81f4913-8184-5751-9322-c2f7a1807445'),
    ('9a8dc8df-3240-52f8-8d65-435762e0034f', 'f3d85a94-89d4-5596-9111-6e7a13ce20f2', 'b5018120-11e1-59d1-839a-d50f4122ce09'),
    ('81c4a283-08d7-58c4-81c9-49d670d8f5e8', '5620f463-3e18-567a-8762-a23dbb1c6e3e', 'a1b00829-c7df-5dae-89b5-b66712d99d49'),
    ('d7236ebe-778e-5996-9be0-032329d0ca84', 'b8ee45d8-8295-5b08-a7c5-c021dd46f243', '586a5aa6-3933-5159-987a-8a64762d74d7'),
    ('d3c4957f-bbcc-58b6-9cb1-6fb9d80c306f', '90642f3a-01b9-55ea-b689-22d1b0bba311', '5d2fe11e-e6b8-5c32-970d-e8930d556d18'),
    ('afa2ea9b-c14d-5071-a41c-b1732336dfc6', 'cd0fc2ab-5c96-5cf0-826e-ea254df4fbab', '23ceed9c-b0de-5ac7-8e1c-c72633d47266');

-- ============================================================================
-- RECETAS
-- ============================================================================
INSERT INTO recetas (id, numero, tipo, fecha_emision, fecha_vencimiento, producto_id, cliente_id, datos_paciente, datos_profesional, dosis, cantidad_autorizada, archivo_respaldo_url, estado, retenida_en_botica, version) VALUES
    ('66d4284a-1a11-5d7e-ae9a-035ed5295ff5', 'REC-0001', 'NORMAL', '2026-07-23', '2026-08-22', '350a61ff-1f06-5f0e-930c-9e8850332b0a', '5730402c-8a8a-5313-9342-3d3c323841bb', 'Paciente atendido en consulta REC-0001', 'Dr. Fernando Castillo Rios - CMP 45612', '500mg cada 8 horas por 7 dias', 21, null, 'APROBADA', false, 0),
    ('21ac9c25-b130-5041-a056-2af940812823', 'REC-0002', 'NORMAL', '2026-07-25', '2026-08-24', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', '9a9fdbb4-5788-533d-9f61-70009063cabb', 'Paciente atendido en consulta REC-0002', 'Dr. Fernando Castillo Rios - CMP 45612', '50mg cada 24 horas', 30, null, 'APROBADA', false, 0),
    ('fb19a897-24be-5fde-bb2a-44db107a8c17', 'REC-0003', 'NORMAL', '2026-07-27', '2026-08-26', 'c387809a-8780-501f-8c81-6d371e9a19bf', '6a29a8bf-b7ff-55f4-aab6-d69ff8a7d243', 'Paciente atendido en consulta REC-0003', 'Dr. Fernando Castillo Rios - CMP 45612', '850mg cada 12 horas', 60, null, 'APROBADA', false, 0),
    ('a68a1f1f-e681-52b7-a345-8d8151d09b02', 'REC-0004', 'ESPECIAL', '2026-07-18', '2026-07-27', 'ce17d1ee-264c-5f12-8c9c-02308fe9fbb1', '64edcc36-30ab-5369-973e-5ff70f94bd2f', 'Paciente atendido en consulta REC-0004', 'Dr. Fernando Castillo Rios - CMP 45612', '2 inhalaciones cada 6 horas segun necesidad', 1, null, 'APROBADA', false, 0),
    ('4a8f9f8b-a69a-576b-8fdf-889964799b3c', 'REC-0005', 'ESPECIAL_RETENIDA', '2026-07-20', '2026-08-19', '120e971e-060e-518b-91b9-4acff22a00ab', '64edcc36-30ab-5369-973e-5ff70f94bd2f', 'Paciente atendido en consulta REC-0005', 'Dr. Fernando Castillo Rios - CMP 45612', '2mg cada 12 horas por 15 dias', 30, null, 'UTILIZADA', true, 0),
    ('67b3ca86-3ebd-56ea-a250-95dafe2a86a4', 'REC-0006', 'ESPECIAL_RETENIDA', '2026-07-22', '2026-08-21', 'e8782adf-f18b-51b7-96ec-6c6f38ddd4dc', '45b69675-01ec-554f-a046-52a380b10995', 'Paciente atendido en consulta REC-0006', 'Dr. Fernando Castillo Rios - CMP 45612', '10mg cada 24 horas por 10 dias', 10, null, 'UTILIZADA', true, 0),
    ('0966a998-9582-5247-9da6-78b132aeb645', 'REC-0007', 'ESPECIAL_RETENIDA', '2026-07-26', '2026-08-25', '120e971e-060e-518b-91b9-4acff22a00ab', null, 'Paciente atendido en consulta REC-0007', 'Dr. Fernando Castillo Rios - CMP 45612', '2mg cada 12 horas', 20, null, 'PENDIENTE', false, 0),
    ('346d3683-9e6e-5c93-8ea0-648b9d5870cc', 'REC-0008', 'NORMAL', '2026-07-13', '2026-08-12', '350a61ff-1f06-5f0e-930c-9e8850332b0a', null, 'Paciente atendido en consulta REC-0008', 'Dr. Fernando Castillo Rios - CMP 45612', '500mg cada 8 horas', 21, null, 'RECHAZADA', false, 0),
    ('756b4df5-5531-5831-bac9-360de6b66136', 'REC-0009', 'NORMAL', '2026-07-24', '2026-08-23', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', '6bc54e9a-6cc2-54a8-ac3f-428a6c4750d4', 'Paciente atendido en consulta REC-0009', 'Dr. Fernando Castillo Rios - CMP 45612', '50mg cada 24 horas por 30 dias', 30, null, 'APROBADA', false, 0),
    ('b3111fa0-832c-5d67-9252-1185f051f594', 'REC-0010', 'ESPECIAL', '2026-07-27', '2026-08-27', 'c387809a-8780-501f-8c81-6d371e9a19bf', 'a6552978-7e8c-51d4-8b74-5a6718fe3987', 'Paciente atendido en consulta REC-0010', 'Dr. Fernando Castillo Rios - CMP 45612', '850mg cada 12 horas por 60 dias', 60, null, 'PENDIENTE', false, 0);

-- ============================================================================
-- VENTAS: sesiones de caja historicas, ventas, detalles, pagos, comprobantes
-- ============================================================================
INSERT INTO sesiones_caja (id, caja_id, usuario_id, fecha_apertura, monto_inicial, fecha_cierre, monto_esperado, monto_declarado, diferencia, observacion_cierre, estado) VALUES
    ('23f811a1-cbe9-5a7b-93a1-1650fb6f032e', '5dc4fdd2-e049-51b7-aa34-48f6c3b0b6f5', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', '2026-07-18 08:00:00+00', 100.0, '2026-07-18 20:00:00+00', '129.50', '129.50', '0.00', 'Cierre de turno sin novedad', 'CERRADA'),
    ('f20d9ceb-b59e-538a-86cd-89f845d78c8d', '11c25355-4314-51b8-8726-63001d6b88f2', '70b442c5-be84-53b6-befe-1fffe647562a', '2026-07-20 08:00:00+00', 100.0, '2026-07-20 20:00:00+00', '130.56', '130.56', '0.00', 'Cierre de turno sin novedad', 'CERRADA'),
    ('870c0715-9467-537f-a223-78df8fa7646a', 'e139085d-b389-53ba-9a68-a4cd8dab4ce2', '6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', '2026-07-22 08:00:00+00', 100.0, '2026-07-22 20:00:00+00', '111.68', '111.68', '0.00', 'Cierre de turno sin novedad', 'CERRADA'),
    ('2a95a15b-9a2c-5580-a03d-b26a4d7c25ed', 'e72c6616-a1ca-554b-b7e4-43a1ac7d5a23', '0110c66b-eb7b-55c1-9f8b-304dc50e4430', '2026-07-24 08:00:00+00', 100.0, '2026-07-24 20:00:00+00', '113.22', '113.22', '0.00', 'Cierre de turno sin novedad', 'CERRADA'),
    ('418e8036-44a1-5736-95ff-5caebcb16130', 'c8b3de56-7a27-533e-8e6d-9195d5ad2a5e', '390070d1-0a7f-55ed-bf44-db1702396fb8', '2026-07-26 08:00:00+00', 100.0, '2026-07-26 20:00:00+00', '100.00', '100.00', '0.00', 'Cierre de turno sin novedad', 'CERRADA');
INSERT INTO ventas (id, caja_id, sesion_caja_id, usuario_id, cliente_id, convenio_seguro_id, linea_credito_id, fecha, estado, numero_correlativo) VALUES
    ('8f82d1fc-945f-5869-80a1-4dc2eb4d8b83', '5dc4fdd2-e049-51b7-aa34-48f6c3b0b6f5', '23f811a1-cbe9-5a7b-93a1-1650fb6f032e', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', null, null, null, '2026-07-18 11:00:00+00', 'CONFIRMADA', 1),
    ('80630ced-8f45-5664-afee-bfd87178411b', '5dc4fdd2-e049-51b7-aa34-48f6c3b0b6f5', '23f811a1-cbe9-5a7b-93a1-1650fb6f032e', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', null, null, null, '2026-07-18 11:00:00+00', 'CONFIRMADA', 2),
    ('79ac7823-44f6-5db4-be19-b55cdb436694', '11c25355-4314-51b8-8726-63001d6b88f2', 'f20d9ceb-b59e-538a-86cd-89f845d78c8d', '70b442c5-be84-53b6-befe-1fffe647562a', '5730402c-8a8a-5313-9342-3d3c323841bb', null, null, '2026-07-20 11:00:00+00', 'CONFIRMADA', 3),
    ('c2726692-927a-5bdd-8f56-4ee70ef2ccdb', '11c25355-4314-51b8-8726-63001d6b88f2', 'f20d9ceb-b59e-538a-86cd-89f845d78c8d', '70b442c5-be84-53b6-befe-1fffe647562a', null, null, null, '2026-07-20 11:00:00+00', 'CONFIRMADA', 4),
    ('c615ef40-7276-5f66-94cb-79d307b4890a', 'e139085d-b389-53ba-9a68-a4cd8dab4ce2', '870c0715-9467-537f-a223-78df8fa7646a', '6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', '64edcc36-30ab-5369-973e-5ff70f94bd2f', null, null, '2026-07-22 11:00:00+00', 'CONFIRMADA', 5),
    ('320ee597-b801-5d10-8059-30b7ed403118', 'e139085d-b389-53ba-9a68-a4cd8dab4ce2', '870c0715-9467-537f-a223-78df8fa7646a', '6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', '9a9fdbb4-5788-533d-9f61-70009063cabb', '812a400b-b984-514a-b4a0-041d0ec0d0d1', null, '2026-07-22 11:00:00+00', 'CONFIRMADA', 6),
    ('b9a5a97e-ecdb-5cc9-9dee-25e535d80b31', 'e72c6616-a1ca-554b-b7e4-43a1ac7d5a23', '2a95a15b-9a2c-5580-a03d-b26a4d7c25ed', '0110c66b-eb7b-55c1-9f8b-304dc50e4430', '6a29a8bf-b7ff-55f4-aab6-d69ff8a7d243', null, null, '2026-07-24 11:00:00+00', 'CONFIRMADA', 7),
    ('d4b72821-e19f-5591-af34-9f0e01cec4ec', 'e72c6616-a1ca-554b-b7e4-43a1ac7d5a23', '2a95a15b-9a2c-5580-a03d-b26a4d7c25ed', '0110c66b-eb7b-55c1-9f8b-304dc50e4430', '45b69675-01ec-554f-a046-52a380b10995', null, null, '2026-07-24 11:00:00+00', 'CONFIRMADA', 8),
    ('0064b946-33e2-52eb-955d-9fe64ed1ff78', 'c8b3de56-7a27-533e-8e6d-9195d5ad2a5e', '418e8036-44a1-5736-95ff-5caebcb16130', '390070d1-0a7f-55ed-bf44-db1702396fb8', '6bc54e9a-6cc2-54a8-ac3f-428a6c4750d4', null, null, '2026-07-26 11:00:00+00', 'CONFIRMADA', 9),
    ('155d3d62-cc80-5868-a490-2ad665b084dc', 'c8b3de56-7a27-533e-8e6d-9195d5ad2a5e', '418e8036-44a1-5736-95ff-5caebcb16130', '390070d1-0a7f-55ed-bf44-db1702396fb8', '770d8af1-6a03-5d45-9c2d-931bf5016044', null, '81d9e5ab-9bce-5d09-a3a7-898d278a2ebc', '2026-07-26 11:00:00+00', 'CONFIRMADA', 10);
INSERT INTO detalles_venta (id, venta_id, producto_id, cantidad, precio_unitario, tasa_impuesto, promocion_aplicada_id, receta_id, descuento_monto) VALUES
    ('3833110c-b2d6-5e6c-8acd-ac9911c11e37', '8f82d1fc-945f-5869-80a1-4dc2eb4d8b83', 'a81f4913-8184-5751-9322-c2f7a1807445', 2, 12.5, '18', null, null, '0'),
    ('939e1772-efb6-5785-8ebe-eaa22d9ee2b2', '80630ced-8f45-5664-afee-bfd87178411b', 'b5018120-11e1-59d1-839a-d50f4122ce09', 3, 14.5, '18', '6d9c5d64-ba0d-56cf-8f8c-26288338db67', null, '14.50'),
    ('daab77f7-6059-5d75-9eff-19d841285179', '79ac7823-44f6-5db4-be19-b55cdb436694', '350a61ff-1f06-5f0e-930c-9e8850332b0a', 1, 25.9, '18', null, '66d4284a-1a11-5d7e-ae9a-035ed5295ff5', '0'),
    ('412c05bb-f302-54b6-9dcc-7fcb80401b66', 'c2726692-927a-5bdd-8f56-4ee70ef2ccdb', '23ceed9c-b0de-5ac7-8e1c-c72633d47266', 2, 15.0, '18', 'e630542d-e22d-5a4b-a714-36a39cbd8f25', null, '5.00'),
    ('2448a568-28a9-5b4f-b4f1-2297910fa889', 'c615ef40-7276-5f66-94cb-79d307b4890a', '120e971e-060e-518b-91b9-4acff22a00ab', 1, 9.9, '18', null, '4a8f9f8b-a69a-576b-8fdf-889964799b3c', '0'),
    ('a79c5f86-1bf3-5025-a0b1-8b967a2621f2', '320ee597-b801-5d10-8059-30b7ed403118', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', 1, 22.0, '18', null, '21ac9c25-b130-5041-a056-2af940812823', '0'),
    ('712f7ea4-ef9d-55c0-8cad-7140d06660a1', 'b9a5a97e-ecdb-5cc9-9dee-25e535d80b31', 'c387809a-8780-501f-8c81-6d371e9a19bf', 2, 19.9, '18', null, 'fb19a897-24be-5fde-bb2a-44db107a8c17', '0'),
    ('bd07b798-7978-56de-b851-a7257f1d7d77', 'd4b72821-e19f-5591-af34-9f0e01cec4ec', 'e8782adf-f18b-51b7-96ec-6c6f38ddd4dc', 1, 11.2, '18', null, '67b3ca86-3ebd-56ea-a250-95dafe2a86a4', '0'),
    ('7d873eb3-017a-5d2c-ad33-dad76930bd70', '0064b946-33e2-52eb-955d-9fe64ed1ff78', 'a203229b-3f3d-51b7-8f7b-beb729aefab0', 2, 8.5, '18', 'f487a9ba-8aa1-503d-9f09-ea3607141901', null, '2.55'),
    ('20e83b3a-e642-54a6-bc29-f93d4d7fba4b', '155d3d62-cc80-5868-a490-2ad665b084dc', '586a5aa6-3933-5159-987a-8a64762d74d7', 2, 10.5, '18', 'b8ee45d8-8295-5b08-a7c5-c021dd46f243', null, '10.50');
INSERT INTO detalle_venta_lotes (id, detalle_venta_id, lote_id, cantidad_tomada) VALUES
    ('5d63ad92-db11-566d-ba20-bf0325787b5c', '3833110c-b2d6-5e6c-8acd-ac9911c11e37', 'ec22db25-d17d-5335-800e-7466c1d0d709', 2),
    ('13eb77de-2358-500f-b15d-357b51f7d6f1', '939e1772-efb6-5785-8ebe-eaa22d9ee2b2', '92e31d84-d764-516d-876e-e2531be07666', 3),
    ('48050caf-7cfe-5db9-9ce8-94468d719540', 'daab77f7-6059-5d75-9eff-19d841285179', '74ae3fa5-bc22-5e87-b3a3-94d22c9bc66b', 1),
    ('7398e3f3-6f95-5ba8-aad8-ec12d03f0a0a', '412c05bb-f302-54b6-9dcc-7fcb80401b66', '4c046251-e60a-5e3c-bbc5-31e03fa418cb', 2),
    ('75e3c975-b212-508d-a807-beda1273ee9e', '2448a568-28a9-5b4f-b4f1-2297910fa889', '9ea735d3-41a9-5eb3-b2c3-4d9a59bce0ee', 1),
    ('be79a586-fc78-5862-9767-1e78e3b7418b', 'a79c5f86-1bf3-5025-a0b1-8b967a2621f2', '1e2d9416-53e8-50c2-8cc8-3c5283a3fa26', 1),
    ('56d9fa9f-fb13-51b4-a49a-29f5d9a594a5', '712f7ea4-ef9d-55c0-8cad-7140d06660a1', 'b004c2ae-44d2-51fb-ae8f-4279b1f3c480', 2),
    ('65181788-7836-5203-b7d9-027cf52bce34', 'bd07b798-7978-56de-b851-a7257f1d7d77', '2af68e15-9190-5583-b5ec-d1c430b6a14f', 1),
    ('4543e6be-b828-5b8f-969c-5f95ad3991c1', '7d873eb3-017a-5d2c-ad33-dad76930bd70', '4472e3e9-003f-5f21-a3dd-9e90b0235187', 2),
    ('c4f373ba-d081-5dbc-ad50-ddc8b147c93f', '20e83b3a-e642-54a6-bc29-f93d4d7fba4b', '153299a9-2688-557f-8c0e-e75c28ac30a7', 2);
INSERT INTO pagos (id, venta_id, forma_pago_id, monto, codigo_autorizacion, fecha) VALUES
    ('c95e97bb-d4ec-5c22-932d-ecf066beeed8', '8f82d1fc-945f-5869-80a1-4dc2eb4d8b83', '00000000-0000-0000-0000-000000000001', '29.50', null, '2026-07-18 11:02:00+00'),
    ('e64e418e-fbda-53de-87da-184981ada305', '80630ced-8f45-5664-afee-bfd87178411b', '00000000-0000-0000-0000-000000000002', '34.22', null, '2026-07-18 11:02:00+00'),
    ('6238ccb8-f8a4-595f-ab3a-e34821bc2373', '79ac7823-44f6-5db4-be19-b55cdb436694', '00000000-0000-0000-0000-000000000001', '30.56', null, '2026-07-20 11:02:00+00'),
    ('c6be1c39-17f0-56d0-a98a-0c6e23589004', 'c2726692-927a-5bdd-8f56-4ee70ef2ccdb', '00000000-0000-0000-0000-000000000004', '29.50', null, '2026-07-20 11:02:00+00'),
    ('7702ea93-39ed-5aaf-a471-b12eed58089d', 'c615ef40-7276-5f66-94cb-79d307b4890a', '00000000-0000-0000-0000-000000000001', '11.68', null, '2026-07-22 11:02:00+00'),
    ('dc53060b-4643-5e51-97e8-804951588bed', '320ee597-b801-5d10-8059-30b7ed403118', '00000000-0000-0000-0000-000000000006', '25.96', null, '2026-07-22 11:02:00+00'),
    ('a2d0a566-f221-5981-a3a2-e5ec4cf12b84', 'b9a5a97e-ecdb-5cc9-9dee-25e535d80b31', '00000000-0000-0000-0000-000000000003', '46.96', null, '2026-07-24 11:02:00+00'),
    ('1948a00b-a8b1-58ac-8fcc-444146c6b611', 'd4b72821-e19f-5591-af34-9f0e01cec4ec', '00000000-0000-0000-0000-000000000001', '13.22', null, '2026-07-24 11:02:00+00'),
    ('920eea3c-932d-51d5-9ebb-97b64fc9a962', '0064b946-33e2-52eb-955d-9fe64ed1ff78', '00000000-0000-0000-0000-000000000005', '17.05', null, '2026-07-26 11:02:00+00'),
    ('2b1a7a22-9464-5bef-91a7-62ff6bffca5d', '155d3d62-cc80-5868-a490-2ad665b084dc', '00000000-0000-0000-0000-000000000007', '12.39', null, '2026-07-26 11:02:00+00');
INSERT INTO comprobantes (id, venta_id, tipo, serie, correlativo, fecha_emision) VALUES
    ('8964f061-af25-598d-88b5-08b254cf3e28', '8f82d1fc-945f-5869-80a1-4dc2eb4d8b83', 'BOLETA', 'B001', 1, '2026-07-18 11:03:00+00'),
    ('99f6a4e0-3b33-5692-9cfb-76fac6689a4f', '80630ced-8f45-5664-afee-bfd87178411b', 'BOLETA', 'B001', 2, '2026-07-18 11:03:00+00'),
    ('ec8e0c0a-d6c6-5561-953d-d318af54df38', '79ac7823-44f6-5db4-be19-b55cdb436694', 'BOLETA', 'B001', 3, '2026-07-20 11:03:00+00'),
    ('20205102-ecc0-50d8-bb74-bfa09c67bb52', 'c2726692-927a-5bdd-8f56-4ee70ef2ccdb', 'BOLETA', 'B001', 4, '2026-07-20 11:03:00+00'),
    ('3e069e6d-5ab9-5e02-b646-baed2a58f476', 'c615ef40-7276-5f66-94cb-79d307b4890a', 'BOLETA', 'B001', 5, '2026-07-22 11:03:00+00'),
    ('6d88a45d-1c0e-50b9-91e4-1597db7e8acb', '320ee597-b801-5d10-8059-30b7ed403118', 'BOLETA', 'B001', 6, '2026-07-22 11:03:00+00'),
    ('0929b10d-e816-5a7b-a7e9-f4e9eace17c9', 'b9a5a97e-ecdb-5cc9-9dee-25e535d80b31', 'FACTURA', 'F001', 1, '2026-07-24 11:03:00+00'),
    ('6e1f6c79-f633-577c-b2ac-f16b48eabd5a', 'd4b72821-e19f-5591-af34-9f0e01cec4ec', 'BOLETA', 'B001', 7, '2026-07-24 11:03:00+00'),
    ('cfb33adf-aaf6-56c8-ac50-5d72c88b4837', '0064b946-33e2-52eb-955d-9fe64ed1ff78', 'BOLETA', 'B001', 8, '2026-07-26 11:03:00+00'),
    ('9bb4222a-b6a3-55bc-b9ee-ca7bdf0a3e7c', '155d3d62-cc80-5868-a490-2ad665b084dc', 'BOLETA', 'B001', 9, '2026-07-26 11:03:00+00');
INSERT INTO movimientos_credito (id, linea_credito_id, venta_id, tipo, monto, fecha) VALUES
    ('95011532-8356-50a5-b8f6-99bf83d753dd', '81d9e5ab-9bce-5d09-a3a7-898d278a2ebc', '155d3d62-cc80-5868-a490-2ad665b084dc', 'CONSUMO', '12.39', '2026-07-26 11:05:00+00');

-- ============================================================================
-- DEVOLUCIONES Y NOTAS DE CREDITO (una por cada venta, cantidad parcial)
-- ============================================================================
INSERT INTO devoluciones (id, venta_id, usuario_id, motivo, fecha) VALUES
    ('94199053-63a5-5633-9d9c-7c2038e92964', '8f82d1fc-945f-5869-80a1-4dc2eb4d8b83', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', 'Cliente reporto reaccion alergica leve', '2026-07-19 15:00:00+00'),
    ('cbc21614-b7da-5431-912f-176af9238985', '80630ced-8f45-5664-afee-bfd87178411b', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', 'Producto con empaque danado', '2026-07-19 15:00:00+00'),
    ('dd73c0b8-ae80-5328-b952-32f2f10774a4', '79ac7823-44f6-5db4-be19-b55cdb436694', '70b442c5-be84-53b6-befe-1fffe647562a', 'Cliente se equivoco de presentacion', '2026-07-21 15:00:00+00'),
    ('56335b84-b75a-56c0-9f3a-a72de8b927c0', 'c2726692-927a-5bdd-8f56-4ee70ef2ccdb', '70b442c5-be84-53b6-befe-1fffe647562a', 'Cambio de indicacion medica', '2026-07-21 15:00:00+00'),
    ('8594474b-912f-5a36-87e6-a20fd7b79201', 'c615ef40-7276-5f66-94cb-79d307b4890a', '6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', 'Cliente ya no lo necesita', '2026-07-23 15:00:00+00'),
    ('edf4e71a-ed92-5302-915f-7115dd54186c', '320ee597-b801-5d10-8059-30b7ed403118', '6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', 'Producto proximo a vencer detectado en mostrador', '2026-07-23 15:00:00+00'),
    ('647d2d02-b786-565a-9afc-e66998be2805', 'b9a5a97e-ecdb-5cc9-9dee-25e535d80b31', '0110c66b-eb7b-55c1-9f8b-304dc50e4430', 'Error de digitacion en la venta original', '2026-07-25 15:00:00+00'),
    ('615c5f64-cfc6-588d-9546-8787d2ba9b73', 'd4b72821-e19f-5591-af34-9f0e01cec4ec', '0110c66b-eb7b-55c1-9f8b-304dc50e4430', 'Cliente solicito otro laboratorio', '2026-07-25 15:00:00+00'),
    ('b79bb6f3-bf10-5889-af10-3779c732a6df', '0064b946-33e2-52eb-955d-9fe64ed1ff78', '390070d1-0a7f-55ed-bf44-db1702396fb8', 'Devolucion por garantia comercial', '2026-07-27 15:00:00+00'),
    ('632846a0-f8ab-5414-affe-3a9572100701', '155d3d62-cc80-5868-a490-2ad665b084dc', '390070d1-0a7f-55ed-bf44-db1702396fb8', 'Cliente devolvio por duplicidad de compra', '2026-07-27 15:00:00+00');
INSERT INTO detalle_devoluciones (id, devolucion_id, detalle_venta_id, producto_id, cantidad, monto_devuelto) VALUES
    ('41df5992-d778-5a81-a803-b200834c1367', '94199053-63a5-5633-9d9c-7c2038e92964', '3833110c-b2d6-5e6c-8acd-ac9911c11e37', 'a81f4913-8184-5751-9322-c2f7a1807445', 1, '12.50'),
    ('a1abccc5-1230-52f9-a405-d2d520ad2431', 'cbc21614-b7da-5431-912f-176af9238985', '939e1772-efb6-5785-8ebe-eaa22d9ee2b2', 'b5018120-11e1-59d1-839a-d50f4122ce09', 1, '14.50'),
    ('7617b76c-1863-5681-9fd7-c5b27bba6b02', 'dd73c0b8-ae80-5328-b952-32f2f10774a4', 'daab77f7-6059-5d75-9eff-19d841285179', '350a61ff-1f06-5f0e-930c-9e8850332b0a', 1, '25.90'),
    ('8cecc910-c6f0-54d5-83f9-48ca9a92e0db', '56335b84-b75a-56c0-9f3a-a72de8b927c0', '412c05bb-f302-54b6-9dcc-7fcb80401b66', '23ceed9c-b0de-5ac7-8e1c-c72633d47266', 1, '15.00'),
    ('b5df4971-bce0-5a61-bf52-faffa8fd9bb7', '8594474b-912f-5a36-87e6-a20fd7b79201', '2448a568-28a9-5b4f-b4f1-2297910fa889', '120e971e-060e-518b-91b9-4acff22a00ab', 1, '9.90'),
    ('6b884d77-7671-56c1-acc6-43596daf94cb', 'edf4e71a-ed92-5302-915f-7115dd54186c', 'a79c5f86-1bf3-5025-a0b1-8b967a2621f2', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', 1, '22.00'),
    ('931aa4f7-9f01-5db0-9843-a748ba6a9383', '647d2d02-b786-565a-9afc-e66998be2805', '712f7ea4-ef9d-55c0-8cad-7140d06660a1', 'c387809a-8780-501f-8c81-6d371e9a19bf', 1, '19.90'),
    ('9ed1c9c8-93b9-5d87-ad97-93176fafebe5', '615c5f64-cfc6-588d-9546-8787d2ba9b73', 'bd07b798-7978-56de-b851-a7257f1d7d77', 'e8782adf-f18b-51b7-96ec-6c6f38ddd4dc', 1, '11.20'),
    ('711779ee-e892-5c7b-a183-2576ef837bd4', 'b79bb6f3-bf10-5889-af10-3779c732a6df', '7d873eb3-017a-5d2c-ad33-dad76930bd70', 'a203229b-3f3d-51b7-8f7b-beb729aefab0', 1, '8.50'),
    ('6211e562-2b49-5168-b35b-f96c45d03569', '632846a0-f8ab-5414-affe-3a9572100701', '20e83b3a-e642-54a6-bc29-f93d4d7fba4b', '586a5aa6-3933-5159-987a-8a64762d74d7', 1, '10.50');
INSERT INTO notas_credito (id, venta_id, comprobante_id, usuario_id, motivo, monto_total, fecha) VALUES
    ('293151d8-f075-5356-acf1-63ae5c9e8376', '8f82d1fc-945f-5869-80a1-4dc2eb4d8b83', '8964f061-af25-598d-88b5-08b254cf3e28', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', 'Nota de credito por devolucion parcial: Cliente reporto reaccion alergica leve', '12.50', '2026-07-19 15:10:00+00'),
    ('9bba86cf-c31d-5b6c-9f47-674da2ddd4d0', '80630ced-8f45-5664-afee-bfd87178411b', '99f6a4e0-3b33-5692-9cfb-76fac6689a4f', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', 'Nota de credito por devolucion parcial: Producto con empaque danado', '14.50', '2026-07-19 15:10:00+00'),
    ('087b7c0f-4043-50a6-a003-bb6971b3c84d', '79ac7823-44f6-5db4-be19-b55cdb436694', 'ec8e0c0a-d6c6-5561-953d-d318af54df38', '70b442c5-be84-53b6-befe-1fffe647562a', 'Nota de credito por devolucion parcial: Cliente se equivoco de presentacion', '25.90', '2026-07-21 15:10:00+00'),
    ('c6c318df-71aa-54ca-af01-dfd0e2041f63', 'c2726692-927a-5bdd-8f56-4ee70ef2ccdb', '20205102-ecc0-50d8-bb74-bfa09c67bb52', '70b442c5-be84-53b6-befe-1fffe647562a', 'Nota de credito por devolucion parcial: Cambio de indicacion medica', '15.00', '2026-07-21 15:10:00+00'),
    ('cdbdf847-7c1c-5eb7-bebc-4e72f2177561', 'c615ef40-7276-5f66-94cb-79d307b4890a', '3e069e6d-5ab9-5e02-b646-baed2a58f476', '6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', 'Nota de credito por devolucion parcial: Cliente ya no lo necesita', '9.90', '2026-07-23 15:10:00+00'),
    ('9b6d273d-2398-5e31-9499-b1479e617d0b', '320ee597-b801-5d10-8059-30b7ed403118', '6d88a45d-1c0e-50b9-91e4-1597db7e8acb', '6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', 'Nota de credito por devolucion parcial: Producto proximo a vencer detectado en mostrador', '22.00', '2026-07-23 15:10:00+00'),
    ('556f7890-7ef1-5184-8504-35d00c0eaf78', 'b9a5a97e-ecdb-5cc9-9dee-25e535d80b31', '0929b10d-e816-5a7b-a7e9-f4e9eace17c9', '0110c66b-eb7b-55c1-9f8b-304dc50e4430', 'Nota de credito por devolucion parcial: Error de digitacion en la venta original', '19.90', '2026-07-25 15:10:00+00'),
    ('971ccae0-c691-5378-a55a-a1f428af8a42', 'd4b72821-e19f-5591-af34-9f0e01cec4ec', '6e1f6c79-f633-577c-b2ac-f16b48eabd5a', '0110c66b-eb7b-55c1-9f8b-304dc50e4430', 'Nota de credito por devolucion parcial: Cliente solicito otro laboratorio', '11.20', '2026-07-25 15:10:00+00'),
    ('d32b83dc-2db0-5273-b970-8d02c6769268', '0064b946-33e2-52eb-955d-9fe64ed1ff78', 'cfb33adf-aaf6-56c8-ac50-5d72c88b4837', '390070d1-0a7f-55ed-bf44-db1702396fb8', 'Nota de credito por devolucion parcial: Devolucion por garantia comercial', '8.50', '2026-07-27 15:10:00+00'),
    ('2fc6d8d4-b9f5-5096-a2cf-52d2a2135cf6', '155d3d62-cc80-5868-a490-2ad665b084dc', '9bb4222a-b6a3-55bc-b9ee-ca7bdf0a3e7c', '390070d1-0a7f-55ed-bf44-db1702396fb8', 'Nota de credito por devolucion parcial: Cliente devolvio por duplicidad de compra', '10.50', '2026-07-27 15:10:00+00');

-- ============================================================================
-- REGLAS DE INCENTIVO E INCENTIVOS DE VENTA
-- ============================================================================
INSERT INTO reglas_incentivo (id, nombre, producto_id, categoria_id, monto_por_unidad, vigencia_inicio, vigencia_fin, activa) VALUES
    ('5d5d7015-8cf4-559c-a7a1-f6de7b092083', 'Incentivo Paracetamol', 'a81f4913-8184-5751-9322-c2f7a1807445', null, 0.5, '2026-05-29', '2026-11-25', true),
    ('5c17052b-f4d6-5c01-83a5-6a19f5d87084', 'Incentivo linea Antibioticos', null, '1f4eb2a0-3988-5ec8-b95e-bca7b33f61e3', 1.2, '2026-05-29', '2026-11-25', true),
    ('9a769762-aca2-5e7b-b9de-e213f388eef6', 'Incentivo Vitamina C', 'b5018120-11e1-59d1-839a-d50f4122ce09', null, 0.8, '2026-05-29', '2026-11-25', true),
    ('1bdf8c0b-3953-5c2d-af44-efdd6a23debc', 'Incentivo linea Cardiovascular', null, 'e70bb699-1668-5790-b1a0-4b6e65ddb950', 1.0, '2026-05-29', '2026-11-25', true),
    ('0afcd54c-36ba-56f8-bad8-3f24dd81634a', 'Incentivo Losartan', '5d2fe11e-e6b8-5c32-970d-e8930d556d18', null, 1.5, '2026-06-28', '2026-10-26', true),
    ('bea4bb91-3618-58bb-94d7-7485d9c3fa0c', 'Incentivo campana antigripal', null, 'f9909839-4110-5a17-b119-9c7286810e9c', 0.6, '2026-04-29', '2026-07-18', false),
    ('77724e0b-e54f-588c-95fd-20b81dc98554', 'Incentivo Metformina', 'c387809a-8780-501f-8c81-6d371e9a19bf', null, 1.1, '2026-06-28', '2026-10-26', true),
    ('a603835a-3f0b-5611-9ca9-91d4097f17dd', 'Incentivo linea Dermatologicos', null, '5010ec5f-53fa-53d6-84a5-63df70fd19e8', 0.9, '2026-06-28', '2026-10-26', true),
    ('83f951fe-1431-52f6-8a33-870e2a8b92e8', 'Incentivo Cetirizina', '586a5aa6-3933-5159-987a-8a64762d74d7', null, 0.7, '2026-06-28', '2026-10-26', true),
    ('87ff7320-b325-5ac5-b0fa-05a664f985de', 'Incentivo general OTC fin de mes', null, null, 0.3, '2026-07-23', '2026-08-22', true);
INSERT INTO incentivos_venta (id, regla_incentivo_id, usuario_id, venta_id, detalle_venta_id, cantidad, monto_calculado, fecha) VALUES
    ('926e781e-b0e1-5ef9-a1d1-f9322fa935ae', '5d5d7015-8cf4-559c-a7a1-f6de7b092083', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', '8f82d1fc-945f-5869-80a1-4dc2eb4d8b83', '3833110c-b2d6-5e6c-8acd-ac9911c11e37', 2, '1.00', '2026-07-18 11:04:00+00'),
    ('c31f84aa-d04e-53e9-b6b1-6ab958333e84', '9a769762-aca2-5e7b-b9de-e213f388eef6', 'cb2ea947-215b-5e31-9864-3b8f7c163a74', '80630ced-8f45-5664-afee-bfd87178411b', '939e1772-efb6-5785-8ebe-eaa22d9ee2b2', 3, '2.40', '2026-07-18 11:04:00+00'),
    ('3c274403-2ad2-54b2-9d22-c84e8d5f1332', '5c17052b-f4d6-5c01-83a5-6a19f5d87084', '70b442c5-be84-53b6-befe-1fffe647562a', '79ac7823-44f6-5db4-be19-b55cdb436694', 'daab77f7-6059-5d75-9eff-19d841285179', 1, '1.20', '2026-07-20 11:04:00+00'),
    ('d6c3e831-7da6-51c7-9560-006391b1431f', '0afcd54c-36ba-56f8-bad8-3f24dd81634a', '6dd4f5c5-c20e-5ba7-b5db-092b5c6f7cb2', '320ee597-b801-5d10-8059-30b7ed403118', 'a79c5f86-1bf3-5025-a0b1-8b967a2621f2', 1, '1.50', '2026-07-22 11:04:00+00'),
    ('1d2d59b0-a97d-5373-9cb9-268c63d626d3', '77724e0b-e54f-588c-95fd-20b81dc98554', '0110c66b-eb7b-55c1-9f8b-304dc50e4430', 'b9a5a97e-ecdb-5cc9-9dee-25e535d80b31', '712f7ea4-ef9d-55c0-8cad-7140d06660a1', 2, '2.20', '2026-07-24 11:04:00+00'),
    ('5369ccf6-3665-5ba3-b6d0-99aeb7efd7fc', '83f951fe-1431-52f6-8a33-870e2a8b92e8', '390070d1-0a7f-55ed-bf44-db1702396fb8', '155d3d62-cc80-5868-a490-2ad665b084dc', '20e83b3a-e642-54a6-bc29-f93d4d7fba4b', 2, '1.40', '2026-07-26 11:04:00+00');
