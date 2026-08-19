package com.posfarmacia.adapters.persistence.venta;

import com.posfarmacia.adapters.persistence.mapper.venta.FormaPagoMapper;
import com.posfarmacia.adapters.persistence.repository.venta.FormaPagoJpaRepository;
import com.posfarmacia.usecases.port.out.venta.FormaPagoRepositoryPort;
import com.posfarmacia.domain.enums.TipoFormaPago;
import com.posfarmacia.domain.model.venta.FormaPago;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FormaPagoRepositoryAdapter implements FormaPagoRepositoryPort {

    private final FormaPagoJpaRepository jpaRepository;

    public FormaPagoRepositoryAdapter(FormaPagoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FormaPago guardar(FormaPago formaPago) {
        var guardada = jpaRepository.save(FormaPagoMapper.aEntidad(formaPago));
        return FormaPagoMapper.aDominio(guardada);
    }

    @Override
    public Optional<FormaPago> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(FormaPagoMapper::aDominio);
    }

    @Override
    public Optional<FormaPago> buscarPorTipo(TipoFormaPago tipo) {
        return jpaRepository.findFirstByTipoAndActivoTrue(tipo.name()).map(FormaPagoMapper::aDominio);
    }

    @Override
    public List<FormaPago> listarActivas() {
        return jpaRepository.findByActivoTrue().stream().map(FormaPagoMapper::aDominio).toList();
    }
}
