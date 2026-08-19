package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.port.in.venta.ConsultarFormasPagoUseCase;
import com.posfarmacia.usecases.port.out.venta.FormaPagoRepositoryPort;
import com.posfarmacia.domain.model.venta.FormaPago;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF12: lista las formas de pago activas. */
public class ConsultarFormasPagoUseCaseImpl implements ConsultarFormasPagoUseCase {

    private final FormaPagoRepositoryPort formasPago;

    public ConsultarFormasPagoUseCaseImpl(FormaPagoRepositoryPort formasPago) {
        this.formasPago = formasPago;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormaPago> consultar() {
        return formasPago.listarActivas();
    }
}
