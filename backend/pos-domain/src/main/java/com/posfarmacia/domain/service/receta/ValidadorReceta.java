package com.posfarmacia.domain.service.receta;

import com.posfarmacia.domain.enums.EstadoReceta;
import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.exception.RecetaInvalidaException;
import com.posfarmacia.domain.exception.RecetaYaUtilizadaException;
import com.posfarmacia.domain.model.receta.Receta;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Politica de dominio pura (sin I/O) que decide si una receta ampara la dispensacion
 * de un medicamento controlado. Traducida de PosFarmacia.Domain.Services.ValidadorReceta
 * (.NET), con el orden de las validaciones corregido: en el .NET original, una receta
 * especial retenida ya utilizada caia primero en el chequeo generico "debe estar
 * aprobada" (mensaje incorrecto) porque nunca se llegaba a evaluar PuedeUsarseNuevamente().
 * Aqui se valida "ya utilizada" antes que "debe estar aprobada" para lanzar siempre
 * la excepcion especifica RecetaYaUtilizadaException (RN18/RN20).
 */
public final class ValidadorReceta {

    private ValidadorReceta() {
    }

    /**
     * @param receta receta presentada por el cliente.
     * @param productoId medicamento que se desea dispensar (RN15).
     * @param cantidad cantidad que se desea dispensar (RN15).
     * @param hoy fecha del sistema, obtenida vía ClockPort por quien invoque este metodo.
     */
    public static void validarParaDispensacion(Receta receta, UUID productoId, Cantidad cantidad, LocalDate hoy) {
        if (!receta.getProductoId().equals(productoId)) {
            throw new RecetaInvalidaException("La receta no corresponde al medicamento que se desea dispensar.");
        }

        if (cantidad.esMayorQue(receta.getCantidadAutorizada())) {
            throw new RecetaInvalidaException(
                    "La cantidad solicitada supera la cantidad autorizada en la receta.");
        }

        if (receta.getTipo() == TipoReceta.ESPECIAL_RETENIDA && receta.getEstado() == EstadoReceta.UTILIZADA) {
            throw new RecetaYaUtilizadaException();
        }

        if (receta.getEstado() != EstadoReceta.APROBADA) {
            throw new RecetaInvalidaException(
                    "La receta debe estar aprobada por el quimico farmaceutico antes de dispensar.");
        }

        if (receta.estaVencida(hoy)) {
            throw new RecetaInvalidaException("La receta se encuentra vencida.");
        }
    }
}
