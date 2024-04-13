package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @Column(name = "dni", nullable = false, length = 9)
    private String dni;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "email")
    private String email;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "cuentaBancaria")
    private String cuentaBancaria;

    @Column(name = "telefono")
    private String telefono;

    @OneToMany(mappedBy = "empleadoDni")
    private Set<EventosEmpleado> eventosEmpleados = new LinkedHashSet<>();

    @OneToOne(mappedBy = "empleado")
    private Usuario usuario;

}