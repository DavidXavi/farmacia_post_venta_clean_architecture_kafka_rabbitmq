package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.venta.IdentificarClienteCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.venta.IdentificarClienteEnVentaUseCase;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.valueobject.Dni;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF09: identifica al cliente de una venta por su DNI. */
public class IdentificarClienteEnVentaUseCaseImpl implements IdentificarClienteEnVentaUseCase {

    private final VentaRepositoryPort ventas;
    private final ClienteRepositoryPort clientes;
    private final ProductoRepositoryPort productos;

    public IdentificarClienteEnVentaUseCaseImpl(VentaRepositoryPort ventas, ClienteRepositoryPort clientes,
            ProductoRepositoryPort productos) {
        this.ventas = ventas;
        this.clientes = clientes;
        this.productos = productos;
    }

    @Override
    @Transactional
    public VentaResult identificar(IdentificarClienteCommand command) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, command.ventaId());
        Cliente cliente = clientes.buscarPorDni(new Dni(command.dni()))
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe un cliente registrado con ese DNI."));

        venta.identificarCliente(cliente.getId());

        Venta guardada = ventas.guardar(venta);
        return VentaResultMapper.aResultado(guardada, productos);
    }
}
