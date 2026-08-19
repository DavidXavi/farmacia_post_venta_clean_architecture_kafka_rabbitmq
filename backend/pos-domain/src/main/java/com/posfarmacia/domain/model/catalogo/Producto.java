package com.posfarmacia.domain.model.catalogo;

import com.posfarmacia.domain.enums.EstadoProducto;
import com.posfarmacia.domain.enums.TipoProducto;
import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.CodigoBarras;
import com.posfarmacia.domain.valueobject.CodigoProducto;
import com.posfarmacia.domain.valueobject.Dinero;
import java.util.UUID;

/**
 * Producto del catalogo (RF03): medicamento, OTC u otro producto de farmacia.
 * CategoriaId/LaboratorioId/PresentacionId apuntan a otros agregados del mismo contexto por UUID.
 */
public final class Producto extends Entidad {

    private final CodigoProducto codigoInterno;
    private CodigoBarras codigoBarras;
    private String nombreComercial;
    private String descripcion;
    private final TipoProducto tipoProducto;
    private final UUID categoriaId;
    private final UUID laboratorioId;
    private final UUID presentacionId;
    private Dinero precioVenta;
    private final boolean esControlado;
    private final boolean requiereReceta;
    private final TipoReceta tipoRecetaRequerida;
    private EstadoProducto estado;

    public Producto(
            CodigoProducto codigoInterno,
            String nombreComercial,
            String descripcion,
            TipoProducto tipoProducto,
            UUID categoriaId,
            UUID laboratorioId,
            UUID presentacionId,
            Dinero precioVenta,
            boolean esControlado,
            boolean requiereReceta,
            TipoReceta tipoRecetaRequerida,
            CodigoBarras codigoBarras) {
        super();
        validarCoherenciaControlado(esControlado, requiereReceta);
        this.codigoInterno = requireNoNulo(codigoInterno, "El codigo interno del producto es obligatorio.");
        this.codigoBarras = codigoBarras;
        this.nombreComercial = requireTexto(nombreComercial, "El nombre comercial del producto es obligatorio.");
        this.descripcion = descripcion == null ? "" : descripcion;
        this.tipoProducto = requireNoNulo(tipoProducto, "El tipo de producto es obligatorio.");
        this.categoriaId = requireNoNulo(categoriaId, "La categoria del producto es obligatoria.");
        this.laboratorioId = requireNoNulo(laboratorioId, "El laboratorio del producto es obligatorio.");
        this.presentacionId = requireNoNulo(presentacionId, "La presentacion del producto es obligatoria.");
        this.precioVenta = requireNoNulo(precioVenta, "El precio de venta del producto es obligatorio.");
        this.esControlado = esControlado;
        this.requiereReceta = requiereReceta;
        this.tipoRecetaRequerida = tipoRecetaRequerida;
        this.estado = EstadoProducto.ACTIVO;
    }

    private Producto(
            UUID id,
            CodigoProducto codigoInterno,
            CodigoBarras codigoBarras,
            String nombreComercial,
            String descripcion,
            TipoProducto tipoProducto,
            UUID categoriaId,
            UUID laboratorioId,
            UUID presentacionId,
            Dinero precioVenta,
            boolean esControlado,
            boolean requiereReceta,
            TipoReceta tipoRecetaRequerida,
            EstadoProducto estado) {
        super(id);
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

    /** Usado por el mapper de persistencia para reconstruir el agregado desde su estado guardado. */
    public static Producto reconstruir(
            UUID id,
            CodigoProducto codigoInterno,
            CodigoBarras codigoBarras,
            String nombreComercial,
            String descripcion,
            TipoProducto tipoProducto,
            UUID categoriaId,
            UUID laboratorioId,
            UUID presentacionId,
            Dinero precioVenta,
            boolean esControlado,
            boolean requiereReceta,
            TipoReceta tipoRecetaRequerida,
            EstadoProducto estado) {
        return new Producto(id, codigoInterno, codigoBarras, nombreComercial, descripcion, tipoProducto,
                categoriaId, laboratorioId, presentacionId, precioVenta, esControlado, requiereReceta,
                tipoRecetaRequerida, estado);
    }

    private static void validarCoherenciaControlado(boolean esControlado, boolean requiereReceta) {
        if (esControlado && !requiereReceta) {
            throw new ValorInvalidoException("Un producto controlado siempre debe requerir receta.");
        }
    }

    private static <T> T requireNoNulo(T valor, String mensaje) {
        if (valor == null) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    private static String requireTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    public void actualizarDatos(String nombreComercial, String descripcion, Dinero precioVenta) {
        this.nombreComercial = requireTexto(nombreComercial, "El nombre comercial del producto es obligatorio.");
        this.descripcion = descripcion == null ? "" : descripcion;
        this.precioVenta = requireNoNulo(precioVenta, "El precio de venta del producto es obligatorio.");
    }

    public void darDeBaja() {
        this.estado = EstadoProducto.DADO_DE_BAJA;
    }

    public void suspender() {
        this.estado = EstadoProducto.SUSPENDIDO;
    }

    public void reactivar() {
        this.estado = EstadoProducto.ACTIVO;
    }

    public boolean estaVendible() {
        return estado == EstadoProducto.ACTIVO;
    }

    public CodigoProducto getCodigoInterno() {
        return codigoInterno;
    }

    public CodigoBarras getCodigoBarras() {
        return codigoBarras;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoProducto getTipoProducto() {
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

    public Dinero getPrecioVenta() {
        return precioVenta;
    }

    public boolean isEsControlado() {
        return esControlado;
    }

    public boolean isRequiereReceta() {
        return requiereReceta;
    }

    public TipoReceta getTipoRecetaRequerida() {
        return tipoRecetaRequerida;
    }

    public EstadoProducto getEstado() {
        return estado;
    }
}
