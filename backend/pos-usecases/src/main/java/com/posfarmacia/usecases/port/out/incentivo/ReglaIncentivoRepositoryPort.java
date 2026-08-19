package com.posfarmacia.usecases.port.out.incentivo;

import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida: persistencia de {@link ReglaIncentivo} (RF18). */
public interface ReglaIncentivoRepositoryPort {

    ReglaIncentivo guardar(ReglaIncentivo regla);

    Optional<ReglaIncentivo> buscarPorId(UUID id);

    List<ReglaIncentivo> listar();
}
