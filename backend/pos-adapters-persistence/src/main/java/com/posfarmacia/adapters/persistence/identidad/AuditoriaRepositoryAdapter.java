package com.posfarmacia.adapters.persistence.identidad;

import com.posfarmacia.adapters.persistence.mapper.identidad.AuditoriaMapper;
import com.posfarmacia.adapters.persistence.repository.identidad.AuditoriaJpaRepository;
import com.posfarmacia.usecases.port.out.identidad.AuditoriaRepositoryPort;
import com.posfarmacia.domain.model.identidad.RegistroAuditoria;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class AuditoriaRepositoryAdapter implements AuditoriaRepositoryPort {

    private final AuditoriaJpaRepository auditorias;

    public AuditoriaRepositoryAdapter(AuditoriaJpaRepository auditorias) {
        this.auditorias = auditorias;
    }

    @Override
    public RegistroAuditoria guardar(RegistroAuditoria registro) {
        auditorias.save(AuditoriaMapper.aEntidad(registro));
        return registro;
    }

    /** Ver el comentario equivalente en VentaRepositoryAdapter: un Instant nulo en una
     * comparacion de rango (>=, <) hace que Postgres no pueda inferir el tipo del parametro
     * (SQLState 42P18), asi que la ausencia de filtro de fecha se resuelve aqui con limites
     * bien definidos en vez de null. */
    private static final Instant SIN_LIMITE_INFERIOR = Instant.EPOCH;
    private static final Instant SIN_LIMITE_SUPERIOR = Instant.parse("9999-12-31T23:59:59Z");

    @Override
    public List<RegistroAuditoria> buscar(LocalDate fecha, String entidad, UUID usuarioId) {
        Instant desde = fecha == null ? SIN_LIMITE_INFERIOR : fecha.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant hasta = fecha == null
                ? SIN_LIMITE_SUPERIOR
                : fecha.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return auditorias.buscar(desde, hasta, entidad, usuarioId).stream()
                .map(AuditoriaMapper::aDominio)
                .toList();
    }
}
