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
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "detalles")
    private String detalles;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Evento> eventos = new LinkedHashSet<>();

    public boolean esDiferente(Cliente c1, Cliente c2) {
        boolean esDiferente = false;
        if (!c1.getId().equals(c2.getId())) {
            esDiferente = true;
        }
        if (!esDiferente && !c1.getNombre().equals(c2.getNombre())) {
            esDiferente = true;
        }
        if (!esDiferente && !c1.getTelefono().equals(c2.getTelefono())) {
            esDiferente = true;
        }
        if (!esDiferente && !c1.getEmail().equals(c2.getEmail())) {
            esDiferente = true;
        }
        if (!esDiferente && !c1.getDetalles().equals(c2.getDetalles())) {
            esDiferente = true;
        }
        return esDiferente;
    }
}
