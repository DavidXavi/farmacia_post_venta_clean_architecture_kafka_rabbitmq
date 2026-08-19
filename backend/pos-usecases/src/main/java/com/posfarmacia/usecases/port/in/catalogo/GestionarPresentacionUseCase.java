package com.posfarmacia.usecases.port.in.catalogo;

import com.posfarmacia.usecases.dto.catalogo.PresentacionResult;
import java.util.List;

/** Puerto de entrada: alta y consulta de presentaciones del catalogo (RF03). CRUD simple, sin invariantes. */
public interface GestionarPresentacionUseCase {

    PresentacionResult crear(String nombre, String unidadMedida);

    List<PresentacionResult> listar();
}
