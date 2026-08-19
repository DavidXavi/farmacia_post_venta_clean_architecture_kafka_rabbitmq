package com.posfarmacia.adapters.web.controller.anulacion;

import com.posfarmacia.adapters.web.request.anulacion.EmitirNotaCreditoRequest;
import com.posfarmacia.adapters.web.response.anulacion.NotaCreditoResponse;
import com.posfarmacia.usecases.dto.anulacion.EmitirNotaCreditoCommand;
import com.posfarmacia.usecases.port.in.anulacion.EmitirNotaCreditoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF16/RN39/RN40: ruta EXACTA heredada de
 * `PosFarmacia.Presentation.Controllers.NotasCreditoController` en arquitectura_2_t2: base
 * {@code api/notas-credito}, sin prefijo {@code /v1} (ver convenciones-migracion-java.md).
 */
@RestController
@RequestMapping("/api/notas-credito")
public class NotasCreditoController {

    private final EmitirNotaCreditoUseCase emitirNotaCredito;

    public NotasCreditoController(EmitirNotaCreditoUseCase emitirNotaCredito) {
        this.emitirNotaCredito = emitirNotaCredito;
    }

    @PostMapping
    public ResponseEntity<NotaCreditoResponse> emitir(@Valid @RequestBody EmitirNotaCreditoRequest request) {
        var command = new EmitirNotaCreditoCommand(request.ventaId(), request.usuarioId(), request.motivo());
        var resultado = emitirNotaCredito.emitir(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(NotaCreditoResponse.desde(resultado));
    }
}
