package com.tfg.jaset.TFG_Staff_Plannit.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni", nullable = false, length = 9, unique = true)
    private String dni;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "dni", referencedColumnName = "dni", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Empleado empleado;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "`contraseña`", nullable = false)
    private String contrasenia;

    @Lob
    @Column(name = "permiso", nullable = false)
    private String permiso;

    public boolean esDiferente(Usuario user1, Usuario user2) {
        boolean sonDiferente = false;
        if(!user1.getNombreUsuario().equals(user2.getNombreUsuario())) {
            sonDiferente = true;
        }
        if(!sonDiferente && !user1.getContrasenia().equals(user2.getContrasenia())) {
            sonDiferente = true;
        }
        if(!sonDiferente && !user1.getPermiso().equals(user2.getPermiso())) {
            sonDiferente = true;
        }
        return sonDiferente;
    }
}
