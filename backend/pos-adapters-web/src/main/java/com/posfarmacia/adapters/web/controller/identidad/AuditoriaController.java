package com.posfarmacia.adapters.web.controller.identidad;

import com.posfarmacia.adapters.web.response.identidad.AuditoriaResponse;
import com.posfarmacia.usecases.port.in.identidad.ConsultarAuditoriaUseCase;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** RF19: consulta de auditoria de operaciones sensibles, reservada al rol Administrador. */
@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final ConsultarAuditoriaUseCase consultarAuditoria;

    public AuditoriaController(ConsultarAuditoriaUseCase consultarAuditoria) {
        this.consultarAuditoria = consultarAuditoria;
    }

    @GetMapping
    public List<AuditoriaResponse> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String entidad,
            @RequestParam(required = false) UUID usuarioId) {
        return consultarAuditoria.consultar(fecha, entidad, usuarioId).stream()
                .map(AuditoriaResponse::desde)
                .toList();
    }
}
