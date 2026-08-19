package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.port.in.identidad.CerrarCajaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.identidad.SesionCajaRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.FormaPagoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.enums.EstadoVenta;
import com.posfarmacia.domain.enums.TipoFormaPago;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.model.venta.FormaPago;
import com.posfarmacia.domain.model.venta.Pago;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.valueobject.Dinero;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

/**
 * RF02: cierre de turno de caja, calculando la diferencia entre lo esperado y lo declarado.
 *
 * <p>El monto esperado se calcula sumando el monto inicial del turno y los ingresos en efectivo
 * de las ventas confirmadas de esa sesion de caja, igual que
 * {@code PosFarmacia.Application.UseCases.CerrarCajaUseCase} (.NET). Este caso de uso depende de
 * {@code VentaRepositoryPort}/{@code FormaPagoRepositoryPort} (contexto de Ventas) solo para
 * lectura, siguiendo el mismo patron ya usado por los contextos de Anulaciones y Reportes.</p>
 */
public class CerrarCajaUseCaseImpl implements CerrarCajaUseCase {

    private final SesionCajaRepositoryPort sesiones;
    private final VentaRepositoryPort ventas;
    private final FormaPagoRepositoryPort formasPago;
    private final ClockPort clock;

    public CerrarCajaUseCaseImpl(SesionCajaRepositoryPort sesiones, VentaRepositoryPort ventas,
            FormaPagoRepositoryPort formasPago, ClockPort clock) {
        this.sesiones = sesiones;
        this.ventas = ventas;
        this.formasPago = formasPago;
        this.clock = clock;
    }

    @Override
    @Transactional
    public SesionCaja cerrar(UUID cajaId, Dinero montoDeclarado, String observacion) {
        SesionCaja sesion = sesiones.buscarSesionActiva(cajaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("La caja no tiene una sesion abierta."));

        Dinero montoEsperado = calcularMontoEsperado(sesion);
        sesion.cerrar(montoEsperado, montoDeclarado, observacion, clock.ahora());
        return sesiones.guardar(sesion);
    }

    private Dinero calcularMontoEsperado(SesionCaja sesion) {
        Set<UUID> idsEfectivo = formasPago.listarActivas().stream()
                .filter(forma -> forma.getTipo() == TipoFormaPago.EFECTIVO)
                .map(FormaPago::getId)
                .collect(Collectors.toSet());

        Dinero ingresosEfectivo = ventas.buscar(null, sesion.getCajaId(), null, null).stream()
                .filter(venta -> sesion.getId().equals(venta.getSesionCajaId()))
                .filter(venta -> venta.getEstado() == EstadoVenta.CONFIRMADA)
                .flatMap(venta -> venta.getPagos().stream())
                .filter(pago -> idsEfectivo.contains(pago.getFormaPagoId()))
                .map(Pago::getMonto)
                .reduce(Dinero.CERO, Dinero::sumar);

        return sesion.getMontoInicial().sumar(ingresosEfectivo);
    }
}
