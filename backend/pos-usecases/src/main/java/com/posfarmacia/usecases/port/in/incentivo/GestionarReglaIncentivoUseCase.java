package com.posfarmacia.usecases.port.in.incentivo;

import com.posfarmacia.usecases.dto.incentivo.ActualizarReglaIncentivoCommand;
import com.posfarmacia.usecases.dto.incentivo.CrearReglaIncentivoCommand;
import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import java.util.List;
import java.util.UUID;

/** Puerto de entrada: CRUD simple de reglas de incentivo (RF18), reservado al rol Administrador. */
public interface GestionarReglaIncentivoUseCase {

    ReglaIncentivo crear(CrearReglaIncentivoCommand command);

    List<ReglaIncentivo> listar();

    ReglaIncentivo actualizar(UUID id, ActualizarReglaIncentivoCommand command);
}
