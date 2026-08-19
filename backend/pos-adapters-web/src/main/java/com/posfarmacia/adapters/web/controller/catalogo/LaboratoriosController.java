package com.posfarmacia.adapters.web.controller.catalogo;

import com.posfarmacia.adapters.web.request.catalogo.NombreRequest;
import com.posfarmacia.adapters.web.response.catalogo.LaboratorioResponse;
import com.posfarmacia.usecases.port.in.catalogo.GestionarLaboratorioUseCase;
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
 * Adaptador de entrada RF03: CRUD simple de laboratorios del catalogo, ruta "/api/laboratorios" (sin
 * prefijo "/v1") para calzar con LaboratoriosController.cs y con CatalogosPage.jsx.
 */
@RestController
@RequestMapping("/api/laboratorios")
public class LaboratoriosController {

    private final GestionarLaboratorioUseCase gestionarLaboratorio;

    public LaboratoriosController(GestionarLaboratorioUseCase gestionarLaboratorio) {
        this.gestionarLaboratorio = gestionarLaboratorio;
    }

    @PostMapping
    public ResponseEntity<LaboratorioResponse> crear(@Valid @RequestBody NombreRequest request) {
        var creado = gestionarLaboratorio.crear(request.nombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(LaboratorioResponse.desde(creado));
    }

    @GetMapping
    public List<LaboratorioResponse> listar() {
        return gestionarLaboratorio.listar().stream().map(LaboratorioResponse::desde).toList();
    }
}
