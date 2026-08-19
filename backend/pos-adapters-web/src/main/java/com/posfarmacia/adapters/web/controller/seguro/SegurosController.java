package com.posfarmacia.adapters.web.controller.seguro;

import com.posfarmacia.adapters.web.request.seguro.ConsultarCoberturaRequest;
import com.posfarmacia.adapters.web.response.seguro.CoberturaResponse;
import com.posfarmacia.usecases.dto.seguro.ConsultarCoberturaCommand;
import com.posfarmacia.usecases.port.in.seguro.ConsultarCoberturaSeguroUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada REST (RF10, RN22-RN27, Word seccion 10). No calcula copagos:
 * delega en {@link ConsultarCoberturaSeguroUseCase}.
 */
@RestController
@RequestMapping("/api/seguros")
public class SegurosController {

    private final ConsultarCoberturaSeguroUseCase consultarCoberturaSeguroUseCase;

    public SegurosController(ConsultarCoberturaSeguroUseCase consultarCoberturaSeguroUseCase) {
        this.consultarCoberturaSeguroUseCase = consultarCoberturaSeguroUseCase;
    }

    @PostMapping("/coberturas/consultar")
    public ResponseEntity<CoberturaResponse> consultarCobertura(@Valid @RequestBody ConsultarCoberturaRequest request) {
        var command = new ConsultarCoberturaCommand(request.dni(), request.convenioId(), request.productoId(),
                request.montoLinea());
        return ResponseEntity.ok(CoberturaResponse.de(consultarCoberturaSeguroUseCase.consultar(command)));
    }
}
