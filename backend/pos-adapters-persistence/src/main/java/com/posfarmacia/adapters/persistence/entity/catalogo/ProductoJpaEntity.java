package com.posfarmacia.adapters.persistence.entity.catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "productos")
public class ProductoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "codigo_interno", nullable = false, unique = true)
    private String codigoInterno;

    @Column(name = "codigo_barras")
    private String codigoBarras;

    @Column(name = "nombre_comercial", nullable = false)
    private String nombreComercial;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "tipo_producto", nullable = false)
    private String tipoProducto;

    @Column(name = "categoria_id", nullable = false)
    private UUID categoriaId;

    @Column(name = "laboratorio_id", nullable = false)
    private UUID laboratorioId;

    @Column(name = "presentacion_id", nullable = false)
    private UUID presentacionId;

    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "es_controlado", nullable = false)
    private boolean esControlado;

    @Column(name = "requiere_receta", nullable = false)
    private boolean requiereReceta;

    @Column(name = "tipo_receta_requerida")
    private String tipoRecetaRequerida;

    @Column(nullable = false)
    private String estado;

    protected ProductoJpaEntity() {
    }

    public ProductoJpaEntity(UUID id, String codigoInterno, String codigoBarras, String nombreComercial,
            String descripcion, String tipoProducto, UUID categoriaId, UUID laboratorioId, UUID presentacionId,
            BigDecimal precioVenta, boolean esControlado, boolean requiereReceta, String tipoRecetaRequerida,
            String estado) {
        this.id = id;
        this.codigoInterno = codigoInterno;
        this.codigoBarras = codigoBarras;
        this.nombreComercial = nombreComercial;
        this.descripcion = descripcion;
        this.tipoProducto = tipoProducto;
        this.categoriaId = categoriaId;
        this.laboratorioId = laboratorioId;
        this.presentacionId = presentacionId;
        this.precioVenta = precioVenta;
        this.esControlado = esControlado;
        this.requiereReceta = requiereReceta;
        this.tipoRecetaRequerida = tipoRecetaRequerida;
        this.estado = estado;
    }

    public UUID getId() {
        return id;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public UUID getCategoriaId() {
        return categoriaId;
    }

    public UUID getLaboratorioId() {
        return laboratorioId;
    }

    public UUID getPresentacionId() {
        return presentacionId;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public boolean isEsControlado() {
        return esControlado;
    }

    public boolean isRequiereReceta() {
        return requiereReceta;
    }

    public String getTipoRecetaRequerida() {
        return tipoRecetaRequerida;
    }

    public String getEstado() {
        return estado;
    }
}
