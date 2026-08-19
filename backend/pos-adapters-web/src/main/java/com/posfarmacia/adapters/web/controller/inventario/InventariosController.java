package com.posfarmacia.adapters.web.controller.inventario;

import com.posfarmacia.adapters.web.response.inventario.InventarioResponse;
import com.posfarmacia.usecases.port.in.inventario.ConsultarInventarioUseCase;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF15: consulta de solo lectura del stock consolidado por local, ruta
 * "/api/inventarios" para calzar con InventariosController.cs y con InventarioPage.jsx.
 */
@RestController
@RequestMapping("/api/inventarios")
public class InventariosController {

    private final ConsultarInventarioUseCase consultarInventario;

    public InventariosController(ConsultarInventarioUseCase consultarInventario) {
        this.consultarInventario = consultarInventario;
    }

    @GetMapping
    public List<InventarioResponse> listar(@RequestParam UUID localId) {
        return consultarInventario.consultarPorLocal(localId).stream().map(InventarioResponse::desde).toList();
    }
}
