package com.posfarmacia.usecases.port.out;

import java.time.Instant;
import java.time.LocalDate;

/** Puerto de salida para obtener fecha/hora de forma comprobable (Word, seccion 6.4). Los casos de uso nunca llaman LocalDate.now()/Instant.now() directamente. */
public interface ClockPort {

    LocalDate hoy();

    Instant ahora();
}
