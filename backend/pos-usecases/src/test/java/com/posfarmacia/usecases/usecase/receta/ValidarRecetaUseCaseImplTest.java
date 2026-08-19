package com.posfarmacia.usecases.usecase.receta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.posfarmacia.usecases.port.in.receta.ValidarRecetaCommand;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaResultado;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.receta.RecetaRepositoryPort;
import com.posfarmacia.domain.enums.EstadoReceta;
import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.RecetaYaUtilizadaException;
import com.posfarmacia.domain.model.receta.Receta;
import com.posfarmacia.domain.model.receta.UsoReceta;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.NumeroReceta;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidarRecetaUseCaseImplTest {

    private final RecetaRepositoryPort recetaRepository = mock(RecetaRepositoryPort.class);
    private final ClockPort clock = mock(ClockPort.class);
    private final UUID productoId = UUID.randomUUID();
    private ValidarRecetaUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidarRecetaUseCaseImpl(recetaRepository, clock);
        when(clock.hoy()).thenReturn(LocalDate.of(2026, 7, 27));
        when(clock.ahora()).thenReturn(Instant.parse("2026-07-27T10:00:00Z"));
    }

    private Receta recetaRetenidaAprobada() {
        Receta receta = new Receta(
                new NumeroReceta("R-0001"),
                TipoReceta.ESPECIAL_RETENIDA,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                productoId,
                null,
                "Paciente",
                "Profesional",
                "dosis",
                new Cantidad(10),
                null);
        receta.aprobar();
        return receta;
    }

    @Test
    void lanzaEntidadNoEncontradaSiLaRecetaNoExiste() {
        when(recetaRepository.buscarPorId(any())).thenReturn(Optional.empty());

        ValidarRecetaCommand command = new ValidarRecetaCommand(UUID.randomUUID(), productoId, new Cantidad(1), null);

        assertThatThrownBy(() -> useCase.validar(command)).isInstanceOf(EntidadNoEncontradaException.class);
    }

    @Test
    void sinVentaIdSoloValidaYNoRegistraUso() {
        Receta receta = recetaRetenidaAprobada();
        when(recetaRepository.buscarPorId(receta.getId())).thenReturn(Optional.of(receta));

        ValidarRecetaCommand command = new ValidarRecetaCommand(receta.getId(), productoId, new Cantidad(1), null);
        ValidarRecetaResultado resultado = useCase.validar(command);

        assertThat(resultado.usoRegistrado()).isFalse();
        assertThat(resultado.estado()).isEqualTo(EstadoReceta.APROBADA);
        verify(recetaRepository, never()).guardar(any());
        verify(recetaRepository, never()).registrarUso(any());
    }

    @Test
    void conVentaIdConfirmaUsoYMarcaRecetaRetenidaComoUtilizada() {
        Receta receta = recetaRetenidaAprobada();
        when(recetaRepository.buscarPorId(receta.getId())).thenReturn(Optional.of(receta));
        UUID ventaId = UUID.randomUUID();

        ValidarRecetaCommand command = new ValidarRecetaCommand(receta.getId(), productoId, new Cantidad(1), ventaId);
        ValidarRecetaResultado resultado = useCase.validar(command);

        assertThat(resultado.usoRegistrado()).isTrue();
        assertThat(resultado.estado()).isEqualTo(EstadoReceta.UTILIZADA);
        assertThat(resultado.retenidaEnBotica()).isTrue();
        verify(recetaRepository, times(1)).guardar(receta);
        verify(recetaRepository, times(1)).registrarUso(any(UsoReceta.class));
    }

    @Test
    void unaSegundaConfirmacionSobreLaMismaRecetaRetenidaYaUtilizadaFalla() {
        Receta receta = recetaRetenidaAprobada();
        when(recetaRepository.buscarPorId(receta.getId())).thenReturn(Optional.of(receta));
        UUID ventaId = UUID.randomUUID();
        ValidarRecetaCommand command = new ValidarRecetaCommand(receta.getId(), productoId, new Cantidad(1), ventaId);

        useCase.validar(command);

        assertThatThrownBy(() -> useCase.validar(command)).isInstanceOf(RecetaYaUtilizadaException.class);
    }
}
