package com.posfarmacia.adapters.web.controller.identidad;

import com.posfarmacia.adapters.web.request.identidad.RegistrarUsuarioRequest;
import com.posfarmacia.adapters.web.response.identidad.UsuarioResponse;
import com.posfarmacia.usecases.port.in.identidad.GestionarRolUseCase;
import com.posfarmacia.usecases.port.in.identidad.GestionarUsuarioUseCase;
import com.posfarmacia.domain.model.identidad.Rol;
import com.posfarmacia.domain.model.identidad.Usuario;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** RF01: alta y consulta de usuarios del sistema. */
@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final GestionarUsuarioUseCase gestionarUsuario;
    private final GestionarRolUseCase gestionarRol;

    public UsuariosController(GestionarUsuarioUseCase gestionarUsuario, GestionarRolUseCase gestionarRol) {
        this.gestionarUsuario = gestionarUsuario;
        this.gestionarRol = gestionarRol;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        Map<UUID, String> nombresRolPorId = gestionarRol.listar().stream()
                .collect(Collectors.toMap(Rol::getId, rol -> rol.getNombre().name()));
        return gestionarUsuario.listar().stream()
                .map(usuario -> UsuarioResponse.desde(usuario, nombresRolPorId))
                .toList();
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {
        Usuario usuario = gestionarUsuario.registrar(request.nombreUsuario(), request.password(), request.localId(),
                request.roles());
        Map<UUID, String> nombresRolPorId = gestionarRol.listar().stream()
                .collect(Collectors.toMap(Rol::getId, rol -> rol.getNombre().name()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.desde(usuario, nombresRolPorId));
    }
}
