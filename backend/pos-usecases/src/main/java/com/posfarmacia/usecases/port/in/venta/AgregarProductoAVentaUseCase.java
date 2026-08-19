package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.usecases.dto.venta.AgregarProductoCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import java.util.UUID;

/** Puerto de entrada RF05: agrega un producto a una venta en proceso. */
public interface AgregarProductoAVentaUseCase {

    VentaResult agregar(UUID ventaId, AgregarProductoCommand command);
}
