package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.venta.IniciarVentaCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.venta.IniciarVentaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.SesionCajaRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.exception.CajaCerradaException;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.valueobject.Dni;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF05/RN01: inicia una venta, exigiendo que la sesion de caja indicada este abierta. */
public class IniciarVentaUseCaseImpl implements IniciarVentaUseCase {

    private final VentaRepositoryPort ventas;
    private final SesionCajaRepositoryPort sesionesCaja;
    private final ClienteRepositoryPort clientes;
    private final ProductoRepositoryPort productos;
    private final ClockPort clock;

    public IniciarVentaUseCaseImpl(VentaRepositoryPort ventas, SesionCajaRepositoryPort sesionesCaja,
            ClienteRepositoryPort clientes, ProductoRepositoryPort productos, ClockPort clock) {
        this.ventas = ventas;
        this.sesionesCaja = sesionesCaja;
        this.clientes = clientes;
        this.productos = productos;
        this.clock = clock;
    }

    @Override
    @Transactional
    public VentaResult iniciar(IniciarVentaCommand command) {
        SesionCaja sesion = sesionesCaja.buscarSesionActiva(command.cajaId())
                .filter(activa -> activa.getId().equals(command.sesionCajaId()))
                .orElseThrow(CajaCerradaException::new);
        sesion.asegurarAbierta();

        UUID clienteId = null;
        if (command.clienteDni() != null && !command.clienteDni().isBlank()) {
            Cliente cliente = clientes.buscarPorDni(new Dni(command.clienteDni()))
                    .orElseThrow(() -> new EntidadNoEncontradaException("No existe un cliente registrado con ese DNI."));
            clienteId = cliente.getId();
        }

        Venta venta = new Venta(command.cajaId(), command.sesionCajaId(), command.usuarioId(), clienteId, clock.ahora());
        Venta guardada = ventas.guardar(venta);
        return VentaResultMapper.aResultado(guardada, productos);
    }
}
