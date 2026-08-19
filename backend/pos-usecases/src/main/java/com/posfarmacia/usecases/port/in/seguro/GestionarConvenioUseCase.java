package com.posfarmacia.usecases.port.in.seguro;

import com.posfarmacia.usecases.dto.seguro.ConfigurarCoberturaCommand;
import com.posfarmacia.usecases.dto.seguro.RegistrarConvenioCommand;
import com.posfarmacia.domain.model.seguro.ConvenioSeguro;
import java.util.List;
import java.util.UUID;

/**
 * CRUD de convenios de seguro como recurso propio (RF10). El frontend (ConveniosPage.jsx)
 * trata el convenio como un recurso completo: se registra, se lista y se le configuran
 * coberturas por producto. No reimplementa el calculo de copago ({@code CalculadorCopago}).
 */
public interface GestionarConvenioUseCase {

    ConvenioSeguro registrar(RegistrarConvenioCommand command);

    List<ConvenioSeguro> listarTodos();

    ConvenioSeguro configurarCobertura(UUID convenioId, ConfigurarCoberturaCommand command);
}
