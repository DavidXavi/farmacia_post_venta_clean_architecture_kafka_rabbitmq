package com.posfarmacia.adapters.persistence.mapper.cliente;

import com.posfarmacia.adapters.persistence.entity.cliente.ClienteJpaEntity;
import com.posfarmacia.domain.enums.EstadoCuenta;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.valueobject.Dni;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toDomain(ClienteJpaEntity entity) {
        return new Cliente(entity.getId(), new Dni(entity.getDni()), entity.getNombres(), entity.getApellidos(),
                entity.getFechaNacimiento(), entity.getTelefono(), entity.getCorreo(), entity.getDireccion(),
                EstadoCuenta.valueOf(entity.getEstado()));
    }

    public ClienteJpaEntity toEntity(Cliente cliente) {
        return new ClienteJpaEntity(cliente.getId(), cliente.getDni().valor(), cliente.getNombres(),
                cliente.getApellidos(), cliente.getFechaNacimiento(), cliente.getTelefono(), cliente.getCorreo(),
                cliente.getDireccion(), cliente.getEstado().name());
    }
}
