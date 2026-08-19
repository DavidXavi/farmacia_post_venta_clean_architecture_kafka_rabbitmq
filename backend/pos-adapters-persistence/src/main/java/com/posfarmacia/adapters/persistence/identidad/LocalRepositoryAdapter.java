package com.posfarmacia.adapters.persistence.identidad;

import com.posfarmacia.adapters.persistence.mapper.identidad.LocalMapper;
import com.posfarmacia.adapters.persistence.repository.identidad.LocalJpaRepository;
import com.posfarmacia.usecases.port.out.identidad.LocalRepositoryPort;
import com.posfarmacia.domain.model.identidad.Local;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LocalRepositoryAdapter implements LocalRepositoryPort {

    private final LocalJpaRepository locales;

    public LocalRepositoryAdapter(LocalJpaRepository locales) {
        this.locales = locales;
    }

    @Override
    public List<Local> listarTodos() {
        return locales.findAll().stream().map(LocalMapper::aDominio).toList();
    }
}
