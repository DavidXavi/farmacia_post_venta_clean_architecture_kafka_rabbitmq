package com.posfarmacia.adapters.persistence.cliente;

import com.posfarmacia.adapters.persistence.mapper.cliente.ClienteMapper;
import com.posfarmacia.adapters.persistence.repository.cliente.ClienteJpaRepository;
import com.posfarmacia.usecases.port.out.cliente.ClienteRepositoryPort;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.valueobject.Dni;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository clienteJpaRepository;
    private final ClienteMapper clienteMapper;

    public ClienteRepositoryAdapter(ClienteJpaRepository clienteJpaRepository, ClienteMapper clienteMapper) {
        this.clienteJpaRepository = clienteJpaRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        var guardado = clienteJpaRepository.save(clienteMapper.toEntity(cliente));
        return clienteMapper.toDomain(guardado);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return clienteJpaRepository.findById(id).map(clienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorDni(Dni dni) {
        return clienteJpaRepository.findByDni(dni.valor()).map(clienteMapper::toDomain);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return clienteJpaRepository.findAll().stream().map(clienteMapper::toDomain).toList();
    }
}
