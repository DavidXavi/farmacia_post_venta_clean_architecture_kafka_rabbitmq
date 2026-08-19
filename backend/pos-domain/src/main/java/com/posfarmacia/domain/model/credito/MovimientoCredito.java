package com.posfarmacia.domain.model.credito;

import com.posfarmacia.domain.enums.TipoMovimientoCredito;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.Instant;
import java.util.UUID;

/**
 * Movimiento (consumo o reversion) sobre una {@link LineaCredito}. La fecha se recibe
 * como parametro (via ClockPort en el caso de uso) para mantener el dominio comprobable.
 */
public final class MovimientoCredito extends Entidad {

    private final UUID lineaCreditoId;
    private final UUID ventaId;
    private final TipoMovimientoCredito tipo;
    private final Dinero monto;
    private final Instant fecha;

    public MovimientoCredito(UUID lineaCreditoId, UUID ventaId, TipoMovimientoCredito tipo, Dinero monto, Instant fecha) {
        super();
        this.lineaCreditoId = lineaCreditoId;
        this.ventaId = ventaId;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
    }

    public MovimientoCredito(UUID id, UUID lineaCreditoId, UUID ventaId, TipoMovimientoCredito tipo,
                             Dinero monto, Instant fecha) {
        super(id);
        this.lineaCreditoId = lineaCreditoId;
        this.ventaId = ventaId;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
    }

    public UUID getLineaCreditoId() {
        return lineaCreditoId;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public TipoMovimientoCredito getTipo() {
        return tipo;
    }

    public Dinero getMonto() {
        return monto;
    }

    public Instant getFecha() {
        return fecha;
    }
}
