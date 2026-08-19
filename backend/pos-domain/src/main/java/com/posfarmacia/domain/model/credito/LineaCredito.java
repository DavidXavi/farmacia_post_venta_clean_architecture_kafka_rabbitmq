package com.posfarmacia.domain.model.credito;

import com.posfarmacia.domain.enums.EstadoLineaCredito;
import com.posfarmacia.domain.exception.LineaCreditoInvalidaException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Linea de credito de farmacia asignada a un cliente (RF11, RN28-RN32).
 */
public final class LineaCredito extends Entidad {

    private final UUID clienteId;
    private final Dinero montoAutorizado;
    private final PeriodoVigencia vigencia;
    private Dinero saldoDisponible;
    private EstadoLineaCredito estado;

    public LineaCredito(UUID clienteId, Dinero montoAutorizado, LocalDate vigenciaInicio, LocalDate vigenciaFin) {
        super();
        this.clienteId = clienteId;
        this.montoAutorizado = montoAutorizado;
        this.saldoDisponible = montoAutorizado;
        this.vigencia = new PeriodoVigencia(vigenciaInicio, vigenciaFin);
        this.estado = EstadoLineaCredito.ACTIVA;
    }

    public LineaCredito(UUID id, UUID clienteId, Dinero montoAutorizado, Dinero saldoDisponible,
                        LocalDate vigenciaInicio, LocalDate vigenciaFin, EstadoLineaCredito estado) {
        super(id);
        this.clienteId = clienteId;
        this.montoAutorizado = montoAutorizado;
        this.saldoDisponible = saldoDisponible;
        this.vigencia = new PeriodoVigencia(vigenciaInicio, vigenciaFin);
        this.estado = estado;
    }

    public boolean estaActivaYVigente(LocalDate hoy) {
        return estado == EstadoLineaCredito.ACTIVA && vigencia.estaVigente(hoy);
    }

    /** RN30-RN31: reduce el saldo disponible al confirmar una venta a credito. */
    public void consumir(Dinero monto) {
        if (monto.esMayorQue(saldoDisponible)) {
            throw new LineaCreditoInvalidaException("El importe financiado supera el saldo disponible del cliente.");
        }
        this.saldoDisponible = saldoDisponible.restar(monto);
    }

    /** RN32: devuelve el importe a la linea, sin superar el monto autorizado. */
    public void revertir(Dinero monto) {
        BigDecimal nuevoSaldo = saldoDisponible.monto().add(monto.monto()).min(montoAutorizado.monto());
        this.saldoDisponible = new Dinero(nuevoSaldo);
    }

    public void bloquear() {
        this.estado = EstadoLineaCredito.BLOQUEADA;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public Dinero getMontoAutorizado() {
        return montoAutorizado;
    }

    public Dinero getSaldoDisponible() {
        return saldoDisponible;
    }

    public PeriodoVigencia getVigencia() {
        return vigencia;
    }

    public EstadoLineaCredito getEstado() {
        return estado;
    }
}
