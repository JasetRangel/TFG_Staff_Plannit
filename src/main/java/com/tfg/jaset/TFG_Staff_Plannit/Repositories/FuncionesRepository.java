package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Funciones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionesRepository extends JpaRepository<Funciones, Integer> {
Optional<Funciones> findById(int id);
Optional<Funciones> findByDescripcion(String descripcion);


}
