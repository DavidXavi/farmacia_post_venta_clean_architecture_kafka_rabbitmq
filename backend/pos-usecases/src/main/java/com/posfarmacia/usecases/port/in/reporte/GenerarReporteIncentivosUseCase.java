package com.posfarmacia.usecases.port.in.reporte;

import com.posfarmacia.usecases.dto.reporte.IncentivoResumenResult;
import com.posfarmacia.usecases.dto.reporte.ReporteIncentivosQuery;
import java.util.List;

/** Puerto de entrada RF18: reporte de incentivos por trabajador, producto y regla aplicada. */
public interface GenerarReporteIncentivosUseCase {

    List<IncentivoResumenResult> generar(ReporteIncentivosQuery query);
}
