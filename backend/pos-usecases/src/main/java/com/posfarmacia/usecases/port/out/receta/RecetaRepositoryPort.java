package com.posfarmacia.usecases.port.out.receta;

import com.posfarmacia.domain.model.receta.Receta;
import com.posfarmacia.domain.model.receta.UsoReceta;
import com.posfarmacia.domain.valueobject.NumeroReceta;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida del contexto de recetas. La implementacion (adaptador de persistencia)
 * es responsable de cargar la receta con un bloqueo (optimista u pesimista) cuando
 * {@link #guardar(Receta)} vaya a persistir un cambio de estado producido por
 * {@code Receta.marcarUtilizada()}, para sostener RN20 ante confirmaciones concurrentes.
 */
public interface RecetaRepositoryPort {

    Optional<Receta> buscarPorId(UUID id);

    Optional<Receta> buscarPorNumero(NumeroReceta numero);

    /** Inserta una receta nueva (RF07: {@code POST /api/recetas}). */
    Receta crear(Receta receta);

    /**
     * Persiste cambios de estado sobre una receta ya existente (aprobar/rechazar,
     * marcar utilizada). Ver la nota de clase sobre bloqueo optimista/pesimista.
     */
    Receta guardar(Receta receta);

    void registrarUso(UsoReceta uso);

    List<UsoReceta> listarUsosPorReceta(UUID recetaId);
}
