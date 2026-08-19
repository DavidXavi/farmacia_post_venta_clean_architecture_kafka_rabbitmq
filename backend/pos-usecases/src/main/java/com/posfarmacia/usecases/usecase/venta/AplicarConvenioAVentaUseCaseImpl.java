package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.seguro.CalcularCopagoCommand;
import com.posfarmacia.usecases.dto.venta.AplicarConvenioCommand;
import com.posfarmacia.usecases.dto.venta.CopagoResult;
import com.posfarmacia.usecases.port.in.seguro.CalcularCopagoUseCase;
import com.posfarmacia.usecases.port.in.venta.AplicarConvenioAVentaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.seguro.AfiliacionClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.seguro.ConvenioSeguroRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.exception.ConvenioNoDisponibleException;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.seguro.AfiliacionCliente;
import com.posfarmacia.domain.model.seguro.ConvenioSeguro;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.service.seguro.ResultadoCopago;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RF10/RN22-RN26: asocia el convenio a la venta y devuelve una vista previa del
 * copago sumando la cobertura linea por linea (RN24). El copago real se vuelve a calcular al
 * confirmar la venta (RN04, ver {@code ConfirmarVentaUseCaseImpl}); esta vista previa nunca se
 * usa para decidir cuanto paga el cliente.
 */
public class AplicarConvenioAVentaUseCaseImpl implements AplicarConvenioAVentaUseCase {

    private final VentaRepositoryPort ventas;
    private final ConvenioSeguroRepositoryPort convenios;
    private final AfiliacionClienteRepositoryPort afiliaciones;
    private final CalcularCopagoUseCase calcularCopago;
    private final ClockPort clock;

    public AplicarConvenioAVentaUseCaseImpl(VentaRepositoryPort ventas, ConvenioSeguroRepositoryPort convenios,
            AfiliacionClienteRepositoryPort afiliaciones, CalcularCopagoUseCase calcularCopago, ClockPort clock) {
        this.ventas = ventas;
        this.convenios = convenios;
        this.afiliaciones = afiliaciones;
        this.calcularCopago = calcularCopago;
        this.clock = clock;
    }

    @Override
    @Transactional
    public CopagoResult aplicar(AplicarConvenioCommand command) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, command.ventaId());
        if (venta.getClienteId() == null) {
            throw new ConvenioNoDisponibleException("Debe identificarse al cliente antes de aplicar un convenio de seguro.");
        }

        ConvenioSeguro convenio = convenios.buscarPorId(command.convenioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El convenio de seguro indicado no existe."));
        Optional<AfiliacionCliente> afiliacion = afiliaciones.buscarPorClienteYConvenio(venta.getClienteId(), convenio.getId());
        LocalDate hoy = clock.hoy();
        boolean afiliacionVigente = afiliacion.map(a -> a.estaActivaYVigente(hoy)).orElse(false);

        BigDecimal montoCubiertoTotal = BigDecimal.ZERO;
        BigDecimal copagoTotal = BigDecimal.ZERO;
        for (DetalleVenta detalle : venta.getDetalles()) {
            BigDecimal porcentajeCubierto = convenio.obtenerCoberturaPara(detalle.getProductoId())
                    .map(cobertura -> cobertura.getPorcentajeCubierto().valor())
                    .orElse(null);
            ResultadoCopago resultado = calcularCopago.calcular(new CalcularCopagoCommand(
                    detalle.getSubtotal().monto(), convenio.isActivo(), afiliacionVigente, porcentajeCubierto));
            montoCubiertoTotal = montoCubiertoTotal.add(resultado.montoCubierto().monto());
            copagoTotal = copagoTotal.add(resultado.copago().monto());
        }

        venta.asignarConvenio(convenio.getId());
        ventas.guardar(venta);

        return new CopagoResult(montoCubiertoTotal, copagoTotal);
    }
}
