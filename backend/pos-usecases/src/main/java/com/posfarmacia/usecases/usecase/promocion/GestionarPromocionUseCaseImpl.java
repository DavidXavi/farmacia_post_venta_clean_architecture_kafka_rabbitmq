package com.posfarmacia.usecases.usecase.promocion;

import com.posfarmacia.usecases.dto.promocion.ActualizarPromocionCommand;
import com.posfarmacia.usecases.dto.promocion.CrearPromocionCommand;
import com.posfarmacia.usecases.port.in.promocion.GestionarPromocionUseCase;
import com.posfarmacia.usecases.port.out.promocion.PromocionRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.promocion.Promocion;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.PeriodoVigencia;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/**
 * CRUD de administracion de promociones (RF06). No decide ninguna regla de aplicacion de
 * promociones durante una venta (eso es responsabilidad de {@code EvaluadorPromociones}, via
 * {@code EvaluarPromocionesUseCase}/{@code SeleccionarPromocionUseCase}).
 */
public class GestionarPromocionUseCaseImpl implements GestionarPromocionUseCase {

    private final PromocionRepositoryPort promociones;

    public GestionarPromocionUseCaseImpl(PromocionRepositoryPort promociones) {
        this.promociones = promociones;
    }

    @Override
    @Transactional
    public Promocion crear(CrearPromocionCommand command) {
        Promocion promocion = Promocion.crear(
                command.nombre(),
                command.descripcion(),
                command.tipoBeneficio(),
                command.valorBeneficio(),
                command.requiereCliente(),
                new Cantidad(command.cantidadMinima()),
                new PeriodoVigencia(command.fechaInicio(), command.fechaFin()));
        for (UUID productoId : productosDe(command.productosParticipantes())) {
            promocion.agregarProductoParticipante(productoId);
        }
        return promociones.guardar(promocion);
    }

    @Override
    @Transactional
    public Promocion actualizar(UUID id, ActualizarPromocionCommand command) {
        Promocion existente = promociones.buscarPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("La promocion indicada no existe."));

        existente.actualizar(
                command.nombre(),
                command.descripcion(),
                command.tipoBeneficio(),
                command.valorBeneficio(),
                command.requiereCliente(),
                new Cantidad(command.cantidadMinima()),
                new PeriodoVigencia(command.fechaInicio(), command.fechaFin()),
                productosDe(command.productosParticipantes()));

        return promociones.guardar(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promocion> listar() {
        return promociones.listar();
    }

    @Override
    @Transactional
    public void desactivar(UUID id) {
        Promocion existente = promociones.buscarPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("La promocion indicada no existe."));
        existente.desactivar();
        promociones.guardar(existente);
    }

    private static List<UUID> productosDe(List<UUID> productosParticipantes) {
        return productosParticipantes == null ? List.of() : productosParticipantes;
    }
}
