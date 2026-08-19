package com.posfarmacia.adapters.persistence.mapper.seguro;

import com.posfarmacia.adapters.persistence.entity.seguro.CoberturaSeguroJpaEntity;
import com.posfarmacia.adapters.persistence.entity.seguro.ConvenioSeguroJpaEntity;
import com.posfarmacia.domain.model.seguro.CoberturaConvenio;
import com.posfarmacia.domain.model.seguro.ConvenioSeguro;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ConvenioSeguroMapper {

    public ConvenioSeguro toDomain(ConvenioSeguroJpaEntity entity, List<CoberturaSeguroJpaEntity> coberturas) {
        List<CoberturaConvenio> coberturasDominio = coberturas.stream()
                .map(c -> new CoberturaConvenio(c.getId(), c.getConvenioId(), c.getProductoId(),
                        new Porcentaje(c.getPorcentajeCubierto())))
                .toList();
        return new ConvenioSeguro(entity.getId(), entity.getNombre(), entity.isActivo(), coberturasDominio);
    }

    public ConvenioSeguroJpaEntity toEntity(ConvenioSeguro convenio) {
        return new ConvenioSeguroJpaEntity(convenio.getId(), convenio.getNombre(), convenio.isActivo());
    }

    public CoberturaSeguroJpaEntity toEntity(CoberturaConvenio cobertura) {
        return new CoberturaSeguroJpaEntity(cobertura.getId(), cobertura.getConvenioId(), cobertura.getProductoId(),
                cobertura.getPorcentajeCubierto().valor());
    }
}
