package com.posfarmacia.adapters.persistence.venta;

import com.posfarmacia.adapters.persistence.entity.venta.DetalleVentaJpaEntity;
import com.posfarmacia.adapters.persistence.entity.venta.VentaJpaEntity;
import com.posfarmacia.adapters.persistence.mapper.venta.ComprobanteMapper;
import com.posfarmacia.adapters.persistence.mapper.venta.DetalleVentaLoteMapper;
import com.posfarmacia.adapters.persistence.mapper.venta.DetalleVentaMapper;
import com.posfarmacia.adapters.persistence.mapper.venta.PagoMapper;
import com.posfarmacia.adapters.persistence.mapper.venta.VentaMapper;
import com.posfarmacia.adapters.persistence.repository.venta.ComprobanteJpaRepository;
import com.posfarmacia.adapters.persistence.repository.venta.DetalleVentaJpaRepository;
import com.posfarmacia.adapters.persistence.repository.venta.DetalleVentaLoteJpaRepository;
import com.posfarmacia.adapters.persistence.repository.venta.PagoJpaRepository;
import com.posfarmacia.adapters.persistence.repository.venta.VentaJpaRepository;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.model.venta.Comprobante;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.DetalleVentaLote;
import com.posfarmacia.domain.model.venta.Pago;
import com.posfarmacia.domain.model.venta.Venta;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Implementacion de {@link VentaRepositoryPort} con Spring Data JPA. El agregado Venta se maneja
 * de forma explicita (sin cascada JPA): en cada {@link #guardar(Venta)} se reemplazan por completo
 * los detalles, lotes asignados, pagos y comprobante a partir del estado actual del agregado en
 * memoria, la forma mas simple de sostener consistencia sin afinar cascadas para colecciones que
 * cambian con cada operacion (agregar detalle, aplicar promocion, asignar lote, pagar, confirmar).
 */
@Repository
public class VentaRepositoryAdapter implements VentaRepositoryPort {

    private final VentaJpaRepository ventaJpaRepository;
    private final DetalleVentaJpaRepository detalleVentaJpaRepository;
    private final DetalleVentaLoteJpaRepository detalleVentaLoteJpaRepository;
    private final PagoJpaRepository pagoJpaRepository;
    private final ComprobanteJpaRepository comprobanteJpaRepository;

    public VentaRepositoryAdapter(VentaJpaRepository ventaJpaRepository,
            DetalleVentaJpaRepository detalleVentaJpaRepository,
            DetalleVentaLoteJpaRepository detalleVentaLoteJpaRepository, PagoJpaRepository pagoJpaRepository,
            ComprobanteJpaRepository comprobanteJpaRepository) {
        this.ventaJpaRepository = ventaJpaRepository;
        this.detalleVentaJpaRepository = detalleVentaJpaRepository;
        this.detalleVentaLoteJpaRepository = detalleVentaLoteJpaRepository;
        this.pagoJpaRepository = pagoJpaRepository;
        this.comprobanteJpaRepository = comprobanteJpaRepository;
    }

    @Override
    public Venta guardar(Venta venta) {
        ventaJpaRepository.save(VentaMapper.aEntidad(venta));

        List<DetalleVentaJpaEntity> detallesExistentes = detalleVentaJpaRepository.findByVentaId(venta.getId());
        for (DetalleVentaJpaEntity detalleExistente : detallesExistentes) {
            detalleVentaLoteJpaRepository.deleteByDetalleVentaId(detalleExistente.getId());
        }
        detalleVentaJpaRepository.deleteByVentaId(venta.getId());
        for (DetalleVenta detalle : venta.getDetalles()) {
            detalleVentaJpaRepository.save(DetalleVentaMapper.aEntidad(detalle));
            for (DetalleVentaLote lote : detalle.getLotes()) {
                detalleVentaLoteJpaRepository.save(DetalleVentaLoteMapper.aEntidad(lote));
            }
        }

        pagoJpaRepository.deleteByVentaId(venta.getId());
        for (Pago pago : venta.getPagos()) {
            pagoJpaRepository.save(PagoMapper.aEntidad(pago));
        }

        comprobanteJpaRepository.deleteByVentaId(venta.getId());
        if (venta.getComprobante() != null) {
            comprobanteJpaRepository.save(ComprobanteMapper.aEntidad(venta.getComprobante()));
        }

        return venta;
    }

    @Override
    public Optional<Venta> buscarPorId(UUID id) {
        return ventaJpaRepository.findById(id).map(this::cargarAgregado);
    }

    @Override
    public long siguienteNumeroCorrelativo() {
        return ventaJpaRepository.siguienteNumeroCorrelativo();
    }

    /** Instant "sin limite" cuando no se filtra por fecha: evita pasar un Instant nulo a la
     * consulta JPQL de rango (v.fecha >= :desde AND v.fecha < :hasta). Postgres no siempre
     * puede inferir el tipo de un parametro nulo usado solo dentro de una comparacion de rango
     * (visto en vivo: SQLState 42P18 "could not determine data type of parameter"), asi que se
     * resuelve en Java con limites bien definidos en vez de arrastrar el null hasta el SQL. */
    private static final Instant SIN_LIMITE_INFERIOR = Instant.EPOCH;
    private static final Instant SIN_LIMITE_SUPERIOR = Instant.parse("9999-12-31T23:59:59Z");

    @Override
    public List<Venta> buscar(LocalDate fecha, UUID cajaId, UUID usuarioId, UUID clienteId) {
        Instant desde = fecha == null ? SIN_LIMITE_INFERIOR : fecha.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant hasta = fecha == null
                ? SIN_LIMITE_SUPERIOR
                : fecha.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return ventaJpaRepository.buscar(desde, hasta, cajaId, usuarioId, clienteId).stream()
                .map(this::cargarAgregado)
                .toList();
    }

    private Venta cargarAgregado(VentaJpaEntity entity) {
        List<DetalleVenta> detalles = detalleVentaJpaRepository.findByVentaId(entity.getId()).stream()
                .map(detalleEntidad -> DetalleVentaMapper.aDominio(detalleEntidad,
                        detalleVentaLoteJpaRepository.findByDetalleVentaId(detalleEntidad.getId())))
                .toList();
        List<Pago> pagos = pagoJpaRepository.findByVentaId(entity.getId()).stream().map(PagoMapper::aDominio).toList();
        Comprobante comprobante = comprobanteJpaRepository.findByVentaId(entity.getId())
                .map(ComprobanteMapper::aDominio)
                .orElse(null);
        return VentaMapper.aDominio(entity, detalles, pagos, comprobante);
    }
}
