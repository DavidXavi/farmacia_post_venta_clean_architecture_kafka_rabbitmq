package com.posfarmacia.domain.model.venta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.enums.EstadoVenta;
import com.posfarmacia.domain.enums.TipoComprobante;
import com.posfarmacia.domain.exception.AnulacionNoPermitidaException;
import com.posfarmacia.domain.exception.PagoInsuficienteException;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Pruebas minimas del Word seccion 11.1 que viven en el agregado Venta:
 * 2. No se puede confirmar una venta con pago insuficiente (RN03/RN04/RN06).
 * 14. Una anulacion fuera del mismo dia se rechaza (RN39/RN40; la nota de credito la emite otro contexto).
 */
class VentaTest {

    private final UUID cajaId = UUID.randomUUID();
    private final UUID sesionCajaId = UUID.randomUUID();
    private final UUID usuarioId = UUID.randomUUID();
    private final Instant ahora = Instant.now();

    private Venta ventaConUnDetalleTotalmenteAsignado(Dinero precioUnitario) {
        Venta venta = new Venta(cajaId, sesionCajaId, usuarioId, null, ahora);
        DetalleVenta detalle = venta.agregarDetalle(UUID.randomUUID(), new Cantidad(1), precioUnitario,
                Porcentaje.CERO, null);
        venta.asignarLoteADetalle(detalle.getId(), UUID.randomUUID(), new Cantidad(1));
        return venta;
    }

    @Test
    void confirmar_lanza_pago_insuficiente_cuando_el_total_pagado_no_cubre_el_total() {
        Venta venta = ventaConUnDetalleTotalmenteAsignado(Dinero.de(100));
        venta.registrarPago(UUID.randomUUID(), Dinero.de(50), null, ahora);

        assertThatThrownBy(() -> venta.confirmar(1L, TipoComprobante.BOLETA, "B001", ahora))
                .isInstanceOf(PagoInsuficienteException.class);
    }

    @Test
    void confirmar_funciona_cuando_el_pago_cubre_exactamente_el_total() {
        Venta venta = ventaConUnDetalleTotalmenteAsignado(Dinero.de(100));
        venta.registrarPago(UUID.randomUUID(), Dinero.de(100), null, ahora);

        venta.confirmar(1L, TipoComprobante.BOLETA, "B001", ahora);

        assertThat(venta.getEstado()).isEqualTo(EstadoVenta.CONFIRMADA);
    }

    @Test
    void anular_permite_una_venta_confirmada_del_mismo_dia() {
        Venta venta = ventaConUnDetalleTotalmenteAsignado(Dinero.de(100));
        venta.registrarPago(UUID.randomUUID(), Dinero.de(100), null, ahora);
        venta.confirmar(1L, TipoComprobante.BOLETA, "B001", ahora);

        LocalDate hoy = ahora.atZone(ZoneId.systemDefault()).toLocalDate();
        venta.anular(hoy);

        assertThat(venta.getEstado()).isEqualTo(EstadoVenta.ANULADA);
    }

    @Test
    void anular_rechaza_una_venta_confirmada_de_un_dia_anterior() {
        Venta venta = ventaConUnDetalleTotalmenteAsignado(Dinero.de(100));
        venta.registrarPago(UUID.randomUUID(), Dinero.de(100), null, ahora);
        venta.confirmar(1L, TipoComprobante.BOLETA, "B001", ahora);

        LocalDate manana = ahora.atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);

        assertThatThrownBy(() -> venta.anular(manana)).isInstanceOf(AnulacionNoPermitidaException.class);
    }
}
