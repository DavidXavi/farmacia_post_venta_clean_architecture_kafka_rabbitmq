package com.posfarmacia.adapters.web.controller.mensajeria;

import com.posfarmacia.adapters.web.response.mensajeria.ActividadRecienteResponse;
import com.posfarmacia.usecases.port.in.mensajeria.ConsultarActividadRecienteUseCase;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone el feed de actividad reciente que confirma, desde el navegador, que los eventos de
 * Kafka (venta confirmada) y las tareas de RabbitMQ (comprobante emitido) llegaron a sus
 * consumidores. Cualquier usuario autenticado puede verla (regla por defecto de SecurityConfig).
 */
@RestController
@RequestMapping("/api/actividad-reciente")
public class ActividadRecienteController {

    private final ConsultarActividadRecienteUseCase consultarActividadReciente;

    public ActividadRecienteController(ConsultarActividadRecienteUseCase consultarActividadReciente) {
        this.consultarActividadReciente = consultarActividadReciente;
    }

    @GetMapping
    public List<ActividadRecienteResponse> listar(@RequestParam(defaultValue = "20") int limite) {
        return consultarActividadReciente.listar(limite).stream().map(ActividadRecienteResponse::desde).toList();
    }
}
