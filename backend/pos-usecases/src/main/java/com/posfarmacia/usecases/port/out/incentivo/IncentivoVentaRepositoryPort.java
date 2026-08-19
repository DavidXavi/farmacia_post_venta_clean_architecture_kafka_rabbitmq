package com.posfarmacia.usecases.port.out.incentivo;

import com.posfarmacia.domain.model.incentivo.IncentivoVenta;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Puerto de salida: persistencia de {@link IncentivoVenta} (RF18). */
public interface IncentivoVentaRepositoryPort {

    IncentivoVenta guardar(IncentivoVenta incentivo);

    /** Filtro RF18: {@code usuarioId} nulo se ignora; {@code desde}/{@code hasta} son inclusivos. */
    List<IncentivoVenta> buscar(LocalDate desde, LocalDate hasta, UUID usuarioId);
}
