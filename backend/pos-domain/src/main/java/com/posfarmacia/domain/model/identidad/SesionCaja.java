package com.posfarmacia.domain.model.identidad;

import com.posfarmacia.domain.enums.EstadoCaja;
import com.posfarmacia.domain.exception.CajaCerradaException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.Instant;
import java.util.UUID;

/**
 * Turno de una caja (RF02). Aplica RN01 (no se vende con la caja cerrada) mediante
 * {@link #asegurarAbierta()}, metodo que el contexto de Ventas debe invocar antes de
 * registrar cualquier venta, y calcula la diferencia de cierre (monto esperado vs
 * declarado). Equivalente a PosFarmacia.Domain.Entities.SesionCaja.
 */
public final class SesionCaja extends Entidad {

    private final UUID cajaId;
    private final UUID usuarioId;
    private final Instant fechaApertura;
    private final Dinero montoInicial;
    private Instant fechaCierre;
    private Dinero montoEsperado;
    private Dinero montoDeclarado;
    private Dinero diferencia;
    private String observacionCierre;
    private EstadoCaja estado;

    public SesionCaja(UUID cajaId, UUID usuarioId, Dinero montoInicial, Instant ahora) {
        super();
        this.cajaId = cajaId;
        this.usuarioId = usuarioId;
        this.montoInicial = montoInicial;
        this.fechaApertura = ahora;
        this.estado = EstadoCaja.ABIERTA;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public SesionCaja(UUID id, UUID cajaId, UUID usuarioId, Instant fechaApertura, Dinero montoInicial,
                       Instant fechaCierre, Dinero montoEsperado, Dinero montoDeclarado, Dinero diferencia,
                       String observacionCierre, EstadoCaja estado) {
        super(id);
        this.cajaId = cajaId;
        this.usuarioId = usuarioId;
        this.fechaApertura = fechaApertura;
        this.montoInicial = montoInicial;
        this.fechaCierre = fechaCierre;
        this.montoEsperado = montoEsperado;
        this.montoDeclarado = montoDeclarado;
        this.diferencia = diferencia;
        this.observacionCierre = observacionCierre;
        this.estado = estado;
    }

    /** RN01: ninguna venta puede registrarse si la sesion de caja no esta abierta. */
    public void asegurarAbierta() {
        if (!estaAbierta()) {
            throw new CajaCerradaException();
        }
    }

    public boolean estaAbierta() {
        return estado == EstadoCaja.ABIERTA;
    }

    /** Cierra el turno calculando la diferencia entre lo esperado y lo declarado por el cajero (RF02). */
    public void cerrar(Dinero montoEsperado, Dinero montoDeclarado, String observacion, Instant ahora) {
        if (!estaAbierta()) {
            throw new CajaCerradaException("La sesion de caja ya se encuentra cerrada.");
        }

        this.montoEsperado = montoEsperado;
        this.montoDeclarado = montoDeclarado;
        this.diferencia = new Dinero(montoDeclarado.monto().subtract(montoEsperado.monto()).abs());
        this.observacionCierre = observacion;
        this.fechaCierre = ahora;
        this.estado = EstadoCaja.CERRADA;
    }

    public UUID getCajaId() {
        return cajaId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public Instant getFechaApertura() {
        return fechaApertura;
    }

    public Dinero getMontoInicial() {
        return montoInicial;
    }

    public Instant getFechaCierre() {
        return fechaCierre;
    }

    public Dinero getMontoEsperado() {
        return montoEsperado;
    }

    public Dinero getMontoDeclarado() {
        return montoDeclarado;
    }

    public Dinero getDiferencia() {
        return diferencia;
    }

    public String getObservacionCierre() {
        return observacionCierre;
    }

    public EstadoCaja getEstado() {
        return estado;
    }
}
