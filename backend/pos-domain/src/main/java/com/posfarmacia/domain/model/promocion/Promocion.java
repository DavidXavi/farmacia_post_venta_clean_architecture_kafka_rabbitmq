package com.posfarmacia.domain.model.promocion;

import com.posfarmacia.domain.enums.TipoBeneficioPromocion;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Promocion aplicable a uno o varios productos (RN07-RN13). Un producto puede tener varias
 * promociones vigentes ("lleva 2 y la 3ra gratis", "10% de descuento por 1 unidad"), pero
 * cada linea de venta solo puede usar una (RN07), y una misma promocion solo se aplica una
 * vez por comprobante completo (RN09, ver {@code EvaluadorPromociones.validarSeleccion}).
 */
public final class Promocion extends Entidad {

    private final List<CondicionPromocion> condiciones = new ArrayList<>();

    private String nombre;
    private String descripcion;
    private TipoBeneficioPromocion tipoBeneficio;
    private BigDecimal valorBeneficio;
    private boolean requiereCliente;
    private Cantidad cantidadMinima;
    private PeriodoVigencia vigencia;
    private boolean activa;

    /** Reconstruccion (por ejemplo, desde el mapper de persistencia) con un id ya existente. */
    public Promocion(
            UUID id,
            String nombre,
            String descripcion,
            TipoBeneficioPromocion tipoBeneficio,
            BigDecimal valorBeneficio,
            boolean requiereCliente,
            Cantidad cantidadMinima,
            PeriodoVigencia vigencia,
            boolean activa,
            List<CondicionPromocion> condiciones) {
        super(id);
        if (nombre == null || nombre.isBlank()) {
            throw new ValorInvalidoException("El nombre de la promocion es obligatorio.");
        }
        this.nombre = nombre;
        this.descripcion = descripcion == null ? "" : descripcion;
        this.tipoBeneficio = Objects.requireNonNull(tipoBeneficio, "tipoBeneficio no puede ser nulo");
        this.valorBeneficio = Objects.requireNonNull(valorBeneficio, "valorBeneficio no puede ser nulo");
        this.requiereCliente = requiereCliente;
        this.cantidadMinima = Objects.requireNonNull(cantidadMinima, "cantidadMinima no puede ser nula");
        this.vigencia = Objects.requireNonNull(vigencia, "vigencia no puede ser nula");
        this.activa = activa;
        if (condiciones != null) {
            this.condiciones.addAll(condiciones);
        }
    }

    /** Crea una promocion nueva (id generado), activa desde su registro. */
    public static Promocion crear(
            String nombre,
            String descripcion,
            TipoBeneficioPromocion tipoBeneficio,
            BigDecimal valorBeneficio,
            boolean requiereCliente,
            Cantidad cantidadMinima,
            PeriodoVigencia vigencia) {
        return new Promocion(
                UUID.randomUUID(),
                nombre,
                descripcion,
                tipoBeneficio,
                valorBeneficio,
                requiereCliente,
                cantidadMinima,
                vigencia,
                true,
                new ArrayList<>());
    }

    public void agregarProductoParticipante(UUID productoId) {
        boolean yaExiste = condiciones.stream().anyMatch(c -> c.productoId().equals(productoId));
        if (!yaExiste) {
            condiciones.add(new CondicionPromocion(productoId));
        }
    }

    /**
     * Edita los datos de la promocion (CRUD de administracion). Reemplaza los productos
     * participantes por la lista indicada, preservando las condiciones que se mantienen y
     * quitando solo las que ya no aplican (equivalente a {@code Promocion.EditarDatos}, .NET).
     */
    public void actualizar(
            String nombre,
            String descripcion,
            TipoBeneficioPromocion tipoBeneficio,
            BigDecimal valorBeneficio,
            boolean requiereCliente,
            Cantidad cantidadMinima,
            PeriodoVigencia vigencia,
            List<UUID> productosParticipantes) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValorInvalidoException("El nombre de la promocion es obligatorio.");
        }
        this.nombre = nombre;
        this.descripcion = descripcion == null ? "" : descripcion;
        this.tipoBeneficio = Objects.requireNonNull(tipoBeneficio, "tipoBeneficio no puede ser nulo");
        this.valorBeneficio = Objects.requireNonNull(valorBeneficio, "valorBeneficio no puede ser nulo");
        this.requiereCliente = requiereCliente;
        this.cantidadMinima = Objects.requireNonNull(cantidadMinima, "cantidadMinima no puede ser nula");
        this.vigencia = Objects.requireNonNull(vigencia, "vigencia no puede ser nula");

        List<UUID> productos = productosParticipantes == null ? List.of() : productosParticipantes;
        condiciones.removeIf(c -> !productos.contains(c.productoId()));
        for (UUID productoId : productos) {
            agregarProductoParticipante(productoId);
        }
    }

    /** RN12: la promocion solo aplica a los productos que forman parte de sus condiciones. */
    public boolean aplicaAProducto(UUID productoId) {
        return condiciones.stream().anyMatch(c -> c.productoId().equals(productoId));
    }

    /** RN11: vigencia (activa y dentro del periodo). */
    public boolean estaVigente(LocalDate hoy) {
        return activa && vigencia.estaVigente(hoy);
    }

    /** Calcula el descuento segun el tipo de beneficio configurado. */
    public Dinero calcularDescuento(Dinero precioUnitario, Cantidad cantidad) {
        Dinero totalLinea = precioUnitario.multiplicar(BigDecimal.valueOf(cantidad.valor()));
        return switch (tipoBeneficio) {
            case DESCUENTO_PORCENTAJE -> new Porcentaje(valorBeneficio).aplicarSobre(totalLinea);
            case DESCUENTO_MONTO -> {
                Dinero montoMaximo = new Dinero(valorBeneficio);
                yield montoMaximo.esMenorQue(totalLinea) ? montoMaximo : totalLinea;
            }
            case LLEVA_N_PAGA_M -> calcularDescuentoLlevaNPagaM(precioUnitario, cantidad);
        };
    }

    private Dinero calcularDescuentoLlevaNPagaM(Dinero precioUnitario, Cantidad cantidad) {
        int n = valorBeneficio.intValue();
        if (n <= 0) {
            return Dinero.CERO;
        }
        int unidadesGratis = cantidad.valor() / n;
        return precioUnitario.multiplicar(BigDecimal.valueOf(unidadesGratis));
    }

    public void desactivar() {
        this.activa = false;
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

    public Cantidad getCantidadMinima() {
        return cantidadMinima;
    }

    public PeriodoVigencia getVigencia() {
        return vigencia;
    }

    public boolean isActiva() {
        return activa;
    }

    public List<CondicionPromocion> getCondiciones() {
        return Collections.unmodifiableList(condiciones);
    }
}
