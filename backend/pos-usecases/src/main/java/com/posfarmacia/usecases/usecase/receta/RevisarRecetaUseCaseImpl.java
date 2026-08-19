package com.posfarmacia.usecases.usecase.receta;

import com.posfarmacia.usecases.port.in.receta.RevisarRecetaCommand;
import com.posfarmacia.usecases.port.in.receta.RevisarRecetaUseCase;
import com.posfarmacia.usecases.port.out.receta.RecetaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.receta.Receta;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso detras de {@code POST /api/recetas/validaciones}: el quimico farmaceutico
 * aprueba o rechaza una receta ya registrada. Traducido de {@code ValidarRecetaUseCase}
 * (.NET); ver {@link RevisarRecetaUseCase} para la razon del renombre en Java.
 */
public class RevisarRecetaUseCaseImpl implements RevisarRecetaUseCase {

    private final RecetaRepositoryPort recetaRepository;

    public RevisarRecetaUseCaseImpl(RecetaRepositoryPort recetaRepository) {
        this.recetaRepository = Objects.requireNonNull(recetaRepository);
    }

    @Override
    @Transactional
    public Receta revisar(RevisarRecetaCommand command) {
        Receta receta = recetaRepository.buscarPorId(command.recetaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La receta indicada no existe."));

        if (command.aprobar()) {
            receta.aprobar();
        } else {
            receta.rechazar();
        }

        return recetaRepository.guardar(receta);
    }
}
