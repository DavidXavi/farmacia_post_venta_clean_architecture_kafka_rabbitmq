package com.posfarmacia.usecases.usecase.reporte;

import com.posfarmacia.usecases.dto.reporte.IncentivoResumenResult;
import com.posfarmacia.usecases.dto.reporte.ReporteIncentivosQuery;
import com.posfarmacia.usecases.port.in.reporte.GenerarReporteIncentivosUseCase;
import com.posfarmacia.usecases.port.out.incentivo.IncentivoVentaRepositoryPort;
import com.posfarmacia.usecases.port.out.incentivo.ReglaIncentivoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.model.incentivo.IncentivoVenta;
import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * RF18: agrupa los incentivos calculados por trabajador y por producto, sumando la cantidad
 * vendida y el monto resultante, e indicando el nombre de la regla aplicada. El producto de cada
 * linea se resuelve consultando la venta original (a diferencia de
 * PosFarmacia.Application.UseCases.GenerarReporteIncentivosUseCase en .NET, que agrupaba por el id
 * de la linea de venta y lo exponia como si fuera el id del producto).
 */
public class GenerarReporteIncentivosUseCaseImpl implements GenerarReporteIncentivosUseCase {

    private final IncentivoVentaRepositoryPort incentivos;
    private final ReglaIncentivoRepositoryPort reglas;
    private final VentaRepositoryPort ventas;

    public GenerarReporteIncentivosUseCaseImpl(IncentivoVentaRepositoryPort incentivos,
            ReglaIncentivoRepositoryPort reglas, VentaRepositoryPort ventas) {
        this.incentivos = incentivos;
        this.reglas = reglas;
        this.ventas = ventas;
    }

    @Override
    public List<IncentivoResumenResult> generar(ReporteIncentivosQuery query) {
        List<IncentivoVenta> registros = incentivos.buscar(query.desde(), query.hasta(), query.usuarioId());

        Map<Clave, List<IncentivoVenta>> agrupados = registros.stream()
                .collect(Collectors.groupingBy(this::clave));

        List<IncentivoResumenResult> resultado = new ArrayList<>();
        for (Map.Entry<Clave, List<IncentivoVenta>> entrada : agrupados.entrySet()) {
            List<IncentivoVenta> lineas = entrada.getValue();
            IncentivoVenta primera = lineas.get(0);

            int cantidadTotal = lineas.stream().mapToInt(i -> i.getCantidad().valor()).sum();
            BigDecimal montoTotal = lineas.stream()
                    .map(i -> i.getMontoCalculado().monto())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            String nombreRegla = reglas.buscarPorId(primera.getReglaIncentivoId())
                    .map(ReglaIncentivo::getNombre)
                    .orElse("");

            resultado.add(new IncentivoResumenResult(entrada.getKey().usuarioId(), entrada.getKey().productoId(),
                    cantidadTotal, nombreRegla, montoTotal));
        }
        return resultado;
    }

    private Clave clave(IncentivoVenta incentivo) {
        return new Clave(incentivo.getUsuarioId(), resolverProductoId(incentivo));
    }

    private UUID resolverProductoId(IncentivoVenta incentivo) {
        return ventas.buscarPorId(incentivo.getVentaId())
                .flatMap(venta -> venta.buscarDetalle(incentivo.getDetalleVentaId()))
                .map(DetalleVenta::getProductoId)
                .orElse(null);
    }

    private record Clave(UUID usuarioId, UUID productoId) {
    }
}
