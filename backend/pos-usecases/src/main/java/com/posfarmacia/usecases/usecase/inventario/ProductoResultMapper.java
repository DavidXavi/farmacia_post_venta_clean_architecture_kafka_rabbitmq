package com.posfarmacia.usecases.usecase.inventario;

import com.posfarmacia.usecases.dto.inventario.ProductoResult;
import com.posfarmacia.domain.model.catalogo.Producto;

/** Mapeo de Producto (dominio) a su proyeccion de lectura. Sin estado, compartido entre los casos de uso del contexto. */
final class ProductoResultMapper {

    private ProductoResultMapper() {
    }

    static ProductoResult aResult(Producto producto) {
        return new ProductoResult(
                producto.getId(),
                producto.getCodigoInterno().valor(),
                producto.getCodigoBarras() == null ? null : producto.getCodigoBarras().valor(),
                producto.getNombreComercial(),
                producto.getDescripcion(),
                producto.getTipoProducto().name(),
                producto.getCategoriaId(),
                producto.getLaboratorioId(),
                producto.getPresentacionId(),
                producto.getPrecioVenta().monto(),
                producto.isEsControlado(),
                producto.isRequiereReceta(),
                producto.getTipoRecetaRequerida() == null ? null : producto.getTipoRecetaRequerida().name(),
                producto.getEstado().name());
    }
}
