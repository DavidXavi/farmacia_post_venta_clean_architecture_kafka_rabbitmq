package com.posfarmacia.adapters.persistence.mapper.catalogo;

import com.posfarmacia.adapters.persistence.entity.catalogo.ProductoJpaEntity;
import com.posfarmacia.domain.enums.EstadoProducto;
import com.posfarmacia.domain.enums.TipoProducto;
import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.model.catalogo.Producto;
import com.posfarmacia.domain.valueobject.CodigoBarras;
import com.posfarmacia.domain.valueobject.CodigoProducto;
import com.posfarmacia.domain.valueobject.Dinero;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static Producto aDominio(ProductoJpaEntity entity) {
        return Producto.reconstruir(
                entity.getId(),
                new CodigoProducto(entity.getCodigoInterno()),
                entity.getCodigoBarras() == null ? null : new CodigoBarras(entity.getCodigoBarras()),
                entity.getNombreComercial(),
                entity.getDescripcion(),
                TipoProducto.valueOf(entity.getTipoProducto()),
                entity.getCategoriaId(),
                entity.getLaboratorioId(),
                entity.getPresentacionId(),
                new Dinero(entity.getPrecioVenta()),
                entity.isEsControlado(),
                entity.isRequiereReceta(),
                entity.getTipoRecetaRequerida() == null ? null : TipoReceta.valueOf(entity.getTipoRecetaRequerida()),
                EstadoProducto.valueOf(entity.getEstado()));
    }

    public static ProductoJpaEntity aEntidad(Producto producto) {
        return new ProductoJpaEntity(
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
