package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.usecases.dto.venta.VentaResult;
import java.util.UUID;

/** Puerto de entrada RF05: consulta una venta por id. */
public interface ObtenerVentaUseCase {

    VentaResult obtener(UUID ventaId);
}
