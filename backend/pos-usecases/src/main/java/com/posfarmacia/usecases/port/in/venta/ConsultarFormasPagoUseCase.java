package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.domain.model.venta.FormaPago;
import java.util.List;

/** Puerto de entrada RF12: lista las formas de pago activas, para que el cajero elija una al registrar un pago. */
public interface ConsultarFormasPagoUseCase {

    List<FormaPago> consultar();
}
