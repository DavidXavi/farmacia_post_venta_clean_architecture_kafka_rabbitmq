package com.posfarmacia.adapters.web.controller.catalogo;

import com.posfarmacia.adapters.web.request.catalogo.NombreRequest;
import com.posfarmacia.adapters.web.response.catalogo.CategoriaResponse;
import com.posfarmacia.usecases.port.in.catalogo.GestionarCategoriaUseCase;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF03: CRUD simple de categorias del catalogo, ruta "/api/categorias" (sin
 * prefijo "/v1") para calzar con CategoriasController.cs y con CatalogosPage.jsx.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriasController {

    private final GestionarCategoriaUseCase gestionarCategoria;

    public CategoriasController(GestionarCategoriaUseCase gestionarCategoria) {
        this.gestionarCategoria = gestionarCategoria;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody NombreRequest request) {
        var creada = gestionarCategoria.crear(request.nombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponse.desde(creada));
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return gestionarCategoria.listar().stream().map(CategoriaResponse::desde).toList();
    }
}
