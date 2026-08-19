package com.posfarmacia.domain.model.venta;

import com.posfarmacia.domain.enums.EstadoVenta;
import com.posfarmacia.domain.enums.TipoComprobante;
import com.posfarmacia.domain.exception.AnulacionNoPermitidaException;
import com.posfarmacia.domain.exception.PagoInsuficienteException;
import com.posfarmacia.domain.exception.PromocionInvalidaException;
import com.posfarmacia.domain.exception.StockInsuficienteException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.exception.VentaYaConfirmadaException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.model.promocion.AplicacionPromocion;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.NumeroComprobante;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Venta (agregado raiz de este contexto, RF05): sus lineas (detalles), pagos y aplicaciones de
 * promocion, referenciando por UUID a los agregados de otros contextos (caja/sesion de caja,
 * usuario, cliente, convenio de seguro, linea de credito). Equivalente a
 * PosFarmacia.Domain.Entities.Venta (.NET).
 *
 * <p>{@link AplicacionPromocion} se reutiliza tal cual del contexto de Promociones (fue disenada
 * explicitamente para que Ventas la asocie a su comprobante, ver el javadoc de
 * {@code SeleccionarPromocionUseCaseImpl}); no se duplica una clase equivalente aqui.
 */
public final class Venta extends Entidad {

    private final List<DetalleVenta> detalles = new ArrayList<>();
    private final List<Pago> pagos = new ArrayList<>();
    private final List<AplicacionPromocion> aplicacionesPromocion = new ArrayList<>();

    private final UUID cajaId;
    private final UUID sesionCajaId;
    private final UUID usuarioId;
    private UUID clienteId;
    private UUID convenioSeguroId;
    private UUID lineaCreditoId;
    private final Instant fecha;
    private EstadoVenta estado;
    private Long numeroCorrelativo;
    private Comprobante comprobante;

    public Venta(UUID cajaId, UUID sesionCajaId, UUID usuarioId, UUID clienteId, Instant ahora) {
        super();
        this.cajaId = cajaId;
        this.sesionCajaId = sesionCajaId;
        this.usuarioId = usuarioId;
        this.clienteId = clienteId;
        this.fecha = ahora;
        this.estado = EstadoVenta.EN_PROCESO;
    }

    /** Constructor de reconstruccion usado por el mapper de persistencia (preserva el id y el estado existente). */
    private Venta(UUID id, UUID cajaId, UUID sesionCajaId, UUID usuarioId, UUID clienteId, UUID convenioSeguroId,
            UUID lineaCreditoId, Instant fecha, EstadoVenta estado, Long numeroCorrelativo,
            List<DetalleVenta> detallesExistentes, List<Pago> pagosExistentes, Comprobante comprobante) {
        super(id);
        this.cajaId = cajaId;
        this.sesionCajaId = sesionCajaId;
        this.usuarioId = usuarioId;
        this.clienteId = clienteId;
        this.convenioSeguroId = convenioSeguroId;
        this.lineaCreditoId = lineaCreditoId;
        this.fecha = fecha;
        this.estado = estado;
        this.numeroCorrelativo = numeroCorrelativo;
        this.comprobante = comprobante;
        if (detallesExistentes != null) {
            this.detalles.addAll(detallesExistentes);
        }
        if (pagosExistentes != null) {
            this.pagos.addAll(pagosExistentes);
        }
        // Las aplicaciones de promocion no se persisten en una tabla propia: se derivan de los
        // detalles ya reconstruidos (cada uno guarda su propio promocionAplicadaId/descuentoMonto),
        // suficiente para sostener la invariante RN09 dentro de esta misma agregacion.
        for (DetalleVenta detalle : this.detalles) {
            if (detalle.getPromocionAplicadaId() != null) {
                this.aplicacionesPromocion.add(AplicacionPromocion.registrar(
                        getId(), detalle.getId(), detalle.getPromocionAplicadaId(), detalle.getDescuentoMonto()));
            }
        }
    }

    /** Usado por el mapper de persistencia para reconstruir el agregado desde su estado guardado. */
    public static Venta reconstruir(UUID id, UUID cajaId, UUID sesionCajaId, UUID usuarioId, UUID clienteId,
            UUID convenioSeguroId, UUID lineaCreditoId, Instant fecha, EstadoVenta estado, Long numeroCorrelativo,
            List<DetalleVenta> detalles, List<Pago> pagos, Comprobante comprobante) {
        return new Venta(id, cajaId, sesionCajaId, usuarioId, clienteId, convenioSeguroId, lineaCreditoId, fecha,
                estado, numeroCorrelativo, detalles, pagos, comprobante);
    }

    public DetalleVenta agregarDetalle(UUID productoId, Cantidad cantidad, Dinero precioUnitario,
            Porcentaje tasaImpuesto, UUID recetaId) {
        asegurarEnProceso();
        DetalleVenta detalle = new DetalleVenta(getId(), productoId, cantidad, precioUnitario, tasaImpuesto, recetaId);
        detalles.add(detalle);
        return detalle;
    }

