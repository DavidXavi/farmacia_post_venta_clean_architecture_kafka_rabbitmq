package com.posfarmacia.adapters.persistence.mapper.receta;

import com.posfarmacia.adapters.persistence.entity.receta.RecetaJpaEntity;
import com.posfarmacia.adapters.persistence.entity.receta.UsoRecetaJpaEntity;
import com.posfarmacia.domain.model.receta.Receta;
import com.posfarmacia.domain.model.receta.UsoReceta;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.NumeroReceta;

/** Traduce entre el modelo de dominio de recetas y sus entidades JPA. Sin estado, sin Spring. */
public final class RecetaMapper {

    private RecetaMapper() {
    }

    public static Receta toDomain(RecetaJpaEntity entity) {
        return Receta.reconstruir(
                entity.getId(),
                new NumeroReceta(entity.getNumero()),
                entity.getTipo(),
                entity.getFechaEmision(),
                entity.getFechaVencimiento(),
                entity.getProductoId(),
                entity.getClienteId(),
                entity.getDatosPaciente(),
                entity.getDatosProfesional(),
                entity.getDosis(),
                new Cantidad(entity.getCantidadAutorizada()),
                entity.getArchivoRespaldoUrl(),
                entity.getEstado(),
                entity.isRetenidaEnBotica());
    }

    public static RecetaJpaEntity toNuevaEntity(Receta receta) {
        RecetaJpaEntity entity = new RecetaJpaEntity();
        entity.setId(receta.getId());
        copiarCampos(entity, receta);
        return entity;
    }

    /** Copia el estado mutable del dominio sobre una entidad ya gestionada por el EntityManager. */
    public static void actualizarEntidadDesdeDominio(RecetaJpaEntity entity, Receta receta) {
        copiarCampos(entity, receta);
    }

    private static void copiarCampos(RecetaJpaEntity entity, Receta receta) {
        entity.setNumero(receta.getNumero().valor());
        entity.setTipo(receta.getTipo());
        entity.setFechaEmision(receta.getFechaEmision());
        entity.setFechaVencimiento(receta.getFechaVencimiento());
        entity.setProductoId(receta.getProductoId());
        entity.setClienteId(receta.getClienteId());
        entity.setDatosPaciente(receta.getDatosPaciente());
        entity.setDatosProfesional(receta.getDatosProfesional());
        entity.setDosis(receta.getDosis());
        entity.setCantidadAutorizada(receta.getCantidadAutorizada().valor());
        entity.setArchivoRespaldoUrl(receta.getArchivoRespaldoUrl());
        entity.setEstado(receta.getEstado());
        entity.setRetenidaEnBotica(receta.isRetenidaEnBotica());
    }

    public static UsoReceta toDomain(UsoRecetaJpaEntity entity) {
        return UsoReceta.reconstruir(entity.getId(), entity.getRecetaId(), entity.getVentaId(), entity.getFecha());
    }

    public static UsoRecetaJpaEntity toEntity(UsoReceta uso) {
        UsoRecetaJpaEntity entity = new UsoRecetaJpaEntity();
        entity.setId(uso.getId());
        entity.setRecetaId(uso.getRecetaId());
        entity.setVentaId(uso.getVentaId());
        entity.setFecha(uso.getFecha());
        return entity;
    }
}
