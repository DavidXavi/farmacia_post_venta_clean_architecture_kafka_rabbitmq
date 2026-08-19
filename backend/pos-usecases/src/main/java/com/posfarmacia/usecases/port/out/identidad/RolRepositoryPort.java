package com.posfarmacia.usecases.port.out.identidad;

import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.model.identidad.Rol;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de lectura del catalogo de roles (RF01). El catalogo de roles es fijo (ver seed en
 * V1__identidad.sql), por eso no expone alta/baja: solo consulta, usada tanto por
 * {@code AutenticarUsuarioUseCase} (resolver nombres de rol de un usuario autenticado) como por
 * {@code GestionarUsuarioUseCase}/{@code GestionarRolUseCase} (registrar/listar usuarios con sus roles).
 */
public interface RolRepositoryPort {

    Optional<Rol> buscarPorId(UUID id);

    Optional<Rol> buscarPorNombre(RolNombre nombre);

    List<Rol> listarTodos();
}
