package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Evento evento;

    @MapsId("empleadoDni")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_dni", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Empleado empleadoDni;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcion_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Funciones funcion;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;
}
