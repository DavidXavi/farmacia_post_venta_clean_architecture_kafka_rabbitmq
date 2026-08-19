package com.posfarmacia.adapters.persistence.entity.seguro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "afiliaciones_cliente")
public class AfiliacionClienteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "convenio_id", nullable = false)
    private UUID convenioId;

    @Column(name = "vigencia_inicio")
    private LocalDate vigenciaInicio;

    @Column(name = "vigencia_fin")
    private LocalDate vigenciaFin;

    @Column(nullable = false, length = 20)
    private String estado;

    protected AfiliacionClienteJpaEntity() {
    }

    public AfiliacionClienteJpaEntity(UUID id, UUID clienteId, UUID convenioId, LocalDate vigenciaInicio,
                                       LocalDate vigenciaFin, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.convenioId = convenioId;
        this.vigenciaInicio = vigenciaInicio;
        this.vigenciaFin = vigenciaFin;
        this.estado = estado;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public UUID getConvenioId() {
        return convenioId;
    }

    public LocalDate getVigenciaInicio() {
        return vigenciaInicio;
    }

    public LocalDate getVigenciaFin() {
        return vigenciaFin;
    }

    public String getEstado() {
        return estado;
    }
}
