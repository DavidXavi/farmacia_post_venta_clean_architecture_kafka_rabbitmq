package com.posfarmacia.domain.enums;

/** Se representa como {@code EnumSet<PermisoEspecial>} en vez del bitmask [Flags] de .NET. */
public enum PermisoEspecial {
    ANULAR_VENTAS,
    VALIDAR_RECETAS,
    AJUSTAR_STOCK,
    VER_AUDITORIA,
    EMITIR_NOTA_CREDITO
}
