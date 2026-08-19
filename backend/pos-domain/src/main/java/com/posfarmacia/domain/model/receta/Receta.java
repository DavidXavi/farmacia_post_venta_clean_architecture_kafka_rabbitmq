package com.posfarmacia.domain.model.receta;

import com.posfarmacia.domain.enums.EstadoReceta;
import com.posfarmacia.domain.enums.TipoReceta;
import com.posfarmacia.domain.exception.RecetaYaUtilizadaException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.NumeroReceta;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Receta medica que ampara la venta de un medicamento controlado (RN14-RN21).
 * Traducida de PosFarmacia.Domain.Entities.Receta (.NET), con dos ajustes:
 * - {@code cantidadAutorizada} pasa de texto libre a {@link Cantidad} para poder
 *   validar realmente la correspondencia de cantidad exigida por RN15.
 * - {@link #marcarUtilizada()} sustituye a MarcarUtilizadaYRetenida como punto unico
 *   de control de reutilizacion (ver comentario en el metodo).
 */
public final class Receta extends Entidad {

    private final NumeroReceta numero;
    private final TipoReceta tipo;
    private final LocalDate fechaEmision;
    private final LocalDate fechaVencimiento;
    private final UUID productoId;
    private final UUID clienteId;
    private final String datosPaciente;
    private final String datosProfesional;
    private final String dosis;
    private final Cantidad cantidadAutorizada;
    private final String archivoRespaldoUrl;
    private EstadoReceta estado;
    private boolean retenidaEnBotica;

    public Receta(
            NumeroReceta numero,
            TipoReceta tipo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            UUID productoId,
            UUID clienteId,
            String datosPaciente,
            String datosProfesional,
            String dosis,
            Cantidad cantidadAutorizada,
            String archivoRespaldoUrl) {
        super();
        validarInvariantes(tipo, fechaVencimiento, datosPaciente, datosProfesional, cantidadAutorizada);
        this.numero = numero;
        this.tipo = tipo;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.productoId = productoId;
        this.clienteId = clienteId;
        this.datosPaciente = datosPaciente;
        this.datosProfesional = datosProfesional;
        this.dosis = dosis;
        this.cantidadAutorizada = cantidadAutorizada;
        this.archivoRespaldoUrl = archivoRespaldoUrl;
        this.estado = EstadoReceta.PENDIENTE;
        this.retenidaEnBotica = false;
    }

    /**
     * Reconstruye una receta ya persistida, sin volver a aplicar las validaciones de
     * creacion (el estado ya fue validado cuando se creo por primera vez). Uso exclusivo
     * del mapper del adaptador de persistencia.
     */
    public static Receta reconstruir(
            UUID id,
            NumeroReceta numero,
            TipoReceta tipo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            UUID productoId,
            UUID clienteId,
            String datosPaciente,
            String datosProfesional,
            String dosis,
            Cantidad cantidadAutorizada,
            String archivoRespaldoUrl,
            EstadoReceta estado,
            boolean retenidaEnBotica) {
        Receta receta = new Receta(id, numero, tipo, fechaEmision, fechaVencimiento, productoId, clienteId,
                datosPaciente, datosProfesional, dosis, cantidadAutorizada, archivoRespaldoUrl);
        receta.estado = estado;
        receta.retenidaEnBotica = retenidaEnBotica;
        return receta;
    }

    private Receta(
            UUID id,
            NumeroReceta numero,
            TipoReceta tipo,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            UUID productoId,
            UUID clienteId,
            String datosPaciente,
            String datosProfesional,
            String dosis,
            Cantidad cantidadAutorizada,
            String archivoRespaldoUrl) {
        super(id);
        this.numero = numero;
        this.tipo = tipo;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.productoId = productoId;
        this.clienteId = clienteId;
        this.datosPaciente = datosPaciente;
        this.datosProfesional = datosProfesional;
        this.dosis = dosis;
        this.cantidadAutorizada = cantidadAutorizada;
        this.archivoRespaldoUrl = archivoRespaldoUrl;
    }

    private static void validarInvariantes(
            TipoReceta tipo,
            LocalDate fechaVencimiento,
            String datosPaciente,
            String datosProfesional,
            Cantidad cantidadAutorizada) {
        if (tipo != TipoReceta.NORMAL && fechaVencimiento == null) {
            throw new ValorInvalidoException("Las recetas especiales requieren fecha de vencimiento.");
        }
        if (datosPaciente == null || datosPaciente.isBlank()) {
            throw new ValorInvalidoException("Los datos del paciente son obligatorios.");
        }
        if (datosProfesional == null || datosProfesional.isBlank()) {
            throw new ValorInvalidoException("Los datos del profesional que prescribe son obligatorios.");
        }
        if (cantidadAutorizada == null || cantidadAutorizada.valor() <= 0) {
            throw new ValorInvalidoException("La cantidad autorizada de la receta debe ser mayor a cero.");
        }
    }

    /** RN16/RN17/RN19: solo las recetas con vencimiento (especial y especial retenida) pueden vencer. */
    public boolean estaVencida(LocalDate hoy) {
        return fechaVencimiento != null && fechaVencimiento.isBefore(hoy);
    }

    public void aprobar() {
        this.estado = EstadoReceta.APROBADA;
    }

    public void rechazar() {
        this.estado = EstadoReceta.RECHAZADA;
    }

    /**
     * Marca la receta como utilizada tras dispensar el medicamento (RN18).
     * Para receta normal y especial (no retenida) no hace nada: ambas siguen
     * reutilizables mientras esten vigentes (RN16/RN17). Solo la especial retenida
     * queda bloqueada para un futuro uso y marcada como retenida en la botica.
     *
     * Punto de prevencion de la condicion de carrera de RN20: quien invoque este
     * metodo (el caso de uso de aplicacion) debe hacerlo dentro de una transaccion
     * que cargue la receta con bloqueo optimista (@Version) o pesimista
     * (SELECT ... FOR UPDATE) en el adaptador de persistencia, para que dos
     * confirmaciones concurrentes de la misma receta retenida no pasen ambas esta
     * validacion antes de que la primera confirme su cambio de estado.
     */
    public void marcarUtilizada() {
        if (tipo != TipoReceta.ESPECIAL_RETENIDA) {
            return;
        }
        if (estado == EstadoReceta.UTILIZADA) {
            throw new RecetaYaUtilizadaException();
        }
        estado = EstadoReceta.UTILIZADA;
        retenidaEnBotica = true;
    }

    public NumeroReceta getNumero() {
        return numero;
    }

    public TipoReceta getTipo() {
        return tipo;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public String getDatosPaciente() {
        return datosPaciente;
    }

    public String getDatosProfesional() {
        return datosProfesional;
    }

    public String getDosis() {
        return dosis;
    }

    public Cantidad getCantidadAutorizada() {
        return cantidadAutorizada;
    }

    public String getArchivoRespaldoUrl() {
        return archivoRespaldoUrl;
    }

    public EstadoReceta getEstado() {
        return estado;
    }

    public boolean isRetenidaEnBotica() {
        return retenidaEnBotica;
    }
}
