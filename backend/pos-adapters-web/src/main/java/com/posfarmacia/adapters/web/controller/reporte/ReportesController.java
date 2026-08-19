package com.posfarmacia.adapters.web.controller.reporte;

import com.posfarmacia.adapters.web.response.reporte.IncentivoResumenResponse;
import com.posfarmacia.adapters.web.response.reporte.LoteProximoAVencerResponse;
import com.posfarmacia.adapters.web.response.venta.VentaResponse;
import com.posfarmacia.usecases.dto.reporte.ReporteIncentivosQuery;
import com.posfarmacia.usecases.dto.venta.ConsultarVentasDiariasQuery;
import com.posfarmacia.usecases.port.in.reporte.ConsultarLotesProximosAVencerUseCase;
import com.posfarmacia.usecases.port.in.reporte.GenerarReporteIncentivosUseCase;
import com.posfarmacia.usecases.port.in.venta.ConsultarVentasDiariasUseCase;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF17/RF18, contexto de Reportes e Incentivos: solo lectura sobre datos de
 * Ventas y de Lotes, mas el reporte propio de incentivos. Reservado al rol Administrador (ver
 * {@code SecurityConfig}). Rutas EXACTAS que ya consume `frontend/src/pages/ReportesPage.jsx`:
 * base {@code api/reportes}, sin prefijo {@code /v1} (heredadas de
 * {@code PosFarmacia.Presentation.Controllers.ReportesController} en arquitectura_2_t2).
 * {@code ConsultarVentasDiariasUseCase} pertenece al contexto de Ventas (ya implementado); se
 * reutiliza aqui en vez de duplicarlo.
 */
@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private static final int HORIZONTE_DIAS_DEFECTO = 90;

    private final ConsultarVentasDiariasUseCase consultarVentasDiarias;
    private final GenerarReporteIncentivosUseCase generarReporteIncentivos;
    private final ConsultarLotesProximosAVencerUseCase consultarLotesProximosAVencer;

    public ReportesController(ConsultarVentasDiariasUseCase consultarVentasDiarias,
            GenerarReporteIncentivosUseCase generarReporteIncentivos,
            ConsultarLotesProximosAVencerUseCase consultarLotesProximosAVencer) {
        this.consultarVentasDiarias = consultarVentasDiarias;
        this.generarReporteIncentivos = generarReporteIncentivos;
        this.consultarLotesProximosAVencer = consultarLotesProximosAVencer;
    }

    @GetMapping("/ventas-diarias")
    public List<VentaResponse> ventasDiarias(
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) UUID cajaId,
            @RequestParam(required = false) UUID usuarioId,
            @RequestParam(required = false) UUID clienteId) {
        var query = new ConsultarVentasDiariasQuery(fecha, cajaId, usuarioId, clienteId);
        return consultarVentasDiarias.consultar(query).stream().map(VentaResponse::desde).toList();
    }

    @GetMapping("/incentivos")
    public List<IncentivoResumenResponse> incentivos(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(required = false) UUID usuarioId) {
        var query = new ReporteIncentivosQuery(desde, hasta, usuarioId);
        return generarReporteIncentivos.generar(query).stream().map(IncentivoResumenResponse::desde).toList();
    }

    @GetMapping("/lotes-proximos-a-vencer")
    public List<LoteProximoAVencerResponse> lotesProximosAVencer(
            @RequestParam(required = false, defaultValue = "" + HORIZONTE_DIAS_DEFECTO) int diasHorizonte) {
        return consultarLotesProximosAVencer.consultar(diasHorizonte).stream()
                .map(LoteProximoAVencerResponse::desde)
                .toList();
    }
}
