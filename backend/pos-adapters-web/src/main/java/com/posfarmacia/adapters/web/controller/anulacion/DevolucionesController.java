package com.posfarmacia.adapters.web.controller.anulacion;

import com.posfarmacia.adapters.web.request.anulacion.LineaDevolucionRequest;
import com.posfarmacia.adapters.web.request.anulacion.RegistrarDevolucionRequest;
import com.posfarmacia.adapters.web.response.anulacion.DevolucionResponse;
import com.posfarmacia.usecases.dto.anulacion.LineaDevolucionCommand;
import com.posfarmacia.usecases.dto.anulacion.RegistrarDevolucionCommand;
import com.posfarmacia.usecases.port.in.anulacion.ConsultarDevolucionesUseCase;
import com.posfarmacia.usecases.port.in.anulacion.RegistrarDevolucionUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada RF16: solo traduce HTTP &lt;-&gt; casos de uso. Rutas EXACTAS a las que ya
 * consume `frontend/src/pages/DevolucionesPage.jsx` (heredadas de
 * `PosFarmacia.Presentation.Controllers.DevolucionesController` en arquitectura_2_t2): base
 * {@code api/devoluciones}, sin prefijo {@code /v1} (ver convenciones-migracion-java.md).
 */
@RestController
@RequestMapping("/api/devoluciones")
public class DevolucionesController {

    private final RegistrarDevolucionUseCase registrarDevolucion;
    private final ConsultarDevolucionesUseCase consultarDevoluciones;

    public DevolucionesController(RegistrarDevolucionUseCase registrarDevolucion,
            ConsultarDevolucionesUseCase consultarDevoluciones) {
        this.registrarDevolucion = registrarDevolucion;
        this.consultarDevoluciones = consultarDevoluciones;
    }

    @PostMapping
    public ResponseEntity<DevolucionResponse> registrar(@Valid @RequestBody RegistrarDevolucionRequest request) {
        List<LineaDevolucionCommand> lineas = request.lineas().stream()
                .map(DevolucionesController::aLineaCommand)
                .toList();
        var command = new RegistrarDevolucionCommand(request.ventaId(), request.usuarioId(), request.motivo(), lineas);
        var resultado = registrarDevolucion.registrar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(DevolucionResponse.desde(resultado));
    }

    @GetMapping
    public List<DevolucionResponse> listar(@RequestParam UUID ventaId) {
        return consultarDevoluciones.consultarPorVenta(ventaId).stream().map(DevolucionResponse::desde).toList();
    }

    private static LineaDevolucionCommand aLineaCommand(LineaDevolucionRequest linea) {
        return new LineaDevolucionCommand(linea.detalleVentaId(), linea.cantidad());
    }
}
