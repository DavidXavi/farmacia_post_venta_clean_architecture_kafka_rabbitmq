package com.posfarmacia.domain.model.venta;

import com.posfarmacia.domain.exception.PromocionInvalidaException;
import com.posfarmacia.domain.exception.StockInsuficienteException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.service.venta.CalculadorTotalVenta;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Linea de una venta (RF05): producto, cantidad, precio, promocion aplicada (a lo sumo una,
 * RN07) y los lotes asignados para su despacho (RN33-RN38). Equivalente a
 * PosFarmacia.Domain.Entities.DetalleVenta (.NET).
 */
public final class DetalleVenta extends Entidad {

    private static final CalculadorTotalVenta CALCULADOR = new CalculadorTotalVenta();

    private final List<DetalleVentaLote> lotes = new ArrayList<>();

    private final UUID ventaId;
    private final UUID productoId;
    private final Cantidad cantidad;
    private final Dinero precioUnitario;
    private final Porcentaje tasaImpuesto;
    private final UUID recetaId;
    private UUID promocionAplicadaId;
    private Dinero descuentoMonto;
    private Dinero impuestoMonto;
    private Dinero subtotal;

    public DetalleVenta(UUID ventaId, UUID productoId, Cantidad cantidad, Dinero precioUnitario,
            Porcentaje tasaImpuesto, UUID recetaId) {
        super();
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tasaImpuesto = tasaImpuesto;
        this.recetaId = recetaId;
        this.descuentoMonto = Dinero.CERO;
        recalcular();
    }

    /** Constructor de reconstruccion usado por el mapper de persistencia (preserva el id y los lotes ya asignados). */
    private DetalleVenta(UUID id, UUID ventaId, UUID productoId, Cantidad cantidad, Dinero precioUnitario,
            Porcentaje tasaImpuesto, UUID recetaId, UUID promocionAplicadaId, Dinero descuentoMonto,
            List<DetalleVentaLote> lotesExistentes) {
        super(id);
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tasaImpuesto = tasaImpuesto;
        this.recetaId = recetaId;
        this.promocionAplicadaId = promocionAplicadaId;
        this.descuentoMonto = descuentoMonto == null ? Dinero.CERO : descuentoMonto;
        if (lotesExistentes != null) {
            this.lotes.addAll(lotesExistentes);
        }
        recalcular();
    }

    /** Usado por el mapper de persistencia para reconstruir la linea desde su estado guardado. */
    public static DetalleVenta reconstruir(UUID id, UUID ventaId, UUID productoId, Cantidad cantidad,
            Dinero precioUnitario, Porcentaje tasaImpuesto, UUID recetaId, UUID promocionAplicadaId,
            Dinero descuentoMonto, List<DetalleVentaLote> lotesExistentes) {
        return new DetalleVenta(id, ventaId, productoId, cantidad, precioUnitario, tasaImpuesto, recetaId,
                promocionAplicadaId, descuentoMonto, lotesExistentes);
    }

    /** RN07: una linea de venta admite a lo sumo una promocion aplicada. */
    void aplicarPromocion(UUID promocionId, Dinero montoDescuento) {
        if (promocionAplicadaId != null) {
            throw new PromocionInvalidaException("Esta linea de venta ya tiene una promocion aplicada.");
        }
        this.promocionAplicadaId = promocionId;
        this.descuentoMonto = montoDescuento;
        recalcular();
    }

    /** RN02/RN33-RN38: acumula la cantidad tomada de un lote sin superar la cantidad vendida en la linea. */
    void asignarLote(UUID loteId, Cantidad cantidadTomada) {
        if (getCantidadAsignadaEnLotes().sumar(cantidadTomada).esMayorQue(cantidad)) {
            throw new StockInsuficienteException("La cantidad asignada de lotes excede la cantidad vendida en la linea.");
        }
        lotes.add(new DetalleVentaLote(getId(), loteId, cantidadTomada));
    }

    private void recalcular() {
        Dinero baseImponible = new Dinero(precioUnitario.monto()
                .multiply(java.math.BigDecimal.valueOf(cantidad.valor()))
                .subtract(descuentoMonto.monto()));
        this.impuestoMonto = tasaImpuesto.aplicarSobre(baseImponible);
        this.subtotal = CALCULADOR.calcularSubtotalLinea(precioUnitario, cantidad, descuentoMonto, tasaImpuesto);
    }

    public Cantidad getCantidadAsignadaEnLotes() {
        return lotes.stream().map(DetalleVentaLote::getCantidadTomada).reduce(Cantidad.CERO, Cantidad::sumar);
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public Cantidad getCantidad() {
        return cantidad;
    }

    public Dinero getPrecioUnitario() {
        return precioUnitario;
    }

    public Porcentaje getTasaImpuesto() {
        return tasaImpuesto;
    }

    public UUID getRecetaId() {
        return recetaId;
    }

    public UUID getPromocionAplicadaId() {
        return promocionAplicadaId;
    }

    public Dinero getDescuentoMonto() {
        return descuentoMonto;
    }

    public Dinero getImpuestoMonto() {
        return impuestoMonto;
    }

    public Dinero getSubtotal() {
        return subtotal;
    }

    public List<DetalleVentaLote> getLotes() {
        return Collections.unmodifiableList(lotes);
    }
}
