package com.posfarmacia.adapters.web.controller.identidad;

import com.posfarmacia.adapters.web.response.identidad.RolResponse;
import com.posfarmacia.usecases.port.in.identidad.GestionarRolUseCase;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** RF01: consulta del catalogo de roles del sistema. */
@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final GestionarRolUseCase gestionarRol;

    public RolesController(GestionarRolUseCase gestionarRol) {
        this.gestionarRol = gestionarRol;
    }

    @GetMapping
    public List<RolResponse> listar() {
        return gestionarRol.listar().stream().map(RolResponse::desde).toList();
    }
}
