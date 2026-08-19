package com.posfarmacia.usecases.port.out.inventario;

import com.posfarmacia.domain.model.catalogo.Producto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida: persistencia del agregado Producto (RF03). */
public interface ProductoRepositoryPort {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(UUID id);

    Optional<Producto> buscarPorCodigoBarras(String codigoBarras);

    Optional<Producto> buscarPorCodigoInterno(String codigoInterno);

    List<Producto> buscar(String texto, UUID categoriaId, UUID laboratorioId);
}
