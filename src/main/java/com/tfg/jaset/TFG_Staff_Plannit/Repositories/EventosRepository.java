package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventosRepository extends JpaRepository<Evento,Integer> {
    Optional<Evento> findByNombre(String nombre);
    Optional<Evento> findByDescripcion(String descripcion);
}
