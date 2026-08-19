package com.posfarmacia.adapters.web.controller.inventario;

import com.posfarmacia.adapters.web.request.inventario.ActualizarProductoRequest;
import com.posfarmacia.adapters.web.request.inventario.CrearProductoRequest;
import com.posfarmacia.adapters.web.response.inventario.ProductoResponse;
import com.posfarmacia.adapters.web.response.inventario.StockVendibleResponse;
import com.posfarmacia.usecases.dto.inventario.ActualizarProductoCommand;
import com.posfarmacia.usecases.dto.inventario.CrearProductoCommand;
import com.posfarmacia.usecases.port.in.inventario.ConsultarStockVendibleUseCase;
import com.posfarmacia.usecases.port.in.inventario.GestionarProductoUseCase;
import com.posfarmacia.domain.enums.TipoProducto;
import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF03/RF14: solo traduce HTTP <-> casos de uso, no calcula reglas de negocio.
 * Ruta sin prefijo de version ("/api/productos", no "/api/v1/productos"): el frontend React
 * (ProductosPage.jsx) ya llama esta ruta y no se va a reescribir (ver convenciones-migracion-java.md).
 */
@RestController
@RequestMapping("/api/productos")
public class ProductosController {

    private final GestionarProductoUseCase gestionarProducto;
    private final ConsultarStockVendibleUseCase consultarStockVendible;

    public ProductosController(GestionarProductoUseCase gestionarProducto,
            ConsultarStockVendibleUseCase consultarStockVendible) {
        this.gestionarProducto = gestionarProducto;
        this.consultarStockVendible = consultarStockVendible;
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody CrearProductoRequest request) {
        CrearProductoCommand command = new CrearProductoCommand(
                request.codigoInterno(),
                request.codigoBarras(),
                request.nombreComercial(),
                request.descripcion(),
                parseEnum(TipoProducto.class, request.tipoProducto(), "tipo de producto"),
                request.categoriaId(),
                request.laboratorioId(),
                request.presentacionId(),
                request.precioVenta(),
                request.esControlado(),
                request.requiereReceta(),
                request.tipoRecetaRequerida() == null
                        ? null
                        : parseEnum(TipoReceta.class, request.tipoRecetaRequerida(), "tipo de receta"));

        var creado = gestionarProducto.crear(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponse.desde(creado));
    }

    @GetMapping
    public List<ProductoResponse> buscar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) UUID categoriaId,
            @RequestParam(required = false) UUID laboratorioId) {
        return gestionarProducto.buscar(texto, categoriaId, laboratorioId).stream()
                .map(ProductoResponse::desde)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductoResponse obtenerPorId(@PathVariable UUID id) {
        return ProductoResponse.desde(gestionarProducto.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable UUID id, @Valid @RequestBody ActualizarProductoRequest request) {
        ActualizarProductoCommand command = new ActualizarProductoCommand(
                request.nombreComercial(), request.descripcion(), request.precioVenta());
        return ProductoResponse.desde(gestionarProducto.actualizar(id, command));
    }

    @GetMapping("/{id}/stock-vendible")
    public StockVendibleResponse stockVendible(@PathVariable UUID id, @RequestParam UUID localId) {
        return StockVendibleResponse.desde(consultarStockVendible.consultar(id, localId));
    }

    @PatchMapping("/{id}/dar-de-baja")
    public ResponseEntity<Void> darDeBaja(@PathVariable UUID id) {
        gestionarProducto.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> tipo, String valor, String etiqueta) {
        try {
            return Enum.valueOf(tipo, valor.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ValorInvalidoException("El " + etiqueta + " '" + valor + "' no es valido.");
        }
    }
}
