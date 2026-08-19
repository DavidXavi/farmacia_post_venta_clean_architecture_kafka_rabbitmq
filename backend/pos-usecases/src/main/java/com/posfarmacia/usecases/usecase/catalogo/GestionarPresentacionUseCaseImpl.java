package com.posfarmacia.usecases.usecase.catalogo;

import com.posfarmacia.usecases.dto.catalogo.PresentacionResult;
import com.posfarmacia.usecases.port.in.catalogo.GestionarPresentacionUseCase;
import com.posfarmacia.usecases.port.out.inventario.PresentacionRepositoryPort;
import com.posfarmacia.domain.model.catalogo.Presentacion;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF03: alta y consulta de presentaciones del catalogo. */
public class GestionarPresentacionUseCaseImpl implements GestionarPresentacionUseCase {

    private final PresentacionRepositoryPort presentaciones;

    public GestionarPresentacionUseCaseImpl(PresentacionRepositoryPort presentaciones) {
        this.presentaciones = presentaciones;
    }

    @Override
    @Transactional
    public PresentacionResult crear(String nombre, String unidadMedida) {
        return aResult(presentaciones.guardar(new Presentacion(nombre, unidadMedida)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PresentacionResult> listar() {
        return presentaciones.listarTodos().stream().map(GestionarPresentacionUseCaseImpl::aResult).toList();
    }

    private static PresentacionResult aResult(Presentacion presentacion) {
        return new PresentacionResult(presentacion.getId(), presentacion.getNombre(), presentacion.getUnidadMedida());
    }
}
