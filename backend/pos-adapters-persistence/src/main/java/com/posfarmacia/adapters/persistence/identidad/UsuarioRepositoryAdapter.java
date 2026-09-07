package com.posfarmacia.adapters.persistence.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.UsuarioJpaEntity;
import com.posfarmacia.adapters.persistence.entity.identidad.UsuarioRolJpaEntity;
import com.posfarmacia.adapters.persistence.mapper.identidad.UsuarioMapper;
import com.posfarmacia.adapters.persistence.repository.identidad.UsuarioJpaRepository;
import com.posfarmacia.adapters.persistence.repository.identidad.UsuarioRolJpaRepository;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarios;
    private final UsuarioRolJpaRepository usuariosRoles;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository usuarios, UsuarioRolJpaRepository usuariosRoles) {
        this.usuarios = usuarios;
        this.usuariosRoles = usuariosRoles;
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarios.findById(id).map(this::aDominioConRoles);
    }

    @Override
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarios.findByNombreUsuario(nombreUsuario).map(this::aDominioConRoles);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.findByEmail(email).map(this::aDominioConRoles);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        usuarios.save(UsuarioMapper.aEntidad(usuario));

        usuariosRoles.deleteByUsuarioId(usuario.getId());
        Set<UsuarioRolJpaEntity> filas = usuario.getRolesIds().stream()
                .map(rolId -> new UsuarioRolJpaEntity(usuario.getId(), rolId))
                .collect(Collectors.toSet());
        usuariosRoles.saveAll(filas);

        return usuario;
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarios.findAll().stream().map(this::aDominioConRoles).toList();
    }

    private Usuario aDominioConRoles(UsuarioJpaEntity entity) {
        Set<UUID> rolesIds = usuariosRoles.findByUsuarioId(entity.getId()).stream()
                .map(UsuarioRolJpaEntity::getRolId)
                .collect(Collectors.toSet());
        return UsuarioMapper.aDominio(entity, rolesIds);
    }
}
