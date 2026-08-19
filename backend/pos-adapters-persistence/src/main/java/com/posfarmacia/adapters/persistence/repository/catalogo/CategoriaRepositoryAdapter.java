package com.posfarmacia.adapters.persistence.repository.catalogo;

import com.posfarmacia.adapters.persistence.mapper.catalogo.CategoriaMapper;
import com.posfarmacia.usecases.port.out.inventario.CategoriaRepositoryPort;
import com.posfarmacia.domain.model.catalogo.Categoria;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final CategoriaJpaRepository jpaRepository;

    public CategoriaRepositoryAdapter(CategoriaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        var guardado = jpaRepository.save(CategoriaMapper.aEntidad(categoria));
        return CategoriaMapper.aDominio(guardado);
    }

    @Override
    public List<Categoria> listarTodos() {
        return jpaRepository.findAll().stream().map(CategoriaMapper::aDominio).toList();
    }

    @Override
    public Optional<Categoria> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(CategoriaMapper::aDominio);
    }
}
