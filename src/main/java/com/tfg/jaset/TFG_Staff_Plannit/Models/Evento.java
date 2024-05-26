package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "direccion_evento", nullable = false)
    private String direccionEvento;

    @Lob
    @Column(name = "detalles")
    private String detalles;

    @OneToMany(mappedBy = "evento")
    private Set<EventosEmpleado> eventosEmpleados = new LinkedHashSet<>();


    public boolean esDiferente(Evento e1, Evento e2) {
        boolean esDiferente = false;
        if (!e1.getFechaInicio().isEqual(e2.getFechaInicio())) {
            esDiferente = true;
        }
        if(!esDiferente && !e1.getFechaFin().isEqual(e2.getFechaFin())) {
            esDiferente = true;
        }
        if(!esDiferente && !e1.getDireccionEvento().equals(e2.getDireccionEvento())) {
            esDiferente = true;
        }
        if(!esDiferente && !e1.getDetalles().equals(e2.getDetalles())) {
            esDiferente = true;
        }
        return esDiferente;
    }


}