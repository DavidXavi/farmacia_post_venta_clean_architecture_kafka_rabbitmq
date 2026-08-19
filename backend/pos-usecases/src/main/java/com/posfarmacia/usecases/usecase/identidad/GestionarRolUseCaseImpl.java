package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.port.in.identidad.GestionarRolUseCase;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.domain.model.identidad.Rol;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/** RF01: consulta del catalogo (fijo) de roles del sistema. */
public class GestionarRolUseCaseImpl implements GestionarRolUseCase {

    private final RolRepositoryPort roles;

    public GestionarRolUseCaseImpl(RolRepositoryPort roles) {
        this.roles = roles;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listar() {
        return roles.listarTodos();
    }
}
