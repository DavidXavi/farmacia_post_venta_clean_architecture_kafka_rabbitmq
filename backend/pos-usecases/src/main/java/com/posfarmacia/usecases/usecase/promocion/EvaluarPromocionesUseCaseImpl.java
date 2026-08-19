package com.posfarmacia.usecases.usecase.promocion;

import com.posfarmacia.usecases.dto.promocion.EvaluarPromocionesQuery;
import com.posfarmacia.usecases.port.in.promocion.EvaluarPromocionesUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.promocion.PromocionRepositoryPort;
import com.posfarmacia.domain.model.promocion.Promocion;
import com.posfarmacia.domain.service.promocion.DatosProductoPromocion;
import com.posfarmacia.domain.service.promocion.EvaluadorPromociones;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.LocalDate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RF06: obtiene las promociones vigentes candidatas para el producto de la linea
 * (puerto de salida) y delega en el motor de dominio la decision de cuales son aplicables.
 * No decide el mismo ninguna regla de negocio: esa responsabilidad es de {@link EvaluadorPromociones}.
 */
public class EvaluarPromocionesUseCaseImpl implements EvaluarPromocionesUseCase {

    private final PromocionRepositoryPort promociones;
    private final ClockPort clock;
    private final EvaluadorPromociones evaluador;

    public EvaluarPromocionesUseCaseImpl(PromocionRepositoryPort promociones, ClockPort clock, EvaluadorPromociones evaluador) {
        this.promociones = promociones;
        this.clock = clock;
        this.evaluador = evaluador;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promocion> evaluar(EvaluarPromocionesQuery query) {
        LocalDate hoy = clock.hoy();
        List<Promocion> candidatas = promociones.buscarVigentesPorProducto(query.productoId(), hoy);
        DatosProductoPromocion datos = new DatosProductoPromocion(
                query.productoId(), new Cantidad(query.cantidad()), query.clienteIdentificado());
        return evaluador.obtenerAplicables(candidatas, datos, hoy);
    }
}
