package com.posfarmacia.adapters.web.controller.cliente;

import com.posfarmacia.adapters.web.request.cliente.ActualizarClienteRequest;
import com.posfarmacia.adapters.web.request.cliente.RegistrarClienteRequest;
import com.posfarmacia.adapters.web.response.cliente.AfiliacionResponse;
import com.posfarmacia.adapters.web.response.cliente.ClienteResponse;
import com.posfarmacia.adapters.web.response.cliente.LineaCreditoResponse;
import com.posfarmacia.usecases.dto.cliente.ActualizarClienteCommand;
import com.posfarmacia.usecases.dto.cliente.RegistrarClienteCommand;
import com.posfarmacia.usecases.port.in.cliente.ActualizarClienteUseCase;
import com.posfarmacia.usecases.port.in.cliente.ConsultarClientesUseCase;
import com.posfarmacia.usecases.port.in.cliente.ConsultarConveniosClienteUseCase;
import com.posfarmacia.usecases.port.in.cliente.IdentificarClienteUseCase;
import com.posfarmacia.usecases.port.in.cliente.RegistrarClienteUseCase;
import com.posfarmacia.usecases.port.in.credito.ConsultarLineaCreditoClienteUseCase;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada REST (RF09, Word seccion 10). No calcula reglas de negocio:
 * solo mapea HTTP <-> casos de uso. Rutas sin prefijo /v1 para calzar con el frontend
 * React ya existente (ver docs/convenciones-migracion-java.md).
 */
@RestController
@RequestMapping("/api/clientes")
public class ClientesController {

    private final IdentificarClienteUseCase identificarClienteUseCase;
    private final RegistrarClienteUseCase registrarClienteUseCase;
    private final ConsultarConveniosClienteUseCase consultarConveniosClienteUseCase;
    private final ConsultarLineaCreditoClienteUseCase consultarLineaCreditoClienteUseCase;
    private final ConsultarClientesUseCase consultarClientesUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;

    public ClientesController(IdentificarClienteUseCase identificarClienteUseCase,
                               RegistrarClienteUseCase registrarClienteUseCase,
                               ConsultarConveniosClienteUseCase consultarConveniosClienteUseCase,
                               ConsultarLineaCreditoClienteUseCase consultarLineaCreditoClienteUseCase,
                               ConsultarClientesUseCase consultarClientesUseCase,
                               ActualizarClienteUseCase actualizarClienteUseCase) {
        this.identificarClienteUseCase = identificarClienteUseCase;
        this.registrarClienteUseCase = registrarClienteUseCase;
        this.consultarConveniosClienteUseCase = consultarConveniosClienteUseCase;
        this.consultarLineaCreditoClienteUseCase = consultarLineaCreditoClienteUseCase;
        this.consultarClientesUseCase = consultarClientesUseCase;
        this.actualizarClienteUseCase = actualizarClienteUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<ClienteResponse> respuesta = consultarClientesUseCase.consultarTodos().stream()
                .map(ClienteResponse::de)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable UUID id, @RequestBody ActualizarClienteRequest request) {
        var command = new ActualizarClienteCommand(request.telefono(), request.correo(), request.direccion());
        return ResponseEntity.ok(ClienteResponse.de(actualizarClienteUseCase.actualizar(id, command)));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<ClienteResponse> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(ClienteResponse.de(identificarClienteUseCase.identificarPorDni(dni)));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody RegistrarClienteRequest request) {
        var command = new RegistrarClienteCommand(request.dni(), request.nombres(), request.apellidos(),
                request.fechaNacimiento(), request.telefono(), request.correo(), request.direccion());
        ClienteResponse response = ClienteResponse.de(registrarClienteUseCase.registrar(command));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/convenios")
    public ResponseEntity<List<AfiliacionResponse>> convenios(@PathVariable UUID id) {
        List<AfiliacionResponse> respuesta = consultarConveniosClienteUseCase.consultarPorCliente(id).stream()
                .map(AfiliacionResponse::de)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}/linea-credito")
    public ResponseEntity<LineaCreditoResponse> lineaCredito(@PathVariable UUID id) {
        LineaCreditoResponse response = consultarLineaCreditoClienteUseCase.consultarPorCliente(id)
                .map(LineaCreditoResponse::de)
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente no tiene una linea de credito registrada."));
        return ResponseEntity.ok(response);
    }
}
