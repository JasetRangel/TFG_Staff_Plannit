package com.tfg.jaset.TFG_Staff_Plannit.Repositories;


import com.tfg.jaset.TFG_Staff_Plannit.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientesRepository extends JpaRepository<Cliente,Integer> {
    Optional<Cliente>findByNombre(String nombre);
}
