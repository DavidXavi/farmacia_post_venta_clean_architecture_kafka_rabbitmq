package com.posfarmacia.adapters.persistence.mapper.promocion;

import com.posfarmacia.adapters.persistence.entity.promocion.CondicionPromocionJpaEntity;
import com.posfarmacia.adapters.persistence.entity.promocion.PromocionJpaEntity;
import com.posfarmacia.domain.model.promocion.CondicionPromocion;
import com.posfarmacia.domain.model.promocion.Promocion;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** Traduce entre la entidad JPA de promociones y el modelo de dominio {@code Promocion}. */
@Component
public class PromocionMapper {

    public Promocion toDomain(PromocionJpaEntity entity) {
        List<CondicionPromocion> condiciones = entity.getCondiciones().stream()
                .map(this::toDomain)
                .toList();

        return new Promocion(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getTipoBeneficio(),
                entity.getValorBeneficio(),
                entity.isRequiereCliente(),
                new Cantidad(entity.getCantidadMinima()),
                new PeriodoVigencia(entity.getVigenciaInicio(), entity.getVigenciaFin()),
                entity.isActiva(),
                condiciones);
    }

    private CondicionPromocion toDomain(CondicionPromocionJpaEntity entity) {
        return new CondicionPromocion(entity.getProductoId());
    }

    /**
     * Reconstruye la entidad JPA (alta o actualizacion) a partir del dominio. Las condiciones se
     * regeneran por completo en cada guardado: {@code CondicionPromocion} no conserva un id de
     * fila propio (solo el {@code productoId}), asi que orphanRemoval + cascade sobre
     * {@code promocion_condiciones} se encargan de sincronizar la tabla con la lista vigente.
     */
    public PromocionJpaEntity toEntity(Promocion promocion) {
        PromocionJpaEntity entity = new PromocionJpaEntity(
                promocion.getId(),
                promocion.getNombre(),
                promocion.getDescripcion(),
                promocion.getTipoBeneficio(),
                promocion.getValorBeneficio(),
                promocion.isRequiereCliente(),
                promocion.getCantidadMinima().valor(),
                promocion.getVigencia().inicio(),
                promocion.getVigencia().fin(),
                promocion.isActiva());
        for (CondicionPromocion condicion : promocion.getCondiciones()) {
            entity.agregarCondicion(new CondicionPromocionJpaEntity(UUID.randomUUID(), condicion.productoId()));
        }
        return entity;
    }
}
