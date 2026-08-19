package com.posfarmacia.adapters.persistence.repository.catalogo;

import com.posfarmacia.adapters.persistence.mapper.catalogo.ProductoMapper;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.domain.model.catalogo.Producto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final ProductoJpaRepository jpaRepository;

    public ProductoRepositoryAdapter(ProductoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Producto guardar(Producto producto) {
        var guardado = jpaRepository.save(ProductoMapper.aEntidad(producto));
        return ProductoMapper.aDominio(guardado);
    }

    @Override
    public Optional<Producto> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(ProductoMapper::aDominio);
    }

    @Override
    public Optional<Producto> buscarPorCodigoBarras(String codigoBarras) {
        return jpaRepository.findByCodigoBarras(codigoBarras).map(ProductoMapper::aDominio);
    }

    @Override
    public Optional<Producto> buscarPorCodigoInterno(String codigoInterno) {
        return jpaRepository.findByCodigoInterno(codigoInterno).map(ProductoMapper::aDominio);
    }

    @Override
    public List<Producto> buscar(String texto, UUID categoriaId, UUID laboratorioId) {
        String patron = (texto == null || texto.isBlank()) ? null : "%" + texto.toLowerCase() + "%";
        return jpaRepository.buscar(patron, categoriaId, laboratorioId).stream()
                .map(ProductoMapper::aDominio)
                .toList();
    }
}
