package com.posfarmacia.adapters.persistence.entity.promocion;

import com.posfarmacia.domain.enums.TipoBeneficioPromocion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA de {@code promociones}. Se mapea a/desde {@code com.posfarmacia.domain.model.promocion.Promocion}
 * mediante {@code PromocionMapper}; nunca se usa como modelo de negocio directamente.
 */
@Entity
@Table(name = "promociones")
public class PromocionJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_beneficio", nullable = false, length = 30)
    private TipoBeneficioPromocion tipoBeneficio;

    @Column(name = "valor_beneficio", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorBeneficio;

    @Column(name = "requiere_cliente", nullable = false)
    private boolean requiereCliente;

    @Column(name = "cantidad_minima", nullable = false)
    private int cantidadMinima;

    @Column(name = "vigencia_inicio")
    private LocalDate vigenciaInicio;

    @Column(name = "vigencia_fin")
    private LocalDate vigenciaFin;

    @Column(nullable = false)
    private boolean activa;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CondicionPromocionJpaEntity> condiciones = new ArrayList<>();

    protected PromocionJpaEntity() {
        // JPA
    }

    public PromocionJpaEntity(
            UUID id,
            String nombre,
            String descripcion,
            TipoBeneficioPromocion tipoBeneficio,
            BigDecimal valorBeneficio,
            boolean requiereCliente,
            int cantidadMinima,
            LocalDate vigenciaInicio,
            LocalDate vigenciaFin,
            boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoBeneficio = tipoBeneficio;
        this.valorBeneficio = valorBeneficio;
        this.requiereCliente = requiereCliente;
        this.cantidadMinima = cantidadMinima;
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

    public String getDescripcion() {
        return descripcion;
    }

    public TipoBeneficioPromocion getTipoBeneficio() {
        return tipoBeneficio;
    }

    public BigDecimal getValorBeneficio() {
        return valorBeneficio;
    }

    public boolean isRequiereCliente() {
        return requiereCliente;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
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

    public List<CondicionPromocionJpaEntity> getCondiciones() {
        return condiciones;
    }

    public void agregarCondicion(CondicionPromocionJpaEntity condicion) {
        condicion.asignarPromocion(this);
        condiciones.add(condicion);
    }
}
