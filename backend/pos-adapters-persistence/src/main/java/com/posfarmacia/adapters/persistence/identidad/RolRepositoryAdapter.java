package com.posfarmacia.adapters.persistence.identidad;

import com.posfarmacia.adapters.persistence.mapper.identidad.RolMapper;
import com.posfarmacia.adapters.persistence.repository.identidad.RolJpaRepository;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.model.identidad.Rol;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class RolRepositoryAdapter implements RolRepositoryPort {

    private final RolJpaRepository roles;

    public RolRepositoryAdapter(RolJpaRepository roles) {
        this.roles = roles;
    }

    @Override
    public Optional<Rol> buscarPorId(UUID id) {
        return roles.findById(id).map(RolMapper::aDominio);
    }

    @Override
    public Optional<Rol> buscarPorNombre(RolNombre nombre) {
        return roles.findByNombre(nombre.name()).map(RolMapper::aDominio);
    }

    @Override
    public List<Rol> listarTodos() {
        return roles.findAll().stream().map(RolMapper::aDominio).toList();
    }
}
