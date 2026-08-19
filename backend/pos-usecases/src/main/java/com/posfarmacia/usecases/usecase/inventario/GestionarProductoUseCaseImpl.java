package com.posfarmacia.usecases.usecase.inventario;

import com.posfarmacia.usecases.dto.inventario.ActualizarProductoCommand;
import com.posfarmacia.usecases.dto.inventario.CrearProductoCommand;
import com.posfarmacia.usecases.dto.inventario.ProductoResult;
import com.posfarmacia.usecases.port.in.inventario.GestionarProductoUseCase;
import com.posfarmacia.usecases.port.out.inventario.CategoriaRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LaboratorioRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.PresentacionRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.catalogo.Producto;
import com.posfarmacia.domain.valueobject.CodigoBarras;
import com.posfarmacia.domain.valueobject.CodigoProducto;
import com.posfarmacia.domain.valueobject.Dinero;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF03: alta, edicion, baja y consulta de productos del catalogo. */
public class GestionarProductoUseCaseImpl implements GestionarProductoUseCase {

    private final ProductoRepositoryPort productos;
    private final CategoriaRepositoryPort categorias;
    private final LaboratorioRepositoryPort laboratorios;
    private final PresentacionRepositoryPort presentaciones;

    public GestionarProductoUseCaseImpl(ProductoRepositoryPort productos, CategoriaRepositoryPort categorias,
            LaboratorioRepositoryPort laboratorios, PresentacionRepositoryPort presentaciones) {
        this.productos = productos;
        this.categorias = categorias;
        this.laboratorios = laboratorios;
        this.presentaciones = presentaciones;
    }

    @Override
    @Transactional
    public ProductoResult crear(CrearProductoCommand command) {
        if (productos.buscarPorCodigoInterno(command.codigoInterno()).isPresent()) {
            throw new ValorInvalidoException("Ya existe un producto con el codigo interno '"
                    + command.codigoInterno() + "'.");
        }
        categorias.buscarPorId(command.categoriaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La categoria indicada no existe."));
        laboratorios.buscarPorId(command.laboratorioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El laboratorio indicado no existe."));
        presentaciones.buscarPorId(command.presentacionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La presentacion indicada no existe."));

        Producto producto = new Producto(
                new CodigoProducto(command.codigoInterno()),
                command.nombreComercial(),
                command.descripcion(),
                command.tipoProducto(),
                command.categoriaId(),
                command.laboratorioId(),
                command.presentacionId(),
                new Dinero(command.precioVenta()),
                command.esControlado(),
                command.requiereReceta(),
                command.tipoRecetaRequerida(),
                command.codigoBarras() == null ? null : new CodigoBarras(command.codigoBarras()));

        return ProductoResultMapper.aResult(productos.guardar(producto));
    }

    @Override
    @Transactional
    public ProductoResult actualizar(UUID productoId, ActualizarProductoCommand command) {
        Producto producto = obtenerOFallar(productoId);
        producto.actualizarDatos(command.nombreComercial(), command.descripcion(), new Dinero(command.precioVenta()));
        return ProductoResultMapper.aResult(productos.guardar(producto));
    }

    @Override
    @Transactional
    public void darDeBaja(UUID productoId) {
        Producto producto = obtenerOFallar(productoId);
        producto.darDeBaja();
        productos.guardar(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResult obtenerPorId(UUID productoId) {
        return ProductoResultMapper.aResult(obtenerOFallar(productoId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResult> buscar(String texto, UUID categoriaId, UUID laboratorioId) {
        return productos.buscar(texto, categoriaId, laboratorioId).stream()
                .map(ProductoResultMapper::aResult)
                .toList();
    }

    private Producto obtenerOFallar(UUID productoId) {
        return productos.buscarPorId(productoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("El producto indicado no existe."));
    }
}
