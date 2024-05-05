package com.tfg.jaset.TFG_Staff_Plannit.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class EventoEmpleadosDTO {

    private String nombreEmpleado;
    private String apellidosEmpleado;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String descripcionFuncion;

    // Constructor con todos los campos
    public EventoEmpleadosDTO(String nombreEmpleado, String apellidosEmpleado, LocalTime horaEntrada, LocalTime horaSalida, String descripcionFuncion) {
        this.nombreEmpleado = nombreEmpleado;
        this.apellidosEmpleado = apellidosEmpleado;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.descripcionFuncion = descripcionFuncion;
    }

}
