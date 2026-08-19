package com.posfarmacia.infrastructure.receta;

import com.posfarmacia.usecases.port.in.receta.ConsultarHistorialRecetasUseCase;
import com.posfarmacia.usecases.port.in.receta.RegistrarRecetaUseCase;
import com.posfarmacia.usecases.port.in.receta.RevisarRecetaUseCase;
import com.posfarmacia.usecases.port.in.receta.ValidarRecetaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.receta.RecetaRepositoryPort;
import com.posfarmacia.usecases.usecase.receta.ConsultarHistorialRecetasUseCaseImpl;
import com.posfarmacia.usecases.usecase.receta.RegistrarRecetaUseCaseImpl;
import com.posfarmacia.usecases.usecase.receta.RevisarRecetaUseCaseImpl;
import com.posfarmacia.usecases.usecase.receta.ValidarRecetaUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Punto de composicion del contexto de recetas: pos-usecases no depende de Spring
 * mas alla de spring-tx, asi que los casos de uso se instancian aqui con {@code new} y
 * se exponen como beans, en vez de anotarlos con @Service dentro de pos-usecases.
 */
@Configuration
public class RecetaUseCaseConfig {

    @Bean
    public ValidarRecetaUseCase validarRecetaUseCase(RecetaRepositoryPort recetaRepositoryPort, ClockPort clockPort) {
        return new ValidarRecetaUseCaseImpl(recetaRepositoryPort, clockPort);
    }

    @Bean
    public ConsultarHistorialRecetasUseCase consultarHistorialRecetasUseCase(
            RecetaRepositoryPort recetaRepositoryPort) {
        return new ConsultarHistorialRecetasUseCaseImpl(recetaRepositoryPort);
    }

    @Bean
    public RegistrarRecetaUseCase registrarRecetaUseCase(RecetaRepositoryPort recetaRepositoryPort) {
        return new RegistrarRecetaUseCaseImpl(recetaRepositoryPort);
    }

    @Bean
    public RevisarRecetaUseCase revisarRecetaUseCase(RecetaRepositoryPort recetaRepositoryPort) {
        return new RevisarRecetaUseCaseImpl(recetaRepositoryPort);
    }
}
