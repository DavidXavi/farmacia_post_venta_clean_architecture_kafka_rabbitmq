package com.posfarmacia.adapters.web.controller.inventario;

import com.posfarmacia.adapters.web.request.inventario.RegistrarLoteRequest;
import com.posfarmacia.adapters.web.response.inventario.LoteResponse;
import com.posfarmacia.usecases.dto.inventario.RegistrarLoteCommand;
import com.posfarmacia.usecases.port.in.inventario.BloquearLoteUseCase;
import com.posfarmacia.usecases.port.in.inventario.ConsultarLotesUseCase;
import com.posfarmacia.usecases.port.in.inventario.RegistrarIngresoLoteUseCase;
import com.posfarmacia.usecases.port.in.inventario.RetirarLoteUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF04: solo traduce HTTP <-> casos de uso, no calcula FEFO ni reglas de vencimiento.
 * Ruta sin prefijo de version ("/api/lotes", no "/api/v1/lotes"): el frontend React (LotesPage.jsx) ya
 * llama esta ruta y no se va a reescribir (ver convenciones-migracion-java.md). El endpoint POST no
 * figura en la lista minima del Word seccion 10, pero se agrega para exponer RegistrarIngresoLoteUseCase
 * (exigido en la seccion 6.3/RF04): sin el, los lotes no podrian darse de alta por API y GET /lotes,
 * bloquear/retirar y stock-vendible quedarian sin datos que consultar. Bloquear/retirar usan PATCH
 * (no POST) para calzar exactamente con LotesController.cs y con LotesPage.jsx (usa api.patch).
 */
@RestController
@RequestMapping("/api/lotes")
public class LotesController {

    private final RegistrarIngresoLoteUseCase registrarIngresoLote;
    private final ConsultarLotesUseCase consultarLotes;
    private final BloquearLoteUseCase bloquearLote;
    private final RetirarLoteUseCase retirarLote;

    public LotesController(RegistrarIngresoLoteUseCase registrarIngresoLote, ConsultarLotesUseCase consultarLotes,
            BloquearLoteUseCase bloquearLote, RetirarLoteUseCase retirarLote) {
        this.registrarIngresoLote = registrarIngresoLote;
        this.consultarLotes = consultarLotes;
        this.bloquearLote = bloquearLote;
        this.retirarLote = retirarLote;
    }

    @PostMapping
    public ResponseEntity<LoteResponse> registrar(@Valid @RequestBody RegistrarLoteRequest request) {
        RegistrarLoteCommand command = new RegistrarLoteCommand(
                request.codigo(), request.productoId(), request.fechaVencimiento(), request.cantidadRecibida(),
                request.localId(), request.costo());
        var creado = registrarIngresoLote.registrar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(LoteResponse.desde(creado));
    }

    @GetMapping
    public List<LoteResponse> listar(@RequestParam(required = false) UUID productoId) {
        return consultarLotes.consultar(productoId).stream().map(LoteResponse::desde).toList();
    }

    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<Void> bloquear(@PathVariable UUID id) {
        bloquearLote.bloquear(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/retirar")
    public ResponseEntity<Void> retirar(@PathVariable UUID id) {
        retirarLote.retirar(id);
        return ResponseEntity.noContent().build();
    }
}
