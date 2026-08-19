package com.posfarmacia.usecases.usecase.anulacion;

import com.posfarmacia.usecases.dto.anulacion.DevolucionResult;
import com.posfarmacia.usecases.port.in.anulacion.ConsultarDevolucionesUseCase;
import com.posfarmacia.usecases.port.out.anulacion.DevolucionRepositoryPort;
import java.util.List;
import java.util.UUID;

/** Caso de uso RF16: lista las devoluciones registradas sobre una venta. */
public class ConsultarDevolucionesUseCaseImpl implements ConsultarDevolucionesUseCase {

    private final DevolucionRepositoryPort devoluciones;

    public ConsultarDevolucionesUseCaseImpl(DevolucionRepositoryPort devoluciones) {
        this.devoluciones = devoluciones;
    }

    @Override
    public List<DevolucionResult> consultarPorVenta(UUID ventaId) {
        return devoluciones.buscarPorVenta(ventaId).stream().map(DevolucionResultMapper::aResultado).toList();
    }
}