    public void identificarCliente(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public void asignarConvenio(UUID convenioSeguroId) {
        this.convenioSeguroId = convenioSeguroId;
    }

    public void asignarLineaCredito(UUID lineaCreditoId) {
        this.lineaCreditoId = lineaCreditoId;
    }

    /**
     * RN07/RN09: aplica la promocion elegida por el cajero a una linea puntual. La decision de si la
     * promocion es "aplicable" ya la tomo {@code EvaluadorPromociones} (otro contexto); la invariante
     * de que una promocion no se repita en el comprobante se protege aqui, en el agregado, igual que en
     * PosFarmacia.Domain.Entities.Venta.AplicarPromocionALinea (.NET).
     */
    public void aplicarPromocionALinea(UUID detalleVentaId, UUID promocionId, Dinero montoDescuento) {
        asegurarEnProceso();

        boolean yaAplicada = aplicacionesPromocion.stream()
                .anyMatch(aplicacion -> aplicacion.getPromocionId().equals(promocionId));
        if (yaAplicada) {
            throw new PromocionInvalidaException("Esta promocion ya fue aplicada en otra linea de este comprobante.");
        }

        DetalleVenta detalle = buscarDetalle(detalleVentaId)
                .orElseThrow(() -> new PromocionInvalidaException("La linea de venta indicada no existe en esta venta."));

        detalle.aplicarPromocion(promocionId, montoDescuento);
        aplicacionesPromocion.add(AplicacionPromocion.registrar(getId(), detalleVentaId, promocionId, montoDescuento));
    }

    public void asignarLoteADetalle(UUID detalleVentaId, UUID loteId, Cantidad cantidadTomada) {
        asegurarEnProceso();
        DetalleVenta detalle = buscarDetalle(detalleVentaId)
                .orElseThrow(() -> new StockInsuficienteException("La linea de venta indicada no existe en esta venta."));
        detalle.asignarLote(loteId, cantidadTomada);
    }

    public Pago registrarPago(UUID formaPagoId, Dinero monto, String codigoAutorizacion, Instant ahora) {
        asegurarEnProceso();
        Pago pago = new Pago(getId(), formaPagoId, monto, codigoAutorizacion, ahora);
        pagos.add(pago);
        return pago;
    }

    /** RN02/RN04/RN05/RN06: exige detalles con lotes asignados completos y pago suficiente; es atomica. */
    public void confirmar(long numeroCorrelativo, TipoComprobante tipoComprobante, String serieComprobante,
            Instant ahora) {
        asegurarEnProceso();

        if (detalles.isEmpty()) {
            throw new ValorInvalidoException("No se puede confirmar una venta sin productos.");
        }

        boolean faltanLotesPorAsignar = detalles.stream()
                .anyMatch(detalle -> detalle.getCantidadAsignadaEnLotes().valor() != detalle.getCantidad().valor());
        if (faltanLotesPorAsignar) {
            throw new StockInsuficienteException(
                    "Todas las lineas de venta deben tener lotes asignados por FEFO antes de confirmar.");
        }

        if (getTotalPagado().esMenorQue(getTotal())) {
            throw new PagoInsuficienteException();
        }

        this.numeroCorrelativo = numeroCorrelativo;
        this.comprobante = new Comprobante(getId(), tipoComprobante,
                new NumeroComprobante(serieComprobante, (int) numeroCorrelativo), ahora);
        this.estado = EstadoVenta.CONFIRMADA;
    }

    public boolean esDelMismoDia(LocalDate hoy) {
        return fecha.atZone(ZoneId.systemDefault()).toLocalDate().equals(hoy);
    }

    /** RN39/RN40: solo una venta confirmada del mismo dia puede anularse directamente. */
    public void anular(LocalDate hoy) {
        if (estado != EstadoVenta.CONFIRMADA) {
            throw new AnulacionNoPermitidaException("Solo una venta confirmada puede anularse.");
        }
        if (!esDelMismoDia(hoy)) {
            throw new AnulacionNoPermitidaException(
                    "La venta corresponde a un dia anterior; debe emitirse una nota de credito.");
        }
        this.estado = EstadoVenta.ANULADA;
    }

    public Optional<DetalleVenta> buscarDetalle(UUID detalleVentaId) {
        return detalles.stream().filter(detalle -> detalle.getId().equals(detalleVentaId)).findFirst();
    }

    /** RN09: promociones ya registradas en este comprobante, para que el contexto de Promociones valide la eleccion. */
    public Set<UUID> getPromocionesAplicadasIds() {
        return aplicacionesPromocion.stream().map(AplicacionPromocion::getPromocionId).collect(Collectors.toSet());
    }

    public Dinero getTotal() {
        return detalles.stream().map(DetalleVenta::getSubtotal).reduce(Dinero.CERO, Dinero::sumar);
    }

    public Dinero getTotalPagado() {
        return pagos.stream().map(Pago::getMonto).reduce(Dinero.CERO, Dinero::sumar);
    }

    private void asegurarEnProceso() {
        if (estado != EstadoVenta.EN_PROCESO) {
            throw new VentaYaConfirmadaException();
        }
    }

    public UUID getCajaId() {
        return cajaId;
    }

    public UUID getSesionCajaId() {
        return sesionCajaId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public UUID getConvenioSeguroId() {
        return convenioSeguroId;
    }

    public UUID getLineaCreditoId() {
        return lineaCreditoId;
    }

    public Instant getFecha() {
        return fecha;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public Long getNumeroCorrelativo() {
        return numeroCorrelativo;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public List<DetalleVenta> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public List<Pago> getPagos() {
        return Collections.unmodifiableList(pagos);
    }

    public List<AplicacionPromocion> getAplicacionesPromocion() {
        return Collections.unmodifiableList(aplicacionesPromocion);
    }
}
