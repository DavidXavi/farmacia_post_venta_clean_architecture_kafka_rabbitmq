package com.posfarmacia.usecases.usecase.catalogo;

import com.posfarmacia.usecases.dto.catalogo.LaboratorioResult;
import com.posfarmacia.usecases.port.in.catalogo.GestionarLaboratorioUseCase;
import com.posfarmacia.usecases.port.out.inventario.LaboratorioRepositoryPort;
import com.posfarmacia.domain.model.catalogo.Laboratorio;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF03: alta y consulta de laboratorios del catalogo. */
public class GestionarLaboratorioUseCaseImpl implements GestionarLaboratorioUseCase {

    private final LaboratorioRepositoryPort laboratorios;

    public GestionarLaboratorioUseCaseImpl(LaboratorioRepositoryPort laboratorios) {
        this.laboratorios = laboratorios;
    }

    @Override
    @Transactional
    public LaboratorioResult crear(String nombre) {
        return aResult(laboratorios.guardar(new Laboratorio(nombre)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboratorioResult> listar() {
        return laboratorios.listarTodos().stream().map(GestionarLaboratorioUseCaseImpl::aResult).toList();
    }

    private static LaboratorioResult aResult(Laboratorio laboratorio) {
        return new LaboratorioResult(laboratorio.getId(), laboratorio.getNombre());
    }
}
