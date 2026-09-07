-- RF01: login con proveedores sociales (Google, Facebook) y segundo factor TOTP
-- (Google Authenticator). No cambia ninguna regla de negocio existente: solo agrega
-- los datos de identidad que los dos flujos nuevos necesitan.

ALTER TABLE usuarios
    ADD COLUMN email           VARCHAR(255) UNIQUE,
    ADD COLUMN proveedor_oauth VARCHAR(30),
    ADD COLUMN mfa_secret      VARCHAR(64),
    ADD COLUMN mfa_habilitado  BOOLEAN NOT NULL DEFAULT FALSE;

-- Una cuenta creada desde Google/Facebook no tiene contrasena local.
ALTER TABLE usuarios ALTER COLUMN password_hash DROP NOT NULL;
