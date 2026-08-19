package com.posfarmacia.usecases.usecase.seguro;

import com.posfarmacia.usecases.dto.seguro.ConsultarCoberturaCommand;
import com.posfarmacia.usecases.dto.seguro.ConsultarCoberturaResult;
import com.posfarmacia.usecases.port.in.seguro.ConsultarCoberturaSeguroUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.usecases.port.out.seguro.CoberturaCentralResult;
import com.posfarmacia.usecases.port.out.seguro.SeguroCentralPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.service.seguro.CalculadorCopago;
import com.posfarmacia.domain.service.seguro.ResultadoCopago;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Dni;

/**
 * RF10, RN22-RN27: identifica al cliente por DNI, consulta la central de seguros y
 * calcula el copago. Si la consulta a la central falla, la excepcion se propaga tal
 * cual (RN27, ver {@code ConsultaSeguroCentralFallidaException}) para que el adaptador
 * REST la traduzca a 409 en vez de asumir cobertura.
 */
public class ConsultarCoberturaSeguroUseCaseImpl implements ConsultarCoberturaSeguroUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final SeguroCentralPort seguroCentralPort;
    private final CalculadorCopago calculadorCopago;
    private final ClockPort clockPort;

    public ConsultarCoberturaSeguroUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort,
                                                SeguroCentralPort seguroCentralPort,
                                                CalculadorCopago calculadorCopago,
                                                ClockPort clockPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.seguroCentralPort = seguroCentralPort;
        this.calculadorCopago = calculadorCopago;
        this.clockPort = clockPort;
    }

    @Override
    public ConsultarCoberturaResult consultar(ConsultarCoberturaCommand command) {
        // RN22: DNI obligatorio para usar un convenio de seguro.
        Dni dni = new Dni(command.dni());
        Cliente cliente = clienteRepositoryPort.buscarPorDni(dni)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe un cliente registrado con ese DNI."));

        CoberturaCentralResult coberturaCentral = seguroCentralPort.consultarCobertura(
                cliente.getId(), command.convenioId(), command.productoId(), clockPort.hoy());

        ResultadoCopago resultado = calculadorCopago.calcular(
                new Dinero(command.montoLinea()),
                coberturaCentral.convenioActivo(),
                coberturaCentral.afiliacionActivaYVigente(),
                coberturaCentral.porcentajeCubierto());

        String codigoAutorizacion = coberturaCentral.codigoAutorizacion() == null
                ? null
                : coberturaCentral.codigoAutorizacion().valor();

        return new ConsultarCoberturaResult(cliente.getId(), command.convenioId(), command.productoId(),
                resultado.montoCubierto().monto(), resultado.copago().monto(), codigoAutorizacion);
    }
}
