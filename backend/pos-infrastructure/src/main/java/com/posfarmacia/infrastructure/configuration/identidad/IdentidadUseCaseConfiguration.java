package com.posfarmacia.infrastructure.configuration.identidad;

import com.posfarmacia.usecases.port.in.identidad.AbrirCajaUseCase;
import com.posfarmacia.usecases.port.in.identidad.AutenticarConProveedorUseCase;
import com.posfarmacia.usecases.port.in.identidad.AutenticarUsuarioUseCase;
import com.posfarmacia.usecases.port.in.identidad.CerrarCajaUseCase;
import com.posfarmacia.usecases.port.in.identidad.ConsultarAuditoriaUseCase;
import com.posfarmacia.usecases.port.in.identidad.ConsultarCajasUseCase;
import com.posfarmacia.usecases.port.in.identidad.ConsultarLocalesUseCase;
import com.posfarmacia.usecases.port.in.identidad.ConsultarSesionActivaUseCase;
import com.posfarmacia.usecases.port.in.identidad.GestionarMfaUseCase;
import com.posfarmacia.usecases.port.in.identidad.GestionarRolUseCase;
import com.posfarmacia.usecases.port.in.identidad.GestionarUsuarioUseCase;
import com.posfarmacia.usecases.port.in.identidad.RegistrarAuditoriaUseCase;
import com.posfarmacia.usecases.port.in.identidad.VerificarSegundoFactorUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.identidad.AuditoriaRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.CajaRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.LocalRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.PasswordHasherPort;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.SesionCajaRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.TotpPort;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.FormaPagoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.usecases.usecase.identidad.AbrirCajaUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.AutenticarConProveedorUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.AutenticarUsuarioUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.CerrarCajaUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.ConsultarAuditoriaUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.ConsultarCajasUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.ConsultarLocalesUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.ConsultarSesionActivaUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.GestionarMfaUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.GestionarRolUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.GestionarUsuarioUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.RegistrarAuditoriaUseCaseImpl;
import com.posfarmacia.usecases.usecase.identidad.VerificarSegundoFactorUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cablea los casos de uso del contexto Identidad/Caja/Auditoria con sus adaptadores de
 * puertos de salida. Los casos de uso son POJOs de pos-usecases (sin anotaciones de
 * Spring, para que el nucleo sea probable sin framework); pos-infrastructure es el unico modulo
 * que conoce las implementaciones concretas y las conecta mediante @Bean.
 */
@Configuration
public class IdentidadUseCaseConfiguration {

    @Bean
    public AutenticarUsuarioUseCase autenticarUsuarioUseCase(
            UsuarioRepositoryPort usuarios, RolRepositoryPort roles, PasswordHasherPort passwordHasher) {
        return new AutenticarUsuarioUseCaseImpl(usuarios, roles, passwordHasher);
    }

    /** RF01: login social. El proveedor ya verifico la identidad; aqui solo se resuelve la cuenta. */
    @Bean
    public AutenticarConProveedorUseCase autenticarConProveedorUseCase(
            UsuarioRepositoryPort usuarios, RolRepositoryPort roles, LocalRepositoryPort locales) {
        return new AutenticarConProveedorUseCaseImpl(usuarios, roles, locales);
    }

    /** RF01: segundo factor TOTP (Google Authenticator). */
    @Bean
    public VerificarSegundoFactorUseCase verificarSegundoFactorUseCase(
            UsuarioRepositoryPort usuarios, RolRepositoryPort roles, TotpPort totp) {
        return new VerificarSegundoFactorUseCaseImpl(usuarios, roles, totp);
    }

    @Bean
    public GestionarMfaUseCase gestionarMfaUseCase(UsuarioRepositoryPort usuarios, TotpPort totp) {
        return new GestionarMfaUseCaseImpl(usuarios, totp);
    }

    @Bean
    public AbrirCajaUseCase abrirCajaUseCase(CajaRepositoryPort cajas, SesionCajaRepositoryPort sesiones, ClockPort clock) {
        return new AbrirCajaUseCaseImpl(cajas, sesiones, clock);
    }

    @Bean
    public CerrarCajaUseCase cerrarCajaUseCase(SesionCajaRepositoryPort sesiones, VentaRepositoryPort ventas,
            FormaPagoRepositoryPort formasPago, ClockPort clock) {
        return new CerrarCajaUseCaseImpl(sesiones, ventas, formasPago, clock);
    }

    @Bean
    public RegistrarAuditoriaUseCase registrarAuditoriaUseCase(AuditoriaRepositoryPort auditoria, ClockPort clock) {
        return new RegistrarAuditoriaUseCaseImpl(auditoria, clock);
    }

    @Bean
    public ConsultarAuditoriaUseCase consultarAuditoriaUseCase(AuditoriaRepositoryPort auditoria) {
        return new ConsultarAuditoriaUseCaseImpl(auditoria);
    }

    @Bean
    public ConsultarCajasUseCase consultarCajasUseCase(CajaRepositoryPort cajas) {
        return new ConsultarCajasUseCaseImpl(cajas);
    }

    @Bean
    public ConsultarSesionActivaUseCase consultarSesionActivaUseCase(SesionCajaRepositoryPort sesiones) {
        return new ConsultarSesionActivaUseCaseImpl(sesiones);
    }

    @Bean
    public ConsultarLocalesUseCase consultarLocalesUseCase(LocalRepositoryPort locales) {
        return new ConsultarLocalesUseCaseImpl(locales);
    }

    @Bean
    public GestionarUsuarioUseCase gestionarUsuarioUseCase(
            UsuarioRepositoryPort usuarios, RolRepositoryPort roles, PasswordHasherPort passwordHasher) {
        return new GestionarUsuarioUseCaseImpl(usuarios, roles, passwordHasher);
    }

    @Bean
    public GestionarRolUseCase gestionarRolUseCase(RolRepositoryPort roles) {
        return new GestionarRolUseCaseImpl(roles);
    }
}
