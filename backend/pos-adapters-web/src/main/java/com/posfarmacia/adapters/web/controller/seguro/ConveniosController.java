package com.posfarmacia.adapters.web.controller.seguro;

import com.posfarmacia.adapters.web.request.seguro.ConfigurarCoberturaRequest;
import com.posfarmacia.adapters.web.request.seguro.RegistrarAfiliacionRequest;
import com.posfarmacia.adapters.web.request.seguro.RegistrarConvenioRequest;
import com.posfarmacia.adapters.web.response.cliente.AfiliacionResponse;
import com.posfarmacia.adapters.web.response.seguro.ConvenioResponse;
import com.posfarmacia.usecases.dto.seguro.ConfigurarCoberturaCommand;
import com.posfarmacia.usecases.dto.seguro.RegistrarAfiliacionCommand;
import com.posfarmacia.usecases.dto.seguro.RegistrarConvenioCommand;
import com.posfarmacia.usecases.port.in.seguro.GestionarConvenioUseCase;
import com.posfarmacia.usecases.port.in.seguro.RegistrarAfiliacionUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada REST para convenios de seguro como recurso propio (RF10).
 * El frontend (ConveniosPage.jsx) trata el convenio como un recurso completo: se
 * registra, se lista, se le configuran coberturas por producto y se afilian clientes.
 * No calcula copagos: eso lo resuelve {@code CalculadorCopago} via {@link SegurosController}
 * y el flujo de venta.
 */
@RestController
@RequestMapping("/api/convenios")
public class ConveniosController {

    private final GestionarConvenioUseCase gestionarConvenioUseCase;
    private final RegistrarAfiliacionUseCase registrarAfiliacionUseCase;

    public ConveniosController(GestionarConvenioUseCase gestionarConvenioUseCase,
                                RegistrarAfiliacionUseCase registrarAfiliacionUseCase) {
        this.gestionarConvenioUseCase = gestionarConvenioUseCase;
        this.registrarAfiliacionUseCase = registrarAfiliacionUseCase;
    }

    @PostMapping
    public ResponseEntity<ConvenioResponse> registrar(@Valid @RequestBody RegistrarConvenioRequest request) {
        var command = new RegistrarConvenioCommand(request.nombre());
        ConvenioResponse response = ConvenioResponse.de(gestionarConvenioUseCase.registrar(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ConvenioResponse>> listar() {
        List<ConvenioResponse> respuesta = gestionarConvenioUseCase.listarTodos().stream()
                .map(ConvenioResponse::de)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/{id}/coberturas")
    public ResponseEntity<ConvenioResponse> configurarCobertura(@PathVariable UUID id,
                                                                 @Valid @RequestBody ConfigurarCoberturaRequest request) {
        var command = new ConfigurarCoberturaCommand(request.productoId(), request.porcentajeCubierto());
        ConvenioResponse response = ConvenioResponse.de(gestionarConvenioUseCase.configurarCobertura(id, command));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/afiliaciones")
    public ResponseEntity<AfiliacionResponse> registrarAfiliacion(@Valid @RequestBody RegistrarAfiliacionRequest request) {
        var command = new RegistrarAfiliacionCommand(request.clienteId(), request.convenioId(),
                request.vigenciaInicio(), request.vigenciaFin());
        AfiliacionResponse response = AfiliacionResponse.de(registrarAfiliacionUseCase.registrar(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
