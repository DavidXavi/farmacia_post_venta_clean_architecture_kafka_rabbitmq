package com.posfarmacia.usecases.port.in.catalogo;

import com.posfarmacia.usecases.dto.catalogo.LaboratorioResult;
import java.util.List;

/** Puerto de entrada: alta y consulta de laboratorios del catalogo (RF03). CRUD simple, sin invariantes. */
public interface GestionarLaboratorioUseCase {

    LaboratorioResult crear(String nombre);

    List<LaboratorioResult> listar();
}
