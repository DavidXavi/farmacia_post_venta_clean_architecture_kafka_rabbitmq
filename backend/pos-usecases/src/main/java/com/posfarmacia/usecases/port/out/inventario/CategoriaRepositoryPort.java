package com.posfarmacia.usecases.port.out.inventario;

import com.posfarmacia.domain.model.catalogo.Categoria;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida: alta y consulta del agregado Categoria. Ademas de validar la referencia de un
 * Producto, alimenta el CRUD simple de catalogo (RF03) que consume CatalogosPage.jsx.
 */
public interface CategoriaRepositoryPort {

    Categoria guardar(Categoria categoria);

    List<Categoria> listarTodos();

    Optional<Categoria> buscarPorId(UUID id);
}
