package com.posfarmacia.adapters.web.response.cliente;

import com.posfarmacia.domain.model.cliente.Cliente;
import java.time.LocalDate;
import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String dni,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String telefono,
        String correo,
        String direccion,
        String estado) {

    public static ClienteResponse de(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getDni().valor(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getFechaNacimiento(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                cliente.getDireccion(),
                cliente.getEstado().name());
    }
}
