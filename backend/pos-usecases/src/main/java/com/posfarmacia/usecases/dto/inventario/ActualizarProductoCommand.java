package com.posfarmacia.usecases.dto.inventario;

import java.math.BigDecimal;

/** Intencion de actualizar los datos comerciales de un producto existente (RF03). */
public record ActualizarProductoCommand(String nombreComercial, String descripcion, BigDecimal precioVenta) {
}
