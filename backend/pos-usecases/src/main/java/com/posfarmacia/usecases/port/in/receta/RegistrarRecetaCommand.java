package com.posfarmacia.usecases.port.in.receta;

import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entrada de {@link RegistrarRecetaUseCase}: registra una receta nueva en estado
 * {@code PENDIENTE}, previa a su revision clinica (ver {@link RevisarRecetaUseCase})
 * y a su validacion para dispensacion (ver {@link ValidarRecetaUseCase}).
 */
public record RegistrarRecetaCommand(
        String numero,
        TipoReceta tipo,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        UUID productoId,
        UUID clienteId,
        String datosPaciente,
        String datosProfesional,
        String dosis,
        Cantidad cantidadAutorizada,
        String archivoRespaldoUrl) {
}
