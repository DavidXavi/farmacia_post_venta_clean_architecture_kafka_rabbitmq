package com.posfarmacia.domain.service.inventario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.domain.exception.StockInsuficienteException;
import com.posfarmacia.domain.model.inventario.Lote;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.CodigoLote;
import com.posfarmacia.domain.valueobject.FechaVencimiento;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Pruebas minimas 11 y 12 del Word seccion 11.1:
 * 11. FEFO selecciona el lote vendible con vencimiento mas cercano.
 * 12. No se venden lotes vencidos ni lotes que vencen dentro de los proximos tres meses.
 */
class AsignadorLotesFEFOTest {

    private final AsignadorLotesFEFO asignador = new AsignadorLotesFEFO();
    private final LocalDate hoy = LocalDate.of(2026, 7, 27);
    private final UUID productoId = UUID.randomUUID();
    private final UUID localId = UUID.randomUUID();

    private Lote lote(String codigo, LocalDate vencimiento, int cantidad) {
        return new Lote(new CodigoLote(codigo), productoId, new FechaVencimiento(vencimiento),
                new Cantidad(cantidad), localId, null);
    }

    @Test
    void selecciona_primero_el_lote_con_vencimiento_mas_cercano() {
        Lote loteLejano = lote("L-LEJANO", hoy.plusYears(2), 50);
        Lote loteCercano = lote("L-CERCANO", hoy.plusMonths(10), 50);

        List<AsignacionLote> asignaciones = asignador.asignar(productoId, new Cantidad(20), hoy,
                List.of(loteLejano, loteCercano));

        assertThat(asignaciones).hasSize(1);
        assertThat(asignaciones.get(0).loteId()).isEqualTo(loteCercano.getId());
        assertThat(asignaciones.get(0).cantidad()).isEqualTo(new Cantidad(20));
    }

    @Test
    void completa_el_despacho_con_varios_lotes_cuando_uno_no_alcanza() {
        Lote lotePrimero = lote("L-1", hoy.plusMonths(10), 10);
        Lote loteSegundo = lote("L-2", hoy.plusYears(1), 30);

        List<AsignacionLote> asignaciones = asignador.asignar(productoId, new Cantidad(25), hoy,
                List.of(loteSegundo, lotePrimero));

        assertThat(asignaciones).hasSize(2);
        assertThat(asignaciones.get(0).loteId()).isEqualTo(lotePrimero.getId());
        assertThat(asignaciones.get(0).cantidad()).isEqualTo(new Cantidad(10));
        assertThat(asignaciones.get(1).loteId()).isEqualTo(loteSegundo.getId());
        assertThat(asignaciones.get(1).cantidad()).isEqualTo(new Cantidad(15));
    }

    @Test
    void rechaza_lotes_vencidos() {
        Lote loteVencido = lote("L-VENCIDO", hoy.minusDays(1), 50);

        assertThatThrownBy(() -> asignador.asignar(productoId, new Cantidad(10), hoy, List.of(loteVencido)))
                .isInstanceOf(StockInsuficienteException.class);
    }

    @Test
    void rechaza_lotes_dentro_del_periodo_preventivo_de_tres_meses() {
        Lote loteEnPeriodoPreventivo = lote("L-PREVENTIVO", hoy.plusMonths(2), 50);

        assertThatThrownBy(() -> asignador.asignar(productoId, new Cantidad(10), hoy, List.of(loteEnPeriodoPreventivo)))
                .isInstanceOf(StockInsuficienteException.class);
    }

    @Test
    void lanza_stock_insuficiente_si_no_hay_stock_total_suficiente() {
        Lote lote = lote("L-1", hoy.plusYears(1), 10);

        assertThatThrownBy(() -> asignador.asignar(productoId, new Cantidad(50), hoy, List.of(lote)))
                .isInstanceOf(StockInsuficienteException.class);
    }

    @Test
    void ignora_lotes_de_otro_producto() {
        Lote loteDeOtroProducto = new Lote(new CodigoLote("L-OTRO"), UUID.randomUUID(),
                new FechaVencimiento(hoy.plusYears(1)), new Cantidad(100), localId, null);
        Lote loteDelProducto = lote("L-MIO", hoy.plusYears(1), 5);

        List<AsignacionLote> asignaciones = asignador.asignar(productoId, new Cantidad(5), hoy,
                List.of(loteDeOtroProducto, loteDelProducto));

        assertThat(asignaciones).hasSize(1);
        assertThat(asignaciones.get(0).loteId()).isEqualTo(loteDelProducto.getId());
    }
}
