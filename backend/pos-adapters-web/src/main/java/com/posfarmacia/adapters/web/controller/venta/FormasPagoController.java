package com.posfarmacia.adapters.web.controller.venta;

import com.posfarmacia.adapters.web.response.venta.FormaPagoResponse;
import com.posfarmacia.usecases.port.in.venta.ConsultarFormasPagoUseCase;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF12: lista las formas de pago activas. Ruta consumida por
 * `frontend/src/pages/VentaPage.jsx` (`GET /api/formas-pago`) para poblar el selector de pagos.
 */
@RestController
@RequestMapping("/api/formas-pago")
public class FormasPagoController {

    private final ConsultarFormasPagoUseCase consultarFormasPago;

    public FormasPagoController(ConsultarFormasPagoUseCase consultarFormasPago) {
        this.consultarFormasPago = consultarFormasPago;
    }

    @GetMapping
    public List<FormaPagoResponse> listar() {
        return consultarFormasPago.consultar().stream().map(FormaPagoResponse::desde).toList();
    }
}
