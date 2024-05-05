package com.tfg.jaset.TFG_Staff_Plannit.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventoDTO {

    private Integer id;
    private String nombreCliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String direccion;
    private String detalles;

    // Constructor, getters y setters
    public EventoDTO(Integer id, String nombreCliente, LocalDate fechaInicio, LocalDate fechaFin, String direccion, String detalles) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.direccion = direccion;
        this.detalles = detalles;
    }

}
