package com.posfarmacia.usecases.port.out.inventario;

import com.posfarmacia.domain.model.catalogo.Presentacion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida: alta y consulta del agregado Presentacion. Ademas de validar la referencia de un
 * Producto, alimenta el CRUD simple de catalogo (RF03) que consume CatalogosPage.jsx.
 */
public interface PresentacionRepositoryPort {

    Presentacion guardar(Presentacion presentacion);

    List<Presentacion> listarTodos();

    Optional<Presentacion> buscarPorId(UUID id);
}
