package com.posfarmacia.usecases.usecase.seguro;

import com.posfarmacia.usecases.dto.seguro.ConfigurarCoberturaCommand;
import com.posfarmacia.usecases.dto.seguro.RegistrarConvenioCommand;
import com.posfarmacia.usecases.port.in.seguro.GestionarConvenioUseCase;
import com.posfarmacia.usecases.port.out.seguro.ConvenioSeguroRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.seguro.ConvenioSeguro;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class GestionarConvenioUseCaseImpl implements GestionarConvenioUseCase {

    private final ConvenioSeguroRepositoryPort convenioSeguroRepositoryPort;

    public GestionarConvenioUseCaseImpl(ConvenioSeguroRepositoryPort convenioSeguroRepositoryPort) {
        this.convenioSeguroRepositoryPort = convenioSeguroRepositoryPort;
    }

    @Override
    @Transactional
    public ConvenioSeguro registrar(RegistrarConvenioCommand command) {
        ConvenioSeguro convenio = new ConvenioSeguro(command.nombre());
        return convenioSeguroRepositoryPort.guardar(convenio);
    }

    @Override
    public List<ConvenioSeguro> listarTodos() {
        return convenioSeguroRepositoryPort.buscarTodos();
    }

    @Override
    @Transactional
    public ConvenioSeguro configurarCobertura(UUID convenioId, ConfigurarCoberturaCommand command) {
        ConvenioSeguro convenio = convenioSeguroRepositoryPort.buscarPorId(convenioId)
                .orElseThrow(() -> new EntidadNoEncontradaException("El convenio indicado no existe."));

        convenio.configurarCobertura(command.productoId(), new Porcentaje(command.porcentajeCubierto()));
        return convenioSeguroRepositoryPort.guardar(convenio);
    }
}
