package com.posfarmacia.adapters.web.response.inventario;

import com.posfarmacia.usecases.dto.inventario.StockVendibleResult;
import java.util.List;
import java.util.UUID;

public record StockVendibleResponse(UUID productoId, int cantidadTotalVendible, List<LoteResponse> lotes) {

    public static StockVendibleResponse desde(StockVendibleResult result) {
        return new StockVendibleResponse(
                result.productoId(),
                result.cantidadTotalVendible(),
                result.lotes().stream().map(LoteResponse::desde).toList());
    }
}
