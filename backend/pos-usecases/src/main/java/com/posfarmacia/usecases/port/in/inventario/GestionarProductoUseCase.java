package com.posfarmacia.usecases.port.in.inventario;

import com.posfarmacia.usecases.dto.inventario.ActualizarProductoCommand;
import com.posfarmacia.usecases.dto.inventario.CrearProductoCommand;
import com.posfarmacia.usecases.dto.inventario.ProductoResult;
import java.util.List;
import java.util.UUID;

/** Puerto de entrada: alta, edicion, baja y consulta de productos del catalogo (RF03). */
public interface GestionarProductoUseCase {

    ProductoResult crear(CrearProductoCommand command);

    ProductoResult actualizar(UUID productoId, ActualizarProductoCommand command);

    void darDeBaja(UUID productoId);

    ProductoResult obtenerPorId(UUID productoId);

    List<ProductoResult> buscar(String texto, UUID categoriaId, UUID laboratorioId);
}
