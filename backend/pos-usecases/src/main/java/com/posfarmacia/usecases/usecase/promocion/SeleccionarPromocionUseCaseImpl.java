package com.posfarmacia.usecases.usecase.promocion;

import com.posfarmacia.usecases.dto.promocion.SeleccionarPromocionCommand;
import com.posfarmacia.usecases.port.in.promocion.SeleccionarPromocionUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.promocion.PromocionRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.promocion.AplicacionPromocion;
import com.posfarmacia.domain.model.promocion.Promocion;
import com.posfarmacia.domain.service.promocion.DatosProductoPromocion;
import com.posfarmacia.domain.service.promocion.EvaluadorPromociones;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RN07-RN12: registra la promocion que el cajero eligio para una linea de venta.
 * Carga la promocion por id (puerto de salida), delega toda la validacion de negocio en
 * {@link EvaluadorPromociones#validarSeleccion} (RN09 incluida) y devuelve el registro de
 * aplicacion resultante para que el contexto de Ventas lo asocie a su comprobante.
 */
public class SeleccionarPromocionUseCaseImpl implements SeleccionarPromocionUseCase {

    private final PromocionRepositoryPort promociones;
    private final ClockPort clock;
    private final EvaluadorPromociones evaluador;

    public SeleccionarPromocionUseCaseImpl(PromocionRepositoryPort promociones, ClockPort clock, EvaluadorPromociones evaluador) {
        this.promociones = promociones;
        this.clock = clock;
        this.evaluador = evaluador;
    }

    @Override
    @Transactional(readOnly = true)
    public AplicacionPromocion seleccionar(SeleccionarPromocionCommand command) {
        Promocion promocion = promociones.buscarPorId(command.promocionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La promocion indicada no existe."));

        LocalDate hoy = clock.hoy();
        DatosProductoPromocion datos = new DatosProductoPromocion(
                command.productoId(), new Cantidad(command.cantidad()), command.clienteIdentificado());

        evaluador.validarSeleccion(promocion, datos, hoy, command.promocionesYaAplicadasEnComprobante());

        Dinero descuento = promocion.calcularDescuento(new Dinero(command.precioUnitario()), datos.cantidad());
        return AplicacionPromocion.registrar(command.ventaId(), command.detalleVentaId(), promocion.getId(), descuento);
    }
}
