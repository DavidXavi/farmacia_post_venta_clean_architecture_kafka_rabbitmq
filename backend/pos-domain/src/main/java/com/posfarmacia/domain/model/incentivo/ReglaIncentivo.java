package com.posfarmacia.domain.model.incentivo;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Regla de incentivo del personal (RF18): asocia un monto fijo por unidad vendida a un producto o
 * a una categoria (nunca a ambos a la vez, ver AYUDA_CATALOGOS en {@code CatalogosPage.jsx}),
 * dentro de un periodo de vigencia. Equivalente a PosFarmacia.Domain.Entities.ReglaIncentivo (.NET).
 */
public final class ReglaIncentivo extends Entidad {

    private String nombre;
    private UUID productoId;
    private UUID categoriaId;
    private Dinero montoPorUnidad;
    private PeriodoVigencia vigencia;
    private boolean activa;

    public ReglaIncentivo(String nombre, UUID productoId, UUID categoriaId, Dinero montoPorUnidad,
            PeriodoVigencia vigencia) {
        super();
        validar(nombre, productoId, categoriaId, montoPorUnidad, vigencia);
        this.nombre = nombre;
        this.productoId = productoId;
        this.categoriaId = categoriaId;
        this.montoPorUnidad = montoPorUnidad;
        this.vigencia = vigencia;
        this.activa = true;
    }

    /** Constructor de reconstruccion usado por el mapper de persistencia (preserva el id y el estado existente). */
    private ReglaIncentivo(UUID id, String nombre, UUID productoId, UUID categoriaId, Dinero montoPorUnidad,
            PeriodoVigencia vigencia, boolean activa) {
        super(id);
        this.nombre = nombre;
        this.productoId = productoId;
        this.categoriaId = categoriaId;
        this.montoPorUnidad = montoPorUnidad;
        this.vigencia = vigencia;
        this.activa = activa;
    }

    /** Usado por el mapper de persistencia para reconstruir la regla desde su estado guardado. */
    public static ReglaIncentivo reconstruir(UUID id, String nombre, UUID productoId, UUID categoriaId,
            Dinero montoPorUnidad, PeriodoVigencia vigencia, boolean activa) {
        return new ReglaIncentivo(id, nombre, productoId, categoriaId, montoPorUnidad, vigencia, activa);
    }

    private static void validar(String nombre, UUID productoId, UUID categoriaId, Dinero montoPorUnidad,
            PeriodoVigencia vigencia) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValorInvalidoException("El nombre de la regla de incentivo es obligatorio.");
        }
        if (productoId == null && categoriaId == null) {
            throw new ValorInvalidoException("La regla de incentivo debe asociarse a un producto o a una categoria.");
        }
        if (productoId != null && categoriaId != null) {
            throw new ValorInvalidoException(
                    "La regla de incentivo no puede asociarse a un producto y a una categoria a la vez.");
        }
        if (montoPorUnidad == null) {
            throw new ValorInvalidoException("El monto por unidad de la regla de incentivo es obligatorio.");
        }
        if (vigencia == null) {
            throw new ValorInvalidoException("La vigencia de la regla de incentivo es obligatoria.");
        }
    }

    /** Edita los datos de la regla (usado por el CRUD de administracion). */
    public void actualizar(String nombre, UUID productoId, UUID categoriaId, Dinero montoPorUnidad,
            PeriodoVigencia vigencia, boolean activa) {
        validar(nombre, productoId, categoriaId, montoPorUnidad, vigencia);
        this.nombre = nombre;
        this.productoId = productoId;
        this.categoriaId = categoriaId;
        this.montoPorUnidad = montoPorUnidad;
        this.vigencia = vigencia;
        this.activa = activa;
    }

    /**
     * Indica si esta regla aplica a la linea de venta de {@code productoId}/{@code categoriaId} el
     * dia {@code hoy}: debe estar activa, vigente, y coincidir por producto o por categoria.
     * Equivalente a PosFarmacia.Domain.Entities.ReglaIncentivo.AplicaA (.NET).
     */
    public boolean aplicaA(UUID productoId, UUID categoriaId, LocalDate hoy) {
        boolean coincideProducto = this.productoId != null && this.productoId.equals(productoId);
        boolean coincideCategoria = this.categoriaId != null && this.categoriaId.equals(categoriaId);
        return activa && vigencia.estaVigente(hoy) && (coincideProducto || coincideCategoria);
    }

    public void desactivar() {
        this.activa = false;
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

    public Dinero getMontoPorUnidad() {
        return montoPorUnidad;
    }

    public PeriodoVigencia getVigencia() {
        return vigencia;
    }

    public boolean isActiva() {
        return activa;
    }
}
