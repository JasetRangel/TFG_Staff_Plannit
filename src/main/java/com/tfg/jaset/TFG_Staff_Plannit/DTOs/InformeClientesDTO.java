package com.tfg.jaset.TFG_Staff_Plannit.DTOs;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class InformeClientesDTO {
    private Integer anio;
    private String mes;
    private String nombreInforme;
    private List<Evento> eventos;

    public InformeClientesDTO(Integer anio, String mes, String nombreInforme) {
        this.anio = anio;
        this.mes = mes;
        this.nombreInforme = nombreInforme;
        this.eventos = new ArrayList<>();
    }
}
