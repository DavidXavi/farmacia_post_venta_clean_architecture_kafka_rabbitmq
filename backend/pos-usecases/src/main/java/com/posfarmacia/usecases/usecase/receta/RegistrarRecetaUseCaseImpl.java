package com.posfarmacia.usecases.usecase.receta;

import com.posfarmacia.usecases.port.in.receta.RegistrarRecetaCommand;
import com.posfarmacia.usecases.port.in.receta.RegistrarRecetaUseCase;
import com.posfarmacia.usecases.port.out.receta.RecetaRepositoryPort;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.receta.Receta;
import com.posfarmacia.domain.valueobject.NumeroReceta;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso detras de {@code POST /api/recetas}: registra una receta nueva en estado
 * {@code PENDIENTE}. Traducido de {@code RegistrarRecetaUseCase} (.NET).
 */
public class RegistrarRecetaUseCaseImpl implements RegistrarRecetaUseCase {

    private final RecetaRepositoryPort recetaRepository;

    public RegistrarRecetaUseCaseImpl(RecetaRepositoryPort recetaRepository) {
        this.recetaRepository = Objects.requireNonNull(recetaRepository);
    }

    @Override
    @Transactional
    public Receta registrar(RegistrarRecetaCommand command) {
        NumeroReceta numero = new NumeroReceta(command.numero());
        if (recetaRepository.buscarPorNumero(numero).isPresent()) {
            throw new ValorInvalidoException("Ya existe una receta registrada con ese numero.");
        }

        Receta receta = new Receta(
                numero,
                command.tipo(),
                command.fechaEmision(),
                command.fechaVencimiento(),
                command.productoId(),
                command.clienteId(),
                command.datosPaciente(),
                command.datosProfesional(),
                command.dosis(),
                command.cantidadAutorizada(),
                command.archivoRespaldoUrl());

        return recetaRepository.crear(receta);
    }
}
