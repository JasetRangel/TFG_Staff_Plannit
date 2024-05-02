package com.tfg.jaset.TFG_Staff_Plannit.DTOs;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InformeEmpleado {
    private Integer anio;
    private String mes;
    private String nombreInforme;
    private BigDecimal horasTotales;
    private List<Evento>eventos;

    public InformeEmpleado(Integer anio, String mes, String nombreInforme, BigDecimal horasTotales) {
        this.anio = anio;
        this.mes = mes;
        this.nombreInforme = String.format("%s_%s_%d",nombreInforme,mes,anio);
        this.horasTotales = horasTotales;
        this.eventos = new ArrayList<>();
    }

}

