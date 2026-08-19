package com.posfarmacia.usecases.usecase.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;
import com.posfarmacia.usecases.port.in.mensajeria.RegistrarActividadUseCase;
import com.posfarmacia.usecases.port.out.mensajeria.ActividadRecienteRepositoryPort;

public class RegistrarActividadUseCaseImpl implements RegistrarActividadUseCase {

    private final ActividadRecienteRepositoryPort actividades;

    public RegistrarActividadUseCaseImpl(ActividadRecienteRepositoryPort actividades) {
        this.actividades = actividades;
    }

    @Override
    public void registrar(ActividadReciente actividad) {
        actividades.registrar(actividad);
    }
}
