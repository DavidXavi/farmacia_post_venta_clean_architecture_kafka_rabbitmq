package com.posfarmacia.usecases.dto.venta;

import com.posfarmacia.domain.enums.TipoComprobante;
import java.util.UUID;

/** Entrada de {@code ConfirmarVentaUseCase} (RN01-RN06). */
public record ConfirmarVentaCommand(UUID ventaId, TipoComprobante tipoComprobante, String serieComprobante) {
}
