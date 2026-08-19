package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.venta.RegistrarPagoCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.venta.RegistrarPagoUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.FormaPagoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.valueobject.Dinero;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF12: registra un pago del cliente sobre una venta en proceso. */
public class RegistrarPagoUseCaseImpl implements RegistrarPagoUseCase {

    private final VentaRepositoryPort ventas;
    private final FormaPagoRepositoryPort formasPago;
    private final ProductoRepositoryPort productos;
    private final ClockPort clock;

    public RegistrarPagoUseCaseImpl(VentaRepositoryPort ventas, FormaPagoRepositoryPort formasPago,
            ProductoRepositoryPort productos, ClockPort clock) {
        this.ventas = ventas;
        this.formasPago = formasPago;
        this.productos = productos;
        this.clock = clock;
    }

    @Override
    @Transactional
    public VentaResult registrar(RegistrarPagoCommand command) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, command.ventaId());
        formasPago.buscarPorId(command.formaPagoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La forma de pago indicada no existe."));

        venta.registrarPago(command.formaPagoId(), new Dinero(command.monto()), command.codigoAutorizacion(),
                clock.ahora());

        Venta guardada = ventas.guardar(venta);
        return VentaResultMapper.aResultado(guardada, productos);
    }
}
