package com.posfarmacia.infrastructure.configuration.mensajeria;

import com.posfarmacia.usecases.port.in.mensajeria.ConsultarActividadRecienteUseCase;
import com.posfarmacia.usecases.port.in.mensajeria.RegistrarActividadUseCase;
import com.posfarmacia.usecases.port.out.mensajeria.ActividadRecienteRepositoryPort;
import com.posfarmacia.usecases.usecase.mensajeria.ConsultarActividadRecienteUseCaseImpl;
import com.posfarmacia.usecases.usecase.mensajeria.RegistrarActividadUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cablea los casos de uso del feed de actividad reciente alimentado por los consumidores de
 * Kafka/RabbitMQ (pos-adapters-messaging). Mismo patron que el resto de *UseCaseConfiguration:
 * los casos de uso son POJOs de pos-usecases, pos-infrastructure los conecta mediante @Bean.
 */
@Configuration
public class MensajeriaUseCaseConfiguration {

    @Bean
    public RegistrarActividadUseCase registrarActividadUseCase(ActividadRecienteRepositoryPort actividades) {
        return new RegistrarActividadUseCaseImpl(actividades);
    }

    @Bean
    public ConsultarActividadRecienteUseCase consultarActividadRecienteUseCase(
            ActividadRecienteRepositoryPort actividades) {
        return new ConsultarActividadRecienteUseCaseImpl(actividades);
    }
}
