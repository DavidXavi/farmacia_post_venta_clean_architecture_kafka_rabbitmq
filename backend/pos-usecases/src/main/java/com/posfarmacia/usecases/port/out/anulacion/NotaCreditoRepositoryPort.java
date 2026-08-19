package com.posfarmacia.usecases.port.out.anulacion;

import com.posfarmacia.domain.model.anulacion.NotaCredito;

/** Puerto de salida: persistencia de la entidad NotaCredito (RF16). */
public interface NotaCreditoRepositoryPort {

    NotaCredito guardar(NotaCredito notaCredito);
}
