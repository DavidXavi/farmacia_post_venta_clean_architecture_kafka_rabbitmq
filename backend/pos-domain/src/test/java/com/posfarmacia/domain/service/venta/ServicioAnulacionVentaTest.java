package com.posfarmacia.domain.service.venta;

import static org.assertj.core.api.Assertions.assertThat;

import com.posfarmacia.domain.enums.EstadoVenta;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.DetalleVentaLote;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Prueba minima 13 del Word seccion 11.1: una anulacion del dia devuelve el stock a los lotes
 * originales (sin implementar aqui la nota de credito, que es de otro contexto).
 */
class ServicioAnulacionVentaTest {

    private final ServicioAnulacionVenta servicio = new ServicioAnulacionVenta();

    @Test
    void obtenerReversionesDeStock_devuelve_cada_lote_original_con_su_cantidad_tomada() {
        UUID detalleId = UUID.randomUUID();
        UUID loteUno = UUID.randomUUID();
        UUID loteDos = UUID.randomUUID();
        Instant ahora = Instant.now();

        DetalleVentaLote asignacionUno = new DetalleVentaLote(detalleId, loteUno, new Cantidad(3));
        DetalleVentaLote asignacionDos = new DetalleVentaLote(detalleId, loteDos, new Cantidad(2));
        DetalleVenta detalle = DetalleVenta.reconstruir(detalleId, UUID.randomUUID(), UUID.randomUUID(),
                new Cantidad(5), Dinero.de(10), Porcentaje.CERO, null, null, Dinero.CERO,
                List.of(asignacionUno, asignacionDos));

        Venta venta = Venta.reconstruir(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                null, null, null, ahora, EstadoVenta.CONFIRMADA, 1L, List.of(detalle), List.of(), null);

        List<ReversionStock> reversiones = servicio.obtenerReversionesDeStock(venta);

        assertThat(reversiones).containsExactlyInAnyOrder(
                new ReversionStock(loteUno, new Cantidad(3)),
                new ReversionStock(loteDos, new Cantidad(2)));
    }

    @Test
    void requiereNotaCredito_es_verdadero_cuando_la_venta_no_es_del_mismo_dia() {
        Instant hace2Dias = Instant.now().minus(Duration.ofDays(2));
        Venta venta = Venta.reconstruir(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                null, null, null, hace2Dias, EstadoVenta.CONFIRMADA, 1L, List.of(), List.of(), null);

        LocalDate hoy = LocalDate.now(ZoneId.systemDefault());

        assertThat(servicio.requiereNotaCredito(venta, hoy)).isTrue();
    }
}
