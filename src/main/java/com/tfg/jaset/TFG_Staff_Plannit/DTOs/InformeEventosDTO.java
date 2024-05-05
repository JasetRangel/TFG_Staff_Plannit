package com.tfg.jaset.TFG_Staff_Plannit.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class InformeEventosDTO {
    //DATOS PARA LA TABLA DE LA VISTA EVENTOS
    private String nombreCliente;
    private Date fechaInicio;
    private Date fechaFin;
    private String descripcion;
    private String id;

    private InformeEmpleadoDTO empleado;


}

