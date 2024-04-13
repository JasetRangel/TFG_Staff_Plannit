package com.tfg.jaset.TFG_Staff_Plannit.Repositories;


import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //encuentro el usuario por su nombre
    Optional<Usuario>findByNombreUsuario(String nombreUsuario);
    boolean existsByDni(String dni);  // Método para comprobar si existe un Usuario por DNI


}
