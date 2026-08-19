package com.posfarmacia.adapters.persistence.repository.receta;

import com.posfarmacia.adapters.persistence.entity.receta.RecetaJpaEntity;
import com.posfarmacia.adapters.persistence.entity.receta.UsoRecetaJpaEntity;
import com.posfarmacia.adapters.persistence.mapper.receta.RecetaMapper;
import com.posfarmacia.usecases.port.out.receta.RecetaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.RecetaYaUtilizadaException;
import com.posfarmacia.domain.model.receta.Receta;
import com.posfarmacia.domain.model.receta.UsoReceta;
import com.posfarmacia.domain.valueobject.NumeroReceta;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

/**
 * Implementacion de {@link RecetaRepositoryPort} con Spring Data JPA.
 *
 * <p>{@link #guardar(Receta)} vuelve a leer la entidad gestionada (misma transaccion,
 * mismo contexto de persistencia) para que Hibernate verifique la columna
 * {@code version} al hacer flush. Si otra transaccion confirmo primero el uso de la
 * misma receta retenida, Hibernate lanza {@link OptimisticLockingFailureException};
 * aqui se traduce a {@link RecetaYaUtilizadaException} para no filtrar una excepcion
 * de framework hacia pos-usecases/pos-domain (RN20).
 */
@Repository
public class RecetaRepositoryAdapter implements RecetaRepositoryPort {

    private final RecetaJpaRepository recetaJpaRepository;
    private final UsoRecetaJpaRepository usoRecetaJpaRepository;

    public RecetaRepositoryAdapter(
            RecetaJpaRepository recetaJpaRepository, UsoRecetaJpaRepository usoRecetaJpaRepository) {
        this.recetaJpaRepository = Objects.requireNonNull(recetaJpaRepository);
        this.usoRecetaJpaRepository = Objects.requireNonNull(usoRecetaJpaRepository);
    }

    @Override
    public Optional<Receta> buscarPorId(UUID id) {
        return recetaJpaRepository.findById(id).map(RecetaMapper::toDomain);
    }

    @Override
    public Optional<Receta> buscarPorNumero(NumeroReceta numero) {
        return recetaJpaRepository.findByNumero(numero.valor()).map(RecetaMapper::toDomain);
    }

    @Override
    public Receta crear(Receta receta) {
        RecetaJpaEntity entity = RecetaMapper.toNuevaEntity(receta);
        RecetaJpaEntity guardada = recetaJpaRepository.save(entity);
        return RecetaMapper.toDomain(guardada);
    }

    @Override
    public Receta guardar(Receta receta) {
        try {
            RecetaJpaEntity entity = recetaJpaRepository.findById(receta.getId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("La receta indicada no existe."));
            RecetaMapper.actualizarEntidadDesdeDominio(entity, receta);
            RecetaJpaEntity guardada = recetaJpaRepository.save(entity);
            return RecetaMapper.toDomain(guardada);
        } catch (OptimisticLockingFailureException ex) {
            throw new RecetaYaUtilizadaException();
        }
    }

    @Override
    public void registrarUso(UsoReceta uso) {
        usoRecetaJpaRepository.save(RecetaMapper.toEntity(uso));
    }

    @Override
    public List<UsoReceta> listarUsosPorReceta(UUID recetaId) {
        return usoRecetaJpaRepository.findByRecetaId(recetaId).stream().map(RecetaMapper::toDomain).toList();
    }
}
