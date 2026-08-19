package com.posfarmacia.usecases.usecase.receta;

import com.posfarmacia.usecases.port.in.receta.ValidarRecetaCommand;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaResultado;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.receta.RecetaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.receta.Receta;
import com.posfarmacia.domain.model.receta.UsoReceta;
import com.posfarmacia.domain.service.receta.ValidadorReceta;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso detras de {@code POST /api/recetas/validar} (RF07).
 *
 * <p>Si {@code command.ventaId()} viene informado, esta llamada representa el punto de
 * confirmacion de la dispensacion: ademas de validar, registra el {@link UsoReceta} y
 * marca la receta como utilizada cuando corresponde (RN18). Toda la operacion corre en
 * una unica transaccion para que la relectura de la receta en {@code guardar} sostenga
 * el bloqueo (optimista/pesimista) que decide el adaptador de persistencia y evite que
 * dos confirmaciones concurrentes reutilicen la misma receta retenida (RN20).
 */
@Transactional
public class ValidarRecetaUseCaseImpl implements ValidarRecetaUseCase {

    private final RecetaRepositoryPort recetaRepository;
    private final ClockPort clock;

    public ValidarRecetaUseCaseImpl(RecetaRepositoryPort recetaRepository, ClockPort clock) {
        this.recetaRepository = Objects.requireNonNull(recetaRepository);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public ValidarRecetaResultado validar(ValidarRecetaCommand command) {
        Receta receta = recetaRepository.buscarPorId(command.recetaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La receta indicada no existe."));

        ValidadorReceta.validarParaDispensacion(receta, command.productoId(), command.cantidad(), clock.hoy());

        boolean usoRegistrado = false;
        if (command.ventaId() != null) {
            receta.marcarUtilizada();
            recetaRepository.guardar(receta);
            recetaRepository.registrarUso(new UsoReceta(receta.getId(), command.ventaId(), clock.ahora()));
            usoRegistrado = true;
        }

        return new ValidarRecetaResultado(
                receta.getId(), receta.getNumero().valor(), receta.getTipo(), receta.getEstado(),
                receta.isRetenidaEnBotica(), usoRegistrado);
    }
}
