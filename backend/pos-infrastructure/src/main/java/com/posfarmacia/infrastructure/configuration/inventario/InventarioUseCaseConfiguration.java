package com.posfarmacia.infrastructure.configuration.inventario;

import com.posfarmacia.usecases.port.in.catalogo.GestionarCategoriaUseCase;
import com.posfarmacia.usecases.port.in.catalogo.GestionarLaboratorioUseCase;
import com.posfarmacia.usecases.port.in.catalogo.GestionarPresentacionUseCase;
import com.posfarmacia.usecases.port.in.inventario.BloquearLoteUseCase;
import com.posfarmacia.usecases.port.in.inventario.ConsultarInventarioUseCase;
import com.posfarmacia.usecases.port.in.inventario.ConsultarLotesUseCase;
import com.posfarmacia.usecases.port.in.inventario.ConsultarStockVendibleUseCase;
import com.posfarmacia.usecases.port.in.inventario.GestionarProductoUseCase;
import com.posfarmacia.usecases.port.in.inventario.RegistrarIngresoLoteUseCase;
import com.posfarmacia.usecases.port.in.inventario.RetirarLoteUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.inventario.CategoriaRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LaboratorioRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.PresentacionRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.usecase.catalogo.GestionarCategoriaUseCaseImpl;
import com.posfarmacia.usecases.usecase.catalogo.GestionarLaboratorioUseCaseImpl;
import com.posfarmacia.usecases.usecase.catalogo.GestionarPresentacionUseCaseImpl;
import com.posfarmacia.usecases.usecase.inventario.BloquearLoteUseCaseImpl;
import com.posfarmacia.usecases.usecase.inventario.ConsultarInventarioUseCaseImpl;
import com.posfarmacia.usecases.usecase.inventario.ConsultarLotesUseCaseImpl;
import com.posfarmacia.usecases.usecase.inventario.ConsultarStockVendibleUseCaseImpl;
import com.posfarmacia.usecases.usecase.inventario.GestionarProductoUseCaseImpl;
import com.posfarmacia.usecases.usecase.inventario.RegistrarIngresoLoteUseCaseImpl;
import com.posfarmacia.usecases.usecase.inventario.RetirarLoteUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cablea los casos de uso del contexto Catalogo/Inventario (RF03/RF04/RF14/RF15). Los casos de uso son
 * POJOs de pos-usecases (sin anotaciones de Spring, para que el nucleo sea probable sin framework);
 * pos-infrastructure es el unico modulo que conoce las implementaciones concretas y las conecta mediante
 * @Bean (mismo patron que IdentidadUseCaseConfiguration/VentaUseCaseConfiguration). Antes de esta clase
 * el contexto no tenia ningun bean de caso de uso registrado: ProductosController/LotesController ya
 * exigian estas dependencias por constructor pero nada las provefa.
 */
@Configuration
public class InventarioUseCaseConfiguration {

    @Bean
    public GestionarProductoUseCase gestionarProductoUseCase(ProductoRepositoryPort productos,
            CategoriaRepositoryPort categorias, LaboratorioRepositoryPort laboratorios,
            PresentacionRepositoryPort presentaciones) {
        return new GestionarProductoUseCaseImpl(productos, categorias, laboratorios, presentaciones);
    }

    @Bean
    public ConsultarStockVendibleUseCase consultarStockVendibleUseCase(LoteRepositoryPort lotes, ClockPort clock) {
        return new ConsultarStockVendibleUseCaseImpl(lotes, clock);
    }

    @Bean
    public RegistrarIngresoLoteUseCase registrarIngresoLoteUseCase(LoteRepositoryPort lotes,
            ProductoRepositoryPort productos, ExistenciaLoteRepositoryPort existencias, ClockPort clock) {
        return new RegistrarIngresoLoteUseCaseImpl(lotes, productos, existencias, clock);
    }

    @Bean
    public ConsultarLotesUseCase consultarLotesUseCase(LoteRepositoryPort lotes) {
        return new ConsultarLotesUseCaseImpl(lotes);
    }

    @Bean
    public BloquearLoteUseCase bloquearLoteUseCase(LoteRepositoryPort lotes, ExistenciaLoteRepositoryPort existencias,
            ClockPort clock) {
        return new BloquearLoteUseCaseImpl(lotes, existencias, clock);
    }

    @Bean
    public RetirarLoteUseCase retirarLoteUseCase(LoteRepositoryPort lotes, ExistenciaLoteRepositoryPort existencias,
            ClockPort clock) {
        return new RetirarLoteUseCaseImpl(lotes, existencias, clock);
    }

    @Bean
    public GestionarCategoriaUseCase gestionarCategoriaUseCase(CategoriaRepositoryPort categorias) {
        return new GestionarCategoriaUseCaseImpl(categorias);
    }

    @Bean
    public GestionarLaboratorioUseCase gestionarLaboratorioUseCase(LaboratorioRepositoryPort laboratorios) {
        return new GestionarLaboratorioUseCaseImpl(laboratorios);
    }

    @Bean
    public GestionarPresentacionUseCase gestionarPresentacionUseCase(PresentacionRepositoryPort presentaciones) {
        return new GestionarPresentacionUseCaseImpl(presentaciones);
    }

    @Bean
    public ConsultarInventarioUseCase consultarInventarioUseCase(ExistenciaLoteRepositoryPort existencias) {
        return new ConsultarInventarioUseCaseImpl(existencias);
    }
}
