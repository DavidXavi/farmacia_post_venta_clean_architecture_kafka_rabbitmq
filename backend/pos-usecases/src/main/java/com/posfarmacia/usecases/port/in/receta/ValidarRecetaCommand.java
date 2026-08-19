package com.posfarmacia.usecases.port.in.receta;

import com.posfarmacia.domain.valueobject.Cantidad;
import java.util.UUID;

/**
 * Entrada de {@link ValidarRecetaUseCase}.
 *
 * <p>{@code ventaId} distingue las dos formas en que RF07 se dispara:
 * <ul>
 *   <li>{@code ventaId == null}: evaluacion previa (por ejemplo, el quimico farmaceutico
 *       revisando la receta antes de agregarla a una venta). Solo valida, sin efectos.</li>
 *   <li>{@code ventaId != null}: confirmacion de uso al momento de dispensar dentro de una
 *       venta ya iniciada. Ademas de validar, registra el {@code UsoReceta} y, si la receta
 *       es especial retenida, la marca como utilizada (RN18/RN20) de forma atomica.</li>
 * </ul>
 */
public record ValidarRecetaCommand(UUID recetaId, UUID productoId, Cantidad cantidad, UUID ventaId) {
}
