package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.Rol;
import java.util.List;

/**
 * RF01: consulta del catalogo de roles del sistema. El catalogo es fijo (seed en
 * V1__identidad.sql, equivalente al enum {@code RolNombre}); no hay alta/baja de roles.
 * Equivalente a ConsultarRolesUseCase (.NET).
 */
public interface GestionarRolUseCase {

    List<Rol> listar();
}
