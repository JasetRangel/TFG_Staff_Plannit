package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado,String> {
    Optional<Empleado> findByDni(String dni);
    Optional<Empleado> findByNombreAndApellidos(String nombre, String apellidos);
    Optional<Empleado> findByNombre(String nombre);
}
