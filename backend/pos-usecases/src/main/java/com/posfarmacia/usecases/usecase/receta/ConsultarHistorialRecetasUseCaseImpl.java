package com.posfarmacia.usecases.usecase.receta;

import com.posfarmacia.usecases.port.in.receta.ConsultarHistorialRecetasUseCase;
import com.posfarmacia.usecases.port.in.receta.UsoRecetaView;
import com.posfarmacia.usecases.port.out.receta.RecetaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.receta.UsoReceta;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ConsultarHistorialRecetasUseCaseImpl implements ConsultarHistorialRecetasUseCase {

    private final RecetaRepositoryPort recetaRepository;

    public ConsultarHistorialRecetasUseCaseImpl(RecetaRepositoryPort recetaRepository) {
        this.recetaRepository = Objects.requireNonNull(recetaRepository);
    }

    @Override
    public List<UsoRecetaView> consultarHistorial(UUID recetaId) {
        recetaRepository.buscarPorId(recetaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("La receta indicada no existe."));

        return recetaRepository.listarUsosPorReceta(recetaId).stream()
                .map(this::toView)
                .toList();
    }

    private UsoRecetaView toView(UsoReceta uso) {
        return new UsoRecetaView(uso.getId(), uso.getRecetaId(), uso.getVentaId(), uso.getFecha());
    }
}
