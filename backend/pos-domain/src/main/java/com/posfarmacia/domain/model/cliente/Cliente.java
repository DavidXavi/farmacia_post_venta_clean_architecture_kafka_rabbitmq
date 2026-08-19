package com.posfarmacia.domain.model.cliente;

import com.posfarmacia.domain.enums.EstadoCuenta;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Dni;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Cliente de la farmacia (RF09). Se identifica por DNI y es la entidad a la que
 * se asocian afiliaciones a convenios de seguro y lineas de credito.
 */
public final class Cliente extends Entidad {

    private Dni dni;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String correo;
    private String direccion;
    private EstadoCuenta estado;

    public Cliente(Dni dni, String nombres, String apellidos, LocalDate fechaNacimiento,
                   String telefono, String correo, String direccion) {
        super();
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = EstadoCuenta.ACTIVO;
    }

    public Cliente(UUID id, Dni dni, String nombres, String apellidos, LocalDate fechaNacimiento,
                   String telefono, String correo, String direccion, EstadoCuenta estado) {
        super(id);
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = estado;
    }

    public void actualizarDatos(String telefono, String correo, String direccion) {
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public String nombreCompleto() {
        return nombres + " " + apellidos;
    }

    public Dni getDni() {
        return dni;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }
}
