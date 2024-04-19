package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "eventos_empleados")
public class EventosEmpleado {
    @EmbeddedId
    private EventosEmpleadoId id;

    @MapsId("eventoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @MapsId("empleadoDni")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_dni", nullable = false)
    private Empleado empleadoDni;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcion_id")
    private Funciones funcion;

    @Column(name = "horas_trabajadas", precision = 5, scale = 2)
    private BigDecimal horasTrabajadas;

}