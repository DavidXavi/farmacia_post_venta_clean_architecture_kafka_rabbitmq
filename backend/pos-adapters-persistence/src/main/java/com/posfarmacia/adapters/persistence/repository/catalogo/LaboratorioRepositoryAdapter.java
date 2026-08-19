package com.posfarmacia.adapters.persistence.repository.catalogo;

import com.posfarmacia.adapters.persistence.mapper.catalogo.LaboratorioMapper;
import com.posfarmacia.usecases.port.out.inventario.LaboratorioRepositoryPort;
import com.posfarmacia.domain.model.catalogo.Laboratorio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LaboratorioRepositoryAdapter implements LaboratorioRepositoryPort {

    private final LaboratorioJpaRepository jpaRepository;

    public LaboratorioRepositoryAdapter(LaboratorioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Laboratorio guardar(Laboratorio laboratorio) {
        var guardado = jpaRepository.save(LaboratorioMapper.aEntidad(laboratorio));
        return LaboratorioMapper.aDominio(guardado);
    }

    @Override
    public List<Laboratorio> listarTodos() {
        return jpaRepository.findAll().stream().map(LaboratorioMapper::aDominio).toList();
    }

    @Override
    public Optional<Laboratorio> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(LaboratorioMapper::aDominio);
    }
}
