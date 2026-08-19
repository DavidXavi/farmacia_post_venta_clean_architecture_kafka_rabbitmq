package com.posfarmacia.domain.service.anulacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.exception.DevolucionInvalidaException;
import com.posfarmacia.domain.model.venta.DetalleVentaLote;
import com.posfarmacia.domain.service.venta.ReversionStock;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AsignadorReversionesDevolucionTest {

    private final AsignadorReversionesDevolucion asignador = new AsignadorReversionesDevolucion();

    @Test
    void asignar_distribuye_la_cantidad_a_devolver_entre_los_lotes_originales_en_orden() {
        UUID detalleId = UUID.randomUUID();
        UUID loteUno = UUID.randomUUID();
        UUID loteDos = UUID.randomUUID();
        List<DetalleVentaLote> lotesOriginales = List.of(
                new DetalleVentaLote(detalleId, loteUno, new Cantidad(3)),
                new DetalleVentaLote(detalleId, loteDos, new Cantidad(2)));

        List<ReversionStock> reversiones = asignador.asignar(lotesOriginales, new Cantidad(4));

        assertThat(reversiones).containsExactly(
                new ReversionStock(loteUno, new Cantidad(3)),
                new ReversionStock(loteDos, new Cantidad(1)));
    }

    @Test
    void asignar_lanza_excepcion_si_los_lotes_originales_no_alcanzan_la_cantidad_pedida() {
        UUID detalleId = UUID.randomUUID();
        UUID loteUno = UUID.randomUUID();
        List<DetalleVentaLote> lotesOriginales = List.of(
                new DetalleVentaLote(detalleId, loteUno, new Cantidad(2)));

        assertThatThrownBy(() -> asignador.asignar(lotesOriginales, new Cantidad(5)))
                .isInstanceOf(DevolucionInvalidaException.class);
    }
}
