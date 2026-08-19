package com.posfarmacia.usecases.usecase.catalogo;

import com.posfarmacia.usecases.dto.catalogo.CategoriaResult;
import com.posfarmacia.usecases.port.in.catalogo.GestionarCategoriaUseCase;
import com.posfarmacia.usecases.port.out.inventario.CategoriaRepositoryPort;
import com.posfarmacia.domain.model.catalogo.Categoria;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF03: alta y consulta de categorias del catalogo. */
public class GestionarCategoriaUseCaseImpl implements GestionarCategoriaUseCase {

    private final CategoriaRepositoryPort categorias;

    public GestionarCategoriaUseCaseImpl(CategoriaRepositoryPort categorias) {
        this.categorias = categorias;
    }

    @Override
    @Transactional
    public CategoriaResult crear(String nombre) {
        return aResult(categorias.guardar(new Categoria(nombre)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResult> listar() {
        return categorias.listarTodos().stream().map(GestionarCategoriaUseCaseImpl::aResult).toList();
    }

    private static CategoriaResult aResult(Categoria categoria) {
        return new CategoriaResult(categoria.getId(), categoria.getNombre());
    }
}
