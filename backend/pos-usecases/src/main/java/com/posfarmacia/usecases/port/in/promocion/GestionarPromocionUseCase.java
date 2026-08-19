package com.posfarmacia.usecases.port.in.promocion;

import com.posfarmacia.usecases.dto.promocion.ActualizarPromocionCommand;
import com.posfarmacia.usecases.dto.promocion.CrearPromocionCommand;
import com.posfarmacia.domain.model.promocion.Promocion;
import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada: CRUD de administracion de promociones (RF06), reservado al rol
 * Administrador salvo la consulta (ver {@code SecurityConfig}). No reemplaza a
 * {@code EvaluarPromocionesUseCase}/{@code SeleccionarPromocionUseCase}, que resuelven la
 * aplicacion de promociones durante una venta.
 */
public interface GestionarPromocionUseCase {

    Promocion crear(CrearPromocionCommand command);

    Promocion actualizar(UUID id, ActualizarPromocionCommand command);

    List<Promocion> listar();

    void desactivar(UUID id);
}
