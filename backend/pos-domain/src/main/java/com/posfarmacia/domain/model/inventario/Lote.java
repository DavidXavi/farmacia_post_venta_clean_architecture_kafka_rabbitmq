package com.posfarmacia.domain.model.inventario;

import com.posfarmacia.domain.enums.EstadoLote;
import com.posfarmacia.domain.exception.StockInsuficienteException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.exception.inventario.EstadoLoteInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.CodigoLote;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.FechaVencimiento;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Lote de un producto (RF04): cantidad recibida/disponible, vencimiento y almacen/local de origen.
 * ProductoId y LocalId apuntan a agregados por UUID (Producto es de este mismo contexto; Local pertenece
 * al contexto de identidad/cajas y solo se referencia por id).
 */
public final class Lote extends Entidad {

    public static final int MESES_PERIODO_PREVENTIVO_DEFECTO = 3;

    private final CodigoLote codigo;
    private final UUID productoId;
    private final FechaVencimiento fechaVencimiento;
    private final Cantidad cantidadRecibida;
    private Cantidad cantidadDisponible;
    private final Dinero costo;
    private final UUID localId;
    private EstadoLote estado;

    public Lote(CodigoLote codigo, UUID productoId, FechaVencimiento fechaVencimiento, Cantidad cantidadRecibida,
            UUID localId, Dinero costo) {
        super();
        this.codigo = requireNoNulo(codigo, "El codigo de lote es obligatorio.");
        this.productoId = requireNoNulo(productoId, "El producto del lote es obligatorio.");
        this.fechaVencimiento = requireNoNulo(fechaVencimiento, "La fecha de vencimiento del lote es obligatoria.");
        this.cantidadRecibida = requireNoNulo(cantidadRecibida, "La cantidad recibida del lote es obligatoria.");
        this.cantidadDisponible = cantidadRecibida;
        this.localId = requireNoNulo(localId, "El local del lote es obligatorio.");
        this.costo = costo;
        this.estado = EstadoLote.DISPONIBLE;
    }

    private Lote(UUID id, CodigoLote codigo, UUID productoId, FechaVencimiento fechaVencimiento,
            Cantidad cantidadRecibida, Cantidad cantidadDisponible, Dinero costo, UUID localId, EstadoLote estado) {
        super(id);
        this.codigo = codigo;
        this.productoId = productoId;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadRecibida = cantidadRecibida;
        this.cantidadDisponible = cantidadDisponible;
        this.costo = costo;
        this.localId = localId;
        this.estado = estado;
    }

    /** Usado por el mapper de persistencia para reconstruir el agregado desde su estado guardado. */
    public static Lote reconstruir(UUID id, CodigoLote codigo, UUID productoId, FechaVencimiento fechaVencimiento,
            Cantidad cantidadRecibida, Cantidad cantidadDisponible, Dinero costo, UUID localId, EstadoLote estado) {
        return new Lote(id, codigo, productoId, fechaVencimiento, cantidadRecibida, cantidadDisponible, costo,
                localId, estado);
    }

    private static <T> T requireNoNulo(T valor, String mensaje) {
        if (valor == null) {
            throw new ValorInvalidoException(mensaje);
        }
        return valor;
    }

    /** RF14/RN35/RN36: vendible solo si esta disponible, tiene stock, no vencio y no esta en periodo preventivo. */
    public boolean esVendible(LocalDate hoy) {
        return esVendible(hoy, MESES_PERIODO_PREVENTIVO_DEFECTO);
    }

    public boolean esVendible(LocalDate hoy, int mesesPreventivos) {
        return estado == EstadoLote.DISPONIBLE
                && cantidadDisponible.valor() > 0
                && !fechaVencimiento.estaVencida(hoy)
                && !fechaVencimiento.estaEnPeriodoPreventivo(hoy, mesesPreventivos);
    }

    /** RN02/RN33: descuenta stock del lote al despacharlo; agota el lote cuando llega a cero. */
    public void reservar(Cantidad cantidad) {
        if (cantidad.esMayorQue(cantidadDisponible)) {
            throw new StockInsuficienteException("El lote " + codigo + " no tiene stock suficiente.");
        }
        cantidadDisponible = cantidadDisponible.restar(cantidad);
        if (cantidadDisponible.valor() == 0) {
            estado = EstadoLote.AGOTADO;
        }
    }

    /**
     * RN43: un lote vencido, retirado o bloqueado no vuelve a ser stock vendible aunque se reverse la venta.
     * Devuelve false cuando la devolucion no pudo aplicarse por ese motivo.
     */
    public boolean devolver(Cantidad cantidad) {
        if (estado == EstadoLote.VENCIDO || estado == EstadoLote.RETIRADO || estado == EstadoLote.BLOQUEADO) {
            return false;
        }
        cantidadDisponible = cantidadDisponible.sumar(cantidad);
        estado = EstadoLote.DISPONIBLE;
        return true;
    }

    public void bloquear() {
        validarNoRetirado("bloquear");
        estado = EstadoLote.BLOQUEADO;
    }

    /** RN37: cuando la central ordena el retiro, el lote deja de ser stock vendible en todas las sedes. */
    public void retirar() {
        validarNoRetirado("retirar");
        estado = EstadoLote.RETIRADO;
    }

    public void marcarVencido() {
        estado = EstadoLote.VENCIDO;
    }

    private void validarNoRetirado(String accion) {
        if (estado == EstadoLote.RETIRADO) {
            throw new EstadoLoteInvalidoException("No se puede " + accion + " el lote " + codigo
                    + " porque ya fue retirado.");
        }
    }

    public CodigoLote getCodigo() {
        return codigo;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public FechaVencimiento getFechaVencimiento() {
        return fechaVencimiento;
    }

    public Cantidad getCantidadRecibida() {
        return cantidadRecibida;
    }

    public Cantidad getCantidadDisponible() {
        return cantidadDisponible;
    }

    public Dinero getCosto() {
        return costo;
    }

    public UUID getLocalId() {
        return localId;
    }

    public EstadoLote getEstado() {
        return estado;
    }
}
