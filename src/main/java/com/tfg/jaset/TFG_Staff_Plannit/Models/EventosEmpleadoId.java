package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class EventosEmpleadoId implements Serializable {
    private static final long serialVersionUID = -4591579896690774919L;
    @Column(name = "evento_id", nullable = false)
    private Integer eventoId;

    @Column(name = "empleado_dni", nullable = false, length = 9)
    private String empleadoDni;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventosEmpleadoId entity = (EventosEmpleadoId) o;
        return Objects.equals(this.fecha, entity.fecha) &&
                Objects.equals(this.eventoId, entity.eventoId) &&
                Objects.equals(this.empleadoDni, entity.empleadoDni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, eventoId, empleadoDni);
    }

}