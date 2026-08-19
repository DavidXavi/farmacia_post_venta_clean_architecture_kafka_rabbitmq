package com.posfarmacia.domain.model.receta;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import java.time.Instant;
import java.util.UUID;

/**
 * Registro de cada uso (dispensacion) de una receta contra una venta concreta.
 * No existe como entidad separada en el .NET de referencia (solo se infiere de
 * Receta.MarcarUtilizadaYRetenida); se modela aqui, tal como sugiere el Word
 * seccion 6.1, para dejar trazabilidad de cada vez que una receta normal o
 * especial se reutiliza, y del momento exacto en que una especial retenida
 * queda consumida (RN18, RN20, RN21).
 */
public final class UsoReceta extends Entidad {

    private final UUID recetaId;
    private final UUID ventaId;
    private final Instant fecha;

    public UsoReceta(UUID recetaId, UUID ventaId, Instant fecha) {
        super();
        this.recetaId = requireNoNulo(recetaId, "recetaId");
        this.ventaId = requireNoNulo(ventaId, "ventaId");
        this.fecha = requireNoNulo(fecha, "fecha");
    }

    /** Reconstruccion desde persistencia. Uso exclusivo del mapper del adaptador. */
    public static UsoReceta reconstruir(UUID id, UUID recetaId, UUID ventaId, Instant fecha) {
        return new UsoReceta(id, recetaId, ventaId, fecha);
    }

    private UsoReceta(UUID id, UUID recetaId, UUID ventaId, Instant fecha) {
        super(id);
        this.recetaId = recetaId;
        this.ventaId = ventaId;
        this.fecha = fecha;
    }

    private static <T> T requireNoNulo(T valor, String campo) {
        if (valor == null) {
            throw new ValorInvalidoException("El campo " + campo + " de un uso de receta no puede ser nulo.");
        }
        return valor;
    }

    public UUID getRecetaId() {
        return recetaId;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public Instant getFecha() {
        return fecha;
    }
}
