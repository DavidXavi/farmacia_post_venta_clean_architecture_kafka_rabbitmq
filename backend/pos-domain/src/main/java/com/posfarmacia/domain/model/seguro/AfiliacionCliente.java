package com.posfarmacia.domain.model.seguro;

import com.posfarmacia.domain.enums.EstadoAfiliacion;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Afiliacion de un {@code Cliente} a un {@link ConvenioSeguro} (RN22-RN23): solo se
 * aplica la cobertura cuando la afiliacion esta activa y dentro de su periodo de vigencia.
 */
public final class AfiliacionCliente extends Entidad {

    private final UUID clienteId;
    private final UUID convenioId;
    private final PeriodoVigencia vigencia;
    private EstadoAfiliacion estado;

    public AfiliacionCliente(UUID clienteId, UUID convenioId, LocalDate vigenciaInicio, LocalDate vigenciaFin) {
        super();
        this.clienteId = clienteId;
        this.convenioId = convenioId;
        this.vigencia = new PeriodoVigencia(vigenciaInicio, vigenciaFin);
        this.estado = EstadoAfiliacion.ACTIVA;
    }

    public AfiliacionCliente(UUID id, UUID clienteId, UUID convenioId, LocalDate vigenciaInicio,
                             LocalDate vigenciaFin, EstadoAfiliacion estado) {
        super(id);
        this.clienteId = clienteId;
        this.convenioId = convenioId;
        this.vigencia = new PeriodoVigencia(vigenciaInicio, vigenciaFin);
        this.estado = estado;
    }

    public boolean estaActivaYVigente(LocalDate hoy) {
        return estado == EstadoAfiliacion.ACTIVA && vigencia.estaVigente(hoy);
    }

    public void suspender() {
        this.estado = EstadoAfiliacion.SUSPENDIDA;
    }

    public void reactivar() {
        this.estado = EstadoAfiliacion.ACTIVA;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public UUID getConvenioId() {
        return convenioId;
    }

    public PeriodoVigencia getVigencia() {
        return vigencia;
    }

    public EstadoAfiliacion getEstado() {
        return estado;
    }
}
