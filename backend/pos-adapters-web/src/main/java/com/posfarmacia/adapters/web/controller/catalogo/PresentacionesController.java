package com.posfarmacia.adapters.web.controller.catalogo;

import com.posfarmacia.adapters.web.request.catalogo.RegistrarPresentacionRequest;
import com.posfarmacia.adapters.web.response.catalogo.PresentacionResponse;
import com.posfarmacia.usecases.port.in.catalogo.GestionarPresentacionUseCase;
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
 * Adaptador de entrada RF03: CRUD simple de presentaciones del catalogo, ruta "/api/presentaciones"
 * (sin prefijo "/v1") para calzar con PresentacionesController.cs y con CatalogosPage.jsx.
 */
@RestController
@RequestMapping("/api/presentaciones")
public class PresentacionesController {

    private final GestionarPresentacionUseCase gestionarPresentacion;

    public PresentacionesController(GestionarPresentacionUseCase gestionarPresentacion) {
        this.gestionarPresentacion = gestionarPresentacion;
    }

    @PostMapping
    public ResponseEntity<PresentacionResponse> crear(@Valid @RequestBody RegistrarPresentacionRequest request) {
        var creada = gestionarPresentacion.crear(request.nombre(), request.unidadMedida());
        return ResponseEntity.status(HttpStatus.CREATED).body(PresentacionResponse.desde(creada));
    }

    @GetMapping
    public List<PresentacionResponse> listar() {
        return gestionarPresentacion.listar().stream().map(PresentacionResponse::desde).toList();
    }
}
