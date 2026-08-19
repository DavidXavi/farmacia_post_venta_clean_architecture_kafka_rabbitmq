package com.posfarmacia.usecases.port.out.cliente;

import com.posfarmacia.domain.model.cliente.Cliente;
import com.posfarmacia.domain.valueobject.Dni;
import java.util.Optional;

/**
 * Puerto de salida hacia la central (Word, seccion 6.4): consulta y confirma el registro
 * de un cliente en la central. La regla de negocio "c" (registro de pago/convenios) indica
 * que el registro del cliente ante los convenios se realiza en la central; la farmacia
 * mantiene su propia copia local como fuente de verdad operativa (ver ClienteRepositoryPort).
 */
public interface ClienteCentralPort {

    Optional<Cliente> consultarPorDni(Dni dni);

    void registrar(Cliente cliente);
}
