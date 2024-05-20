package com.tfg.jaset.TFG_Staff_Plannit.DTOs;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InformeEmpleadoDTO {
    private Integer anio;
    private String mes;
    private String nombreInforme;
    private String dniEmpleado;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private List<Evento>eventos;

    public InformeEmpleadoDTO(Integer anio, String mes, String nombreInforme,String dniEmpleado, LocalTime horaEntrada, LocalTime horaSalida) {
        this.anio = anio;
        this.mes = mes;
        this.nombreInforme = String.format("%s_%s_%d",nombreInforme,mes,anio);
        this.dniEmpleado = dniEmpleado;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.eventos = new ArrayList<>();
    }

}

