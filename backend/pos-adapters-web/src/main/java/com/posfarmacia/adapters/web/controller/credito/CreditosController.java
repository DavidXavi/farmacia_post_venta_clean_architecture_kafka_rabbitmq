package com.posfarmacia.adapters.web.controller.credito;

import com.posfarmacia.adapters.web.request.credito.RegistrarLineaCreditoRequest;
import com.posfarmacia.adapters.web.response.cliente.LineaCreditoResponse;
import com.posfarmacia.usecases.dto.credito.RegistrarLineaCreditoCommand;
import com.posfarmacia.usecases.port.in.credito.RegistrarLineaCreditoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada REST para lineas de credito como recurso propio (RF11).
 * El frontend (CreditosPage.jsx) registra una linea de credito nueva para un cliente
 * ya identificado por DNI. No reimplementa {@code ValidadorLineaCredito}.
 */
@RestController
@RequestMapping("/api/lineas-credito")
public class CreditosController {

    private final RegistrarLineaCreditoUseCase registrarLineaCreditoUseCase;

    public CreditosController(RegistrarLineaCreditoUseCase registrarLineaCreditoUseCase) {
        this.registrarLineaCreditoUseCase = registrarLineaCreditoUseCase;
    }

    @PostMapping
    public ResponseEntity<LineaCreditoResponse> registrar(@Valid @RequestBody RegistrarLineaCreditoRequest request) {
        var command = new RegistrarLineaCreditoCommand(request.clienteId(), request.montoAutorizado(),
                request.vigenciaInicio(), request.vigenciaFin());
        LineaCreditoResponse response = LineaCreditoResponse.de(registrarLineaCreditoUseCase.registrar(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
