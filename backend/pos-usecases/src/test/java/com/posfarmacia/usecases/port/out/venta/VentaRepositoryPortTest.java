package com.posfarmacia.usecases.port.out.venta;

import static org.assertj.core.api.Assertions.assertThat;

import com.posfarmacia.domain.model.venta.Venta;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Prueba minima 15 del Word seccion 11.1: una venta facturada no se elimina fisicamente (RN05).
 * Se verifica estructuralmente que ni el agregado Venta ni su puerto de persistencia exponen
 * ningun metodo de borrado fisico: el unico camino para cambiar su estado es confirmar/anular.
 */
class VentaRepositoryPortTest {

    @Test
    void ventaRepositoryPort_no_expone_ningun_metodo_de_borrado_fisico() {
        assertThat(nombresDeMetodosSospechosos(VentaRepositoryPort.class)).isEmpty();
    }

    @Test
    void ventaAgregado_no_expone_ningun_metodo_de_borrado_fisico() {
        assertThat(nombresDeMetodosSospechosos(Venta.class)).isEmpty();
    }

    private static List<String> nombresDeMetodosSospechosos(Class<?> clase) {
        return Arrays.stream(clase.getDeclaredMethods())
                .map(Method::getName)
                .map(String::toLowerCase)
                .filter(nombre -> nombre.contains("eliminar") || nombre.contains("borrar") || nombre.contains("delete"))
                .toList();
    }
}
