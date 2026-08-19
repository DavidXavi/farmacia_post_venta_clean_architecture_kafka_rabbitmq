package com.posfarmacia.infrastructure.configuration.reporte;

import com.posfarmacia.usecases.port.in.incentivo.GestionarReglaIncentivoUseCase;
import com.posfarmacia.usecases.port.in.reporte.ConsultarLotesProximosAVencerUseCase;
import com.posfarmacia.usecases.port.in.reporte.GenerarReporteIncentivosUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.incentivo.IncentivoVentaRepositoryPort;
import com.posfarmacia.usecases.port.out.incentivo.ReglaIncentivoRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.usecases.usecase.incentivo.GestionarReglaIncentivoUseCaseImpl;
import com.posfarmacia.usecases.usecase.reporte.ConsultarLotesProximosAVencerUseCaseImpl;
import com.posfarmacia.usecases.usecase.reporte.GenerarReporteIncentivosUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cablea los casos de uso del contexto Reportes/Incentivos. Los casos de uso son POJOs de
 * pos-usecases (sin anotaciones de Spring); pos-infrastructure es el unico modulo que conoce las
 * implementaciones concretas y las conecta mediante @Bean (mismo patron que
 * VentaUseCaseConfiguration/IdentidadUseCaseConfiguration).
 */
@Configuration
public class ReporteUseCaseConfiguration {

    @Bean
    public GestionarReglaIncentivoUseCase gestionarReglaIncentivoUseCase(ReglaIncentivoRepositoryPort reglas) {
        return new GestionarReglaIncentivoUseCaseImpl(reglas);
    }

    @Bean
    public GenerarReporteIncentivosUseCase generarReporteIncentivosUseCase(IncentivoVentaRepositoryPort incentivos,
            ReglaIncentivoRepositoryPort reglas, VentaRepositoryPort ventas) {
        return new GenerarReporteIncentivosUseCaseImpl(incentivos, reglas, ventas);
    }

    @Bean
    public ConsultarLotesProximosAVencerUseCase consultarLotesProximosAVencerUseCase(LoteRepositoryPort lotes,
            ClockPort clock) {
        return new ConsultarLotesProximosAVencerUseCaseImpl(lotes, clock);
    }
}
