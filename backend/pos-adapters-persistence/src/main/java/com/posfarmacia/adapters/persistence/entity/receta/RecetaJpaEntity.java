package com.posfarmacia.adapters.persistence.entity.receta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidad JPA de recetas. Se mapea a/desde {@code com.posfarmacia.domain.model.receta.Receta}
 * mediante {@code RecetaMapper}; nunca se usa como modelo de negocio.
 *
 * <p>Bloqueo elegido para RN20 (evitar que dos confirmaciones concurrentes reutilicen la
 * misma receta retenida): optimista via {@code @Version}. Se prefirio sobre
 * {@code @Lock(PESSIMISTIC_WRITE)} porque no exige mantener una fila bloqueada durante toda
 * la transaccion de venta (que puede incluir otras validaciones) y porque Hibernate lo
 * resuelve de forma estandar con cualquier motor, incluido Postgres. Un conflicto de
 * version se traduce en {@code RecetaRepositoryAdapter.guardar} a
 * {@code RecetaYaUtilizadaException} para no filtrar excepciones de framework hacia
 * pos-usecases/pos-domain.
 */
@Entity
@Table(name = "recetas")
public class RecetaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "numero", nullable = false, unique = true, length = 50)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private com.posfarmacia.domain.enums.TipoReceta tipo;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    @Column(name = "cliente_id")
    private UUID clienteId;

    @Column(name = "datos_paciente", nullable = false, length = 300)
    private String datosPaciente;

    @Column(name = "datos_profesional", nullable = false, length = 300)
    private String datosProfesional;

    @Column(name = "dosis", length = 300)
    private String dosis;

    @Column(name = "cantidad_autorizada", nullable = false)
    private int cantidadAutorizada;

    @Column(name = "archivo_respaldo_url", length = 500)
    private String archivoRespaldoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private com.posfarmacia.domain.enums.EstadoReceta estado;

    @Column(name = "retenida_en_botica", nullable = false)
    private boolean retenidaEnBotica;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public RecetaJpaEntity() {
        // JPA
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public com.posfarmacia.domain.enums.TipoReceta getTipo() {
        return tipo;
    }

    public void setTipo(com.posfarmacia.domain.enums.TipoReceta tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public void setProductoId(UUID productoId) {
        this.productoId = productoId;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public String getDatosPaciente() {
        return datosPaciente;
    }

    public void setDatosPaciente(String datosPaciente) {
        this.datosPaciente = datosPaciente;
    }

    public String getDatosProfesional() {
        return datosProfesional;
    }

    public void setDatosProfesional(String datosProfesional) {
        this.datosProfesional = datosProfesional;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public int getCantidadAutorizada() {
        return cantidadAutorizada;
    }

    public void setCantidadAutorizada(int cantidadAutorizada) {
        this.cantidadAutorizada = cantidadAutorizada;
    }

    public String getArchivoRespaldoUrl() {
        return archivoRespaldoUrl;
    }

    public void setArchivoRespaldoUrl(String archivoRespaldoUrl) {
        this.archivoRespaldoUrl = archivoRespaldoUrl;
    }

    public com.posfarmacia.domain.enums.EstadoReceta getEstado() {
        return estado;
    }

    public void setEstado(com.posfarmacia.domain.enums.EstadoReceta estado) {
        this.estado = estado;
    }

    public boolean isRetenidaEnBotica() {
        return retenidaEnBotica;
    }

    public void setRetenidaEnBotica(boolean retenidaEnBotica) {
        this.retenidaEnBotica = retenidaEnBotica;
    }

    public long getVersion() {
        return version;
    }
}
