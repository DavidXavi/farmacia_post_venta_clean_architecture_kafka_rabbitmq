package com.posfarmacia.domain.model.seguro;

import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.util.UUID;

/**
 * Cobertura de un producto especifico dentro de un {@link ConvenioSeguro} (RN24):
 * la existencia de un convenio no implica que todos los productos esten cubiertos,
 * cada producto tiene su propio porcentaje cubierto.
 */
public final class CoberturaConvenio extends Entidad {

    private final UUID convenioId;
    private final UUID productoId;
    private Porcentaje porcentajeCubierto;

    public CoberturaConvenio(UUID convenioId, UUID productoId, Porcentaje porcentajeCubierto) {
        super();
        this.convenioId = convenioId;
        this.productoId = productoId;
        this.porcentajeCubierto = porcentajeCubierto;
    }

    public CoberturaConvenio(UUID id, UUID convenioId, UUID productoId, Porcentaje porcentajeCubierto) {
        super(id);
        this.convenioId = convenioId;
        this.productoId = productoId;
        this.porcentajeCubierto = porcentajeCubierto;
    }

    public void actualizarPorcentaje(Porcentaje porcentaje) {
        this.porcentajeCubierto = porcentaje;
    }

    public UUID getConvenioId() {
        return convenioId;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public Porcentaje getPorcentajeCubierto() {
        return porcentajeCubierto;
    }
}
