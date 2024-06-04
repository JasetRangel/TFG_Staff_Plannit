package com.tfg.jaset.TFG_Staff_Plannit.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
public class EventoEmpleadosDTO {

    private String nombreEmpleado;
    private String apellidosEmpleado;
    LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String descripcionFuncion;

    // Constructor con todos los campos
    public EventoEmpleadosDTO(String nombreEmpleado, String apellidosEmpleado,LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida, String descripcionFuncion) {
        this.nombreEmpleado = nombreEmpleado;
        this.apellidosEmpleado = apellidosEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.descripcionFuncion = descripcionFuncion;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventoEmpleadosDTO that)) return false;

        return Objects.equals(getNombreEmpleado(), that.getNombreEmpleado()) && Objects.equals(getApellidosEmpleado(), that.getApellidosEmpleado()) && Objects.equals(getFecha(), that.getFecha()) && Objects.equals(getHoraEntrada(), that.getHoraEntrada()) && Objects.equals(getHoraSalida(), that.getHoraSalida()) && Objects.equals(getDescripcionFuncion(), that.getDescripcionFuncion());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getNombreEmpleado());
        result = 31 * result + Objects.hashCode(getApellidosEmpleado());
        result = 31 * result + Objects.hashCode(getFecha());
        result = 31 * result + Objects.hashCode(getHoraEntrada());
        result = 31 * result + Objects.hashCode(getHoraSalida());
        result = 31 * result + Objects.hashCode(getDescripcionFuncion());
        return result;
    }
}
