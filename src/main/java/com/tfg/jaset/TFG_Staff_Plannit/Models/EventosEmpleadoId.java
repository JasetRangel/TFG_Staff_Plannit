package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class EventosEmpleadoId implements Serializable {
    private static final long serialVersionUID = 6200176535441162874L;

    @Column(name = "evento_id", nullable = false)
    private Integer eventoId;

    @Column(name = "empleado_dni", nullable = false, length = 9)
    private String empleadoDni;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_entrada", nullable = false)
    private LocalTime horaEntrada;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventosEmpleadoId that = (EventosEmpleadoId) o;
        return Objects.equals(eventoId, that.eventoId) &&
                Objects.equals(empleadoDni, that.empleadoDni) &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(horaEntrada, that.horaEntrada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventoId, empleadoDni, fecha, horaEntrada);
    }
}
