package com.posfarmacia.adapters.web.controller.identidad;

import com.posfarmacia.adapters.web.request.identidad.AperturaCajaRequest;
import com.posfarmacia.adapters.web.request.identidad.CierreCajaRequest;
import com.posfarmacia.adapters.web.response.identidad.CajaResponse;
import com.posfarmacia.adapters.web.response.identidad.SesionCajaResponse;
import com.posfarmacia.usecases.port.in.identidad.AbrirCajaUseCase;
import com.posfarmacia.usecases.port.in.identidad.CerrarCajaUseCase;
import com.posfarmacia.usecases.port.in.identidad.ConsultarCajasUseCase;
import com.posfarmacia.usecases.port.in.identidad.ConsultarSesionActivaUseCase;
import com.posfarmacia.domain.model.identidad.Caja;
import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.valueobject.Dinero;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** RF02: consulta, apertura y cierre de caja. */
@RestController
@RequestMapping("/api/cajas")
public class CajasController {

    private final ConsultarCajasUseCase consultarCajas;
    private final ConsultarSesionActivaUseCase consultarSesionActiva;
    private final AbrirCajaUseCase abrirCaja;
    private final CerrarCajaUseCase cerrarCaja;

    public CajasController(ConsultarCajasUseCase consultarCajas, ConsultarSesionActivaUseCase consultarSesionActiva,
                            AbrirCajaUseCase abrirCaja, CerrarCajaUseCase cerrarCaja) {
        this.consultarCajas = consultarCajas;
        this.consultarSesionActiva = consultarSesionActiva;
        this.abrirCaja = abrirCaja;
        this.cerrarCaja = cerrarCaja;
    }

    @GetMapping
    public List<CajaResponse> listar() {
        return consultarCajas.consultar().stream().map(CajaResponse::desde).toList();
    }

    @GetMapping("/{cajaId}/sesion-activa")
    public ResponseEntity<SesionCajaResponse> sesionActiva(@PathVariable UUID cajaId) {
        Optional<SesionCaja> sesion = consultarSesionActiva.consultar(cajaId);
        return sesion.map(SesionCajaResponse::desde).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{cajaId}/aperturas")
    public ResponseEntity<SesionCajaResponse> abrir(@PathVariable UUID cajaId,
                                                      @Valid @RequestBody AperturaCajaRequest request) {
        SesionCaja sesion = abrirCaja.abrir(cajaId, request.usuarioId(), new Dinero(request.montoInicial()));
        return ResponseEntity.status(HttpStatus.CREATED).body(SesionCajaResponse.desde(sesion));
    }

    @PostMapping("/{cajaId}/cierres")
    public SesionCajaResponse cerrar(@PathVariable UUID cajaId, @Valid @RequestBody CierreCajaRequest request) {
        SesionCaja sesion = cerrarCaja.cerrar(cajaId, new Dinero(request.montoDeclarado()), request.observacion());
        return SesionCajaResponse.desde(sesion);
    }
}
