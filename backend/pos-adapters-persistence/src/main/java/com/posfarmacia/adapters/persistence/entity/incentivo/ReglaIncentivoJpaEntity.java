package com.posfarmacia.adapters.persistence.entity.incentivo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidad JPA de {@code reglas_incentivo}. Se mapea a/desde
 * {@code com.posfarmacia.domain.model.incentivo.ReglaIncentivo} mediante {@code ReglaIncentivoMapper};
 * nunca se usa como modelo de negocio directamente.
 */
@Entity
@Table(name = "reglas_incentivo")
public class ReglaIncentivoJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "producto_id")
    private UUID productoId;

    @Column(name = "categoria_id")
    private UUID categoriaId;

    @Column(name = "monto_por_unidad", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoPorUnidad;

    @Column(name = "vigencia_inicio")
    private LocalDate vigenciaInicio;

    @Column(name = "vigencia_fin")
    private LocalDate vigenciaFin;

    @Column(nullable = false)
    private boolean activa;

    protected ReglaIncentivoJpaEntity() {
        // JPA
    }

    public ReglaIncentivoJpaEntity(UUID id, String nombre, UUID productoId, UUID categoriaId,
            BigDecimal montoPorUnidad, LocalDate vigenciaInicio, LocalDate vigenciaFin, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.productoId = productoId;
        this.categoriaId = categoriaId;
        this.montoPorUnidad = montoPorUnidad;
        this.vigenciaInicio = vigenciaInicio;
        this.vigenciaFin = vigenciaFin;
        this.activa = activa;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public UUID getCategoriaId() {
        return categoriaId;
    }

    public BigDecimal getMontoPorUnidad() {
        return montoPorUnidad;
    }

    public LocalDate getVigenciaInicio() {
        return vigenciaInicio;
    }

    public LocalDate getVigenciaFin() {
        return vigenciaFin;
    }

    public boolean isActiva() {
        return activa;
    }
}
