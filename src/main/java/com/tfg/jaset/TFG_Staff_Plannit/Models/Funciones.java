package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "funciones")
public class Funciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "valorHora", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorHora;

    @OneToMany(mappedBy = "funcion")
    private Set<EventosEmpleado> eventosEmpleados = new LinkedHashSet<>();

}