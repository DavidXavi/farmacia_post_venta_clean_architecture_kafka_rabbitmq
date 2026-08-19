package com.posfarmacia.domain.model.seguro;

import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Convenio de seguro con el que la farmacia tiene acuerdo de copago (RF10, RN22-RN26).
 */
public final class ConvenioSeguro extends Entidad {

    private final List<CoberturaConvenio> coberturas = new ArrayList<>();
    private String nombre;
    private boolean activo;

    public ConvenioSeguro(String nombre) {
        super();
        this.nombre = nombre;
        this.activo = true;
    }

    public ConvenioSeguro(UUID id, String nombre, boolean activo, List<CoberturaConvenio> coberturas) {
        super(id);
        this.nombre = nombre;
        this.activo = activo;
        if (coberturas != null) {
            this.coberturas.addAll(coberturas);
        }
    }

    public void configurarCobertura(UUID productoId, Porcentaje porcentajeCubierto) {
        Optional<CoberturaConvenio> existente = obtenerCoberturaPara(productoId);
        if (existente.isPresent()) {
            existente.get().actualizarPorcentaje(porcentajeCubierto);
            return;
        }
        coberturas.add(new CoberturaConvenio(getId(), productoId, porcentajeCubierto));
    }

    public Optional<CoberturaConvenio> obtenerCoberturaPara(UUID productoId) {
        return coberturas.stream().filter(c -> c.getProductoId().equals(productoId)).findFirst();
    }

    public void desactivar() {
        this.activo = false;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public List<CoberturaConvenio> getCoberturas() {
        return List.copyOf(coberturas);
    }
}
