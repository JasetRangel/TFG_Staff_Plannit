package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "cuenta_bancaria")
    private String cuentaBancaria;

    @Column(name = "telefono")
    private String telefono;

    @OneToMany(mappedBy = "empleadoDni")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<EventosEmpleado> eventosEmpleados = new LinkedHashSet<>();

    @OneToOne(mappedBy = "empleado")
    private Usuario usuario;

    public Empleado() {
    }

    public boolean esDiferente(Empleado e1, Empleado e2) {
        boolean esDiferente = false;
        if (!e1.getDni().equals(e2.getDni())) {
            esDiferente = true;
        }
        if (!esDiferente && !e1.getNombre().equals(e2.getNombre())) {
            esDiferente = true;
        }
        if (!esDiferente && !e1.getApellidos().equals(e2.getApellidos())) {
            esDiferente = true;
        }
        if (!esDiferente && !e1.getEmail().equals(e2.getEmail())) {
            esDiferente = true;
        }
        if (!esDiferente && !e1.getDireccion().equals(e2.getDireccion())) {
            esDiferente = true;
        }
        if (!esDiferente && !e1.getCuentaBancaria().equals(e2.getCuentaBancaria())) {
            esDiferente = true;
        }
        if (!esDiferente && !e1.getTelefono().equals(e2.getTelefono())) {
            esDiferente = true;
        }
        if (!esDiferente && !e1.getEdad().equals(e2.getEdad())) {
            esDiferente = true;
        }
        return esDiferente;
    }
}
