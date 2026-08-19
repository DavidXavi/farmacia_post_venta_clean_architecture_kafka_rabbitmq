package com.posfarmacia.usecases.port.out.inventario;

import com.posfarmacia.domain.model.catalogo.Laboratorio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida: alta y consulta del agregado Laboratorio. Ademas de validar la referencia de un
 * Producto, alimenta el CRUD simple de catalogo (RF03) que consume CatalogosPage.jsx.
 */
public interface LaboratorioRepositoryPort {

    Laboratorio guardar(Laboratorio laboratorio);

    List<Laboratorio> listarTodos();

    Optional<Laboratorio> buscarPorId(UUID id);
}
