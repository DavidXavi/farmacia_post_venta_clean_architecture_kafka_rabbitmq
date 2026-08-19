package com.posfarmacia.adapters.persistence.mensajeria;

import com.posfarmacia.usecases.dto.mensajeria.ActividadReciente;
import com.posfarmacia.usecases.port.out.mensajeria.ActividadRecienteRepositoryPort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Repository;

/**
 * Feed de actividad reciente en memoria: el proposito de este adaptador es mostrar en el
 * navegador que los consumidores de Kafka y RabbitMQ efectivamente reciben mensajes, no
 * ser un registro de auditoria durable. Si el feed debe sobrevivir un reinicio, se
 * reemplaza por una tabla igual que el resto de {@code adapters-persistence}.
 */
@Repository
public class ActividadRecienteInMemoryRepository implements ActividadRecienteRepositoryPort {

    private static final int CAPACIDAD_MAXIMA = 200;

    private final Deque<ActividadReciente> actividades = new ConcurrentLinkedDeque<>();

    @Override
    public void registrar(ActividadReciente actividad) {
        actividades.addFirst(actividad);
        while (actividades.size() > CAPACIDAD_MAXIMA) {
            actividades.removeLast();
        }
    }

    @Override
    public List<ActividadReciente> listarRecientes(int limite) {
        List<ActividadReciente> copia = new ArrayList<>(actividades);
        copia.sort(Comparator.comparing(ActividadReciente::ocurridoEn).reversed());
        int hasta = Math.min(limite, copia.size());
        return Collections.unmodifiableList(copia.subList(0, hasta));
    }
}
