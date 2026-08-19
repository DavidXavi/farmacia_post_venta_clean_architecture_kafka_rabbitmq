package com.posfarmacia.usecases.usecase.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;
import com.posfarmacia.usecases.port.in.mensajeria.ConsultarActividadRecienteUseCase;
import com.posfarmacia.usecases.port.out.mensajeria.ActividadRecienteRepositoryPort;
import java.util.List;

public class ConsultarActividadRecienteUseCaseImpl implements ConsultarActividadRecienteUseCase {

    private final ActividadRecienteRepositoryPort actividades;

    public ConsultarActividadRecienteUseCaseImpl(ActividadRecienteRepositoryPort actividades) {
        this.actividades = actividades;
    }

    @Override
    public List<ActividadReciente> listar(int limite) {
        return actividades.listarRecientes(limite);
    }
}
