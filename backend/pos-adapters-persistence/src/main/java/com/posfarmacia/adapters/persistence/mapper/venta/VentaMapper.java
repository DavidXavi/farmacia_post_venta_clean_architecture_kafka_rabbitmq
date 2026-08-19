package com.posfarmacia.adapters.persistence.mapper.venta;

import com.posfarmacia.adapters.persistence.entity.venta.VentaJpaEntity;
import com.posfarmacia.domain.enums.EstadoVenta;
import com.posfarmacia.domain.model.venta.Comprobante;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.Pago;
import com.posfarmacia.domain.model.venta.Venta;
import java.util.List;

public final class VentaMapper {

    private VentaMapper() {
    }

    public static Venta aDominio(VentaJpaEntity entity, List<DetalleVenta> detalles, List<Pago> pagos,
            Comprobante comprobante) {
        return Venta.reconstruir(
                entity.getId(),
                entity.getCajaId(),
                entity.getSesionCajaId(),
                entity.getUsuarioId(),
                entity.getClienteId(),
                entity.getConvenioSeguroId(),
                entity.getLineaCreditoId(),
                entity.getFecha(),
                EstadoVenta.valueOf(entity.getEstado()),
                entity.getNumeroCorrelativo(),
                detalles,
                pagos,
                comprobante);
    }

    public static VentaJpaEntity aEntidad(Venta venta) {
        return new VentaJpaEntity(
                venta.getId(),
                venta.getCajaId(),
                venta.getSesionCajaId(),
                venta.getUsuarioId(),
                venta.getClienteId(),
                venta.getConvenioSeguroId(),
                venta.getLineaCreditoId(),
                venta.getFecha(),
                venta.getEstado().name(),
                venta.getNumeroCorrelativo());
    }
}
