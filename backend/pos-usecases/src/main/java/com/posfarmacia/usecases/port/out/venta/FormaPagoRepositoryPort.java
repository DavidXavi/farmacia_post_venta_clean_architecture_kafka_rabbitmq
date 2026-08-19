package com.posfarmacia.usecases.port.out.venta;

import com.posfarmacia.domain.enums.TipoFormaPago;
import com.posfarmacia.domain.model.venta.FormaPago;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida: persistencia del agregado FormaPago (RF12). */
public interface FormaPagoRepositoryPort {

    FormaPago guardar(FormaPago formaPago);

    Optional<FormaPago> buscarPorId(UUID id);

    /** Usado por el contexto de Ventas para registrar automaticamente el copago de seguro o el consumo de credito como pago (RF12). */
    Optional<FormaPago> buscarPorTipo(TipoFormaPago tipo);

    List<FormaPago> listarActivas();
}
