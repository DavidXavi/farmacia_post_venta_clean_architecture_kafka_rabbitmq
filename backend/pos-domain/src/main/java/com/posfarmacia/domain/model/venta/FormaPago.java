package com.posfarmacia.domain.model.venta;

import com.posfarmacia.domain.enums.TipoFormaPago;
import com.posfarmacia.domain.model.Entidad;
import java.util.UUID;

/**
 * Forma de pago autorizada por la farmacia (RF12): efectivo, tarjeta, transferencia,
 * billetera digital, copago de seguro, credito de farmacia u otra configurada.
 * Equivalente a PosFarmacia.Domain.Entities.FormaPago (.NET).
 */
public final class FormaPago extends Entidad {

    private final String nombre;
    private final TipoFormaPago tipo;
    private boolean activo;

    public FormaPago(String nombre, TipoFormaPago tipo) {
        super();
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = true;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public FormaPago(UUID id, String nombre, TipoFormaPago tipo, boolean activo) {
        super(id);
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = activo;
    }

    public void desactivar() {
        this.activo = false;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoFormaPago getTipo() {
        return tipo;
    }

    public boolean isActivo() {
        return activo;
    }
}
