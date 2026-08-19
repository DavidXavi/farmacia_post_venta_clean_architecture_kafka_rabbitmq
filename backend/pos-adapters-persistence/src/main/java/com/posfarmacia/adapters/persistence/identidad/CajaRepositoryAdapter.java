package com.posfarmacia.adapters.persistence.identidad;

import com.posfarmacia.adapters.persistence.mapper.identidad.CajaMapper;
import com.posfarmacia.adapters.persistence.repository.identidad.CajaJpaRepository;
import com.posfarmacia.usecases.port.out.identidad.CajaRepositoryPort;
import com.posfarmacia.domain.model.identidad.Caja;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class CajaRepositoryAdapter implements CajaRepositoryPort {

    private final CajaJpaRepository cajas;

    public CajaRepositoryAdapter(CajaJpaRepository cajas) {
        this.cajas = cajas;
    }

    @Override
    public Optional<Caja> buscarPorId(UUID id) {
        return cajas.findById(id).map(CajaMapper::aDominio);
    }

    @Override
    public List<Caja> listarTodas() {
        return cajas.findAll().stream().map(CajaMapper::aDominio).toList();
    }
}
