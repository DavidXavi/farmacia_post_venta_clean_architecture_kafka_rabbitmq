package com.posfarmacia.usecases.port.in.anulacion;

import com.posfarmacia.usecases.dto.anulacion.EmitirNotaCreditoCommand;
import com.posfarmacia.usecases.dto.anulacion.NotaCreditoResult;

/**
 * Puerto de entrada RF16/RN39/RN40: emite una nota de credito sobre una venta confirmada (la via
 * a usar cuando la venta ya no es del mismo dia y por lo tanto no admite anulacion directa, ver
 * {@code com.posfarmacia.usecases.port.in.venta.AnularVentaUseCase}). Tambien revierte el stock
 * despachado a sus lotes originales (RN42/RN43), igual que una anulacion.
 */
public interface EmitirNotaCreditoUseCase {

    NotaCreditoResult emitir(EmitirNotaCreditoCommand command);
}
