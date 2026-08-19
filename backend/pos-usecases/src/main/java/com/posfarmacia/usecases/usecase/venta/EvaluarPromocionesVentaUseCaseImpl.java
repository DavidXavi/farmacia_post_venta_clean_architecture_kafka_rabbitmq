package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.promocion.EvaluarPromocionesQuery;
import com.posfarmacia.usecases.dto.venta.PromocionDisponibleResult;
import com.posfarmacia.usecases.port.in.promocion.EvaluarPromocionesUseCase;
import com.posfarmacia.usecases.port.in.venta.EvaluarPromocionesVentaUseCase;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.promocion.Promocion;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.Venta;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF06: reutiliza el motor de promociones de otro contexto para una linea de una venta puntual. */
public class EvaluarPromocionesVentaUseCaseImpl implements EvaluarPromocionesVentaUseCase {

    private final VentaRepositoryPort ventas;
    private final EvaluarPromocionesUseCase evaluarPromociones;

    public EvaluarPromocionesVentaUseCaseImpl(VentaRepositoryPort ventas, EvaluarPromocionesUseCase evaluarPromociones) {
        this.ventas = ventas;
        this.evaluarPromociones = evaluarPromociones;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromocionDisponibleResult> evaluar(UUID ventaId, UUID detalleVentaId) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, ventaId);
        DetalleVenta detalle = venta.buscarDetalle(detalleVentaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("La linea de venta indicada no existe."));

        EvaluarPromocionesQuery query = new EvaluarPromocionesQuery(
                detalle.getProductoId(), detalle.getCantidad().valor(), venta.getClienteId() != null);

        return evaluarPromociones.evaluar(query).stream().map(EvaluarPromocionesVentaUseCaseImpl::aResultado).toList();
    }

    private static PromocionDisponibleResult aResultado(Promocion promocion) {
        return new PromocionDisponibleResult(
                promocion.getId(), promocion.getNombre(), promocion.getTipoBeneficio(),
                promocion.getValorBeneficio());
    }
}
