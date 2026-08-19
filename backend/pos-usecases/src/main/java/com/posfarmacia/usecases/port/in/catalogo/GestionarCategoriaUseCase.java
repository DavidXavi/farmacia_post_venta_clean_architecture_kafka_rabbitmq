package com.posfarmacia.usecases.port.in.catalogo;

import com.posfarmacia.usecases.dto.catalogo.CategoriaResult;
import java.util.List;

/** Puerto de entrada: alta y consulta de categorias del catalogo (RF03). CRUD simple, sin invariantes. */
public interface GestionarCategoriaUseCase {

    CategoriaResult crear(String nombre);

    List<CategoriaResult> listar();
}
