package com.posfarmacia.usecases.usecase.incentivo;

import com.posfarmacia.usecases.dto.incentivo.ActualizarReglaIncentivoCommand;
import com.posfarmacia.usecases.dto.incentivo.CrearReglaIncentivoCommand;
import com.posfarmacia.usecases.port.in.incentivo.GestionarReglaIncentivoUseCase;
import com.posfarmacia.usecases.port.out.incentivo.ReglaIncentivoRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class GestionarReglaIncentivoUseCaseImpl implements GestionarReglaIncentivoUseCase {

    private final ReglaIncentivoRepositoryPort reglas;

    public GestionarReglaIncentivoUseCaseImpl(ReglaIncentivoRepositoryPort reglas) {
        this.reglas = reglas;
    }

    @Override
    @Transactional
    public ReglaIncentivo crear(CrearReglaIncentivoCommand command) {
        ReglaIncentivo regla = new ReglaIncentivo(command.nombre(), command.productoId(), command.categoriaId(),
                new Dinero(command.montoPorUnidad()), new PeriodoVigencia(command.fechaInicio(), command.fechaFin()));
        return reglas.guardar(regla);
    }

    @Override
    public List<ReglaIncentivo> listar() {
        return reglas.listar();
    }

    @Override
    @Transactional
    public ReglaIncentivo actualizar(UUID id, ActualizarReglaIncentivoCommand command) {
        ReglaIncentivo existente = reglas.buscarPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe la regla de incentivo indicada."));

        existente.actualizar(command.nombre(), command.productoId(), command.categoriaId(),
                new Dinero(command.montoPorUnidad()), new PeriodoVigencia(command.fechaInicio(), command.fechaFin()),
                command.activa());

        return reglas.guardar(existente);
    }
}
