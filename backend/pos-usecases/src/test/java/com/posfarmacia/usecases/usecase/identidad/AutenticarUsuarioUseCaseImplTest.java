package com.posfarmacia.usecases.usecase.identidad;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.usecases.port.out.identidad.PasswordHasherPort;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.exception.CredencialesInvalidasException;
import com.posfarmacia.domain.model.identidad.Rol;
import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Prueba el caso de uso de autenticacion (RF01) con un fake de PasswordHasherPort en vez de
 * BCrypt real: el algoritmo concreto de hash vive en el adaptador REST, la aplicacion solo
 * depende del contrato PasswordHasherPort.
 */
class AutenticarUsuarioUseCaseImplTest {

    /** Fake simple: invierte la contrasena para "hashearla", suficiente para probar el flujo. */
    private static final class PasswordHasherFake implements PasswordHasherPort {
        @Override
        public String hash(String password) {
            return new StringBuilder(password).reverse().toString();
        }

        @Override
        public boolean verificar(String password, String hash) {
            return hash(password).equals(hash);
        }
    }

    private final PasswordHasherFake passwordHasher = new PasswordHasherFake();
    private final Map<String, Usuario> usuariosPorNombre = new HashMap<>();
    private final Map<UUID, Rol> rolesPorId = new HashMap<>();

    private final UsuarioRepositoryPort usuarioRepositoryPort = new UsuarioRepositoryPort() {
        @Override
        public Optional<Usuario> buscarPorId(UUID id) {
            return usuariosPorNombre.values().stream().filter(u -> u.getId().equals(id)).findFirst();
        }

        @Override
        public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
            return Optional.ofNullable(usuariosPorNombre.get(nombreUsuario));
        }

        @Override
        public Usuario guardar(Usuario usuario) {
            usuariosPorNombre.put(usuario.getNombreUsuario(), usuario);
            return usuario;
        }

        @Override
        public List<Usuario> listarTodos() {
            return List.copyOf(usuariosPorNombre.values());
        }
    };

    private final RolRepositoryPort rolRepositoryPort = new RolRepositoryPort() {
        @Override
        public Optional<Rol> buscarPorId(UUID id) {
            return Optional.ofNullable(rolesPorId.get(id));
        }

        @Override
        public Optional<Rol> buscarPorNombre(RolNombre nombre) {
            return rolesPorId.values().stream().filter(rol -> rol.getNombre() == nombre).findFirst();
        }

        @Override
        public List<Rol> listarTodos() {
            return List.copyOf(rolesPorId.values());
        }
    };

    private final AutenticarUsuarioUseCaseImpl useCase =
            new AutenticarUsuarioUseCaseImpl(usuarioRepositoryPort, rolRepositoryPort, passwordHasher);

    private Rol rolCajero;

    @BeforeEach
    void setUp() {
        rolCajero = new Rol(RolNombre.CAJERO, "Cajero de tienda");
        rolesPorId.put(rolCajero.getId(), rolCajero);
    }

    @Test
    void autenticaConCredencialesCorrectasYDevuelveElRolAsignado() {
        UUID localId = UUID.randomUUID();
        Usuario usuario = new Usuario("jperez", passwordHasher.hash("clave-secreta"), localId);
        usuario.asignarRol(rolCajero.getId());
        usuarioRepositoryPort.guardar(usuario);

        UsuarioAutenticado resultado = useCase.autenticar("jperez", "clave-secreta");

        assertThat(resultado.usuarioId()).isEqualTo(usuario.getId());
        assertThat(resultado.nombreUsuario()).isEqualTo("jperez");
        assertThat(resultado.roles()).containsExactly(RolNombre.CAJERO);
        assertThat(resultado.localId()).isEqualTo(localId);
    }

    @Test
    void rechazaUnaContrasenaIncorrecta() {
        Usuario usuario = new Usuario("jperez", passwordHasher.hash("clave-secreta"), UUID.randomUUID());
        usuarioRepositoryPort.guardar(usuario);

        assertThatThrownBy(() -> useCase.autenticar("jperez", "clave-equivocada"))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    void rechazaUnUsuarioQueNoExiste() {
        assertThatThrownBy(() -> useCase.autenticar("no-existe", "cualquiera"))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    void rechazaUnUsuarioSuspendidoAunqueLaContrasenaSeaCorrecta() {
        Usuario usuario = new Usuario("jperez", passwordHasher.hash("clave-secreta"), UUID.randomUUID());
        usuario.suspender();
        usuarioRepositoryPort.guardar(usuario);

        assertThatThrownBy(() -> useCase.autenticar("jperez", "clave-secreta"))
                .isInstanceOf(CredencialesInvalidasException.class);
    }
}
