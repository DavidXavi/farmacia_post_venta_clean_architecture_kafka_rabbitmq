package com.posfarmacia.adapters.external.central;

import com.posfarmacia.usecases.port.out.cliente.ClienteCentralPort;
import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.valueobject.Dni;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * MOCK/STUB de la central de clientes (Word, seccion 12: "servicios simulados/mocks" para
 * ambientes de prueba). Pendiente de reemplazo por la integracion real con la central: la
 * farmacia mantiene su propia copia local como fuente de verdad operativa (ver
 * ClienteRepositoryPort/pos-adapters-persistence); este adaptador solo confirma el
 * registro ante la central sin efectos adicionales.
 */
@Component
public class ClienteCentralMockAdapter implements ClienteCentralPort {

    @Override
    public Optional<Cliente> consultarPorDni(Dni dni) {
        // ponytail: la central aun no expone una consulta real; el flujo actual resuelve
        // el cliente contra la persistencia local (ClienteRepositoryPort).
        return Optional.empty();
    }

    @Override
    public void registrar(Cliente cliente) {
        // ponytail: no-op documentado; sustituir por la llamada real a la central cuando
        // este disponible (RF09, regla de negocio "c").
    }
}
