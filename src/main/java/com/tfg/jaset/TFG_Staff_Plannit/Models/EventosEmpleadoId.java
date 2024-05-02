package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Embeddable
public class EventosEmpleadoId implements Serializable {
    @Serial
    private static final long serialVersionUID = -4591579896690774919L;
    @Column(name = "evento_id", nullable = false)
    private Integer eventoId;

    @Column(name = "empleado_dni", nullable = false, length = 9)
    private String empleadoDni;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;


}