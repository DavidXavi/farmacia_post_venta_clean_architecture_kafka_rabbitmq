package com.posfarmacia.adapters.web.controller.incentivo;

import com.posfarmacia.adapters.web.request.incentivo.ActualizarReglaIncentivoRequest;
import com.posfarmacia.adapters.web.request.incentivo.CrearReglaIncentivoRequest;
import com.posfarmacia.adapters.web.response.incentivo.ReglaIncentivoResponse;
import com.posfarmacia.usecases.dto.incentivo.ActualizarReglaIncentivoCommand;
import com.posfarmacia.usecases.dto.incentivo.CrearReglaIncentivoCommand;
import com.posfarmacia.usecases.port.in.incentivo.GestionarReglaIncentivoUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF18: CRUD simple de reglas de incentivo, reservado al rol Administrador
 * (ver {@code SecurityConfig}). Ruta EXACTA que ya consume
 * `frontend/src/pages/CatalogosPage.jsx` ({@code /api/reglas-incentivo}, sin prefijo {@code /v1}).
 * No calcula reglas de negocio: solo mapea HTTP &lt;-&gt; caso de uso.
 */
@RestController
@RequestMapping("/api/reglas-incentivo")
public class ReglasIncentivoController {

    private final GestionarReglaIncentivoUseCase gestionarReglaIncentivo;

    public ReglasIncentivoController(GestionarReglaIncentivoUseCase gestionarReglaIncentivo) {
        this.gestionarReglaIncentivo = gestionarReglaIncentivo;
    }

    @GetMapping
    public List<ReglaIncentivoResponse> listar() {
        return gestionarReglaIncentivo.listar().stream().map(ReglaIncentivoResponse::desde).toList();
    }

    @PostMapping
    public ResponseEntity<ReglaIncentivoResponse> crear(@Valid @RequestBody CrearReglaIncentivoRequest request) {
        var command = new CrearReglaIncentivoCommand(request.nombre(), request.productoId(), request.categoriaId(),
                request.montoPorUnidad(), request.fechaInicio(), request.fechaFin());
        var creada = gestionarReglaIncentivo.crear(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReglaIncentivoResponse.desde(creada));
    }

    @PutMapping("/{id}")
    public ReglaIncentivoResponse actualizar(@PathVariable UUID id,
            @Valid @RequestBody ActualizarReglaIncentivoRequest request) {
        var command = new ActualizarReglaIncentivoCommand(request.nombre(), request.productoId(),
                request.categoriaId(), request.montoPorUnidad(), request.fechaInicio(), request.fechaFin(),
                request.activa());
        return ReglaIncentivoResponse.desde(gestionarReglaIncentivo.actualizar(id, command));
    }
}
