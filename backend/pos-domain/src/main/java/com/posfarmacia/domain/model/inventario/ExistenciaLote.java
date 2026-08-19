package com.posfarmacia.domain.model.inventario;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.Instant;
import java.util.UUID;

/**
 * Rollup de stock actual por producto+local (RF04), recalculado siempre desde la suma de los lotes:
 * nunca es la fuente de verdad, solo una vista consolidada para consulta rapida.
 * La fecha de actualizacion se recibe como parametro (via ClockPort en la capa de aplicacion), nunca
 * se llama Instant.now() dentro del dominio.
 */
public final class ExistenciaLote extends Entidad {

    private final UUID productoId;
    private final UUID localId;
    private Cantidad cantidadActual;
    private Instant actualizadoEn;

    public ExistenciaLote(UUID productoId, UUID localId, Cantidad cantidadActual, Instant actualizadoEn) {
        super();
        this.productoId = requireNoNulo(productoId, "El producto de la existencia es obligatorio.");
        this.localId = requireNoNulo(localId, "El local de la existencia es obligatorio.");
        this.cantidadActual = requireNoNulo(cantidadActual, "La cantidad actual de la existencia es obligatoria.");
        this.actualizadoEn = requireNoNulo(actualizadoEn, "La fecha de actualizacion es obligatoria.");
    }

    private ExistenciaLote(UUID id, UUID productoId, UUID localId, Cantidad cantidadActual, Instant actualizadoEn) {
        super(id);
        this.productoId = productoId;
        this.localId = localId;
        this.cantidadActual = cantidadActual;
        this.actualizadoEn = actualizadoEn;
    }

    /** Usado por el mapper de persistencia para reconstruir el agregado desde su estado guardado. */
    public static ExistenciaLote reconstruir(UUID id, UUID productoId, UUID localId, Cantidad cantidadActual,
            Instant actualizadoEn) {
        return new ExistenciaLote(id, productoId, localId, cantidadActual, actualizadoEn);
    }

    private static <T> T requireNoNulo(T valor, String mensaje) {
        if (valor == null) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    public void actualizar(Cantidad cantidadActual, Instant actualizadoEn) {
        this.cantidadActual = requireNoNulo(cantidadActual, "La cantidad actual de la existencia es obligatoria.");
        this.actualizadoEn = requireNoNulo(actualizadoEn, "La fecha de actualizacion es obligatoria.");
    }

    public UUID getProductoId() {
        return productoId;
    }

    public UUID getLocalId() {
        return localId;
    }

    public Cantidad getCantidadActual() {
        return cantidadActual;
    }

    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
