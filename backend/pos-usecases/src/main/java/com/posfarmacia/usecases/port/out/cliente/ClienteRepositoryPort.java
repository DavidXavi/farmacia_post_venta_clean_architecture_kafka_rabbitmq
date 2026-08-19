package com.posfarmacia.usecases.port.out.cliente;

import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.valueobject.Dni;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida hacia la persistencia de {@link Cliente}. */
public interface ClienteRepositoryPort {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(UUID id);

    Optional<Cliente> buscarPorDni(Dni dni);

    List<Cliente> buscarTodos();
}
