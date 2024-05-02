package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Cliente;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventosRepository extends JpaRepository<Evento,Integer> {
    Optional<Evento> findById(Integer id);
    Optional<Evento> findByDireccionEvento(String direccionEvento);
    Optional<Evento> findByCliente(Cliente cliente);
    Optional<Evento> findByFechaInicio(Date fechaInicio);
    Optional<Evento> findByFechaFin(Date fechaFin);

    @Query("SELECT e FROM Evento e JOIN e.eventosEmpleados ee JOIN ee.empleadoDni emp WHERE EXTRACT(YEAR FROM e.fechaInicio) = :anio AND FUNCTION('MONTHNAME', e.fechaInicio) = :mes AND emp.dni = :dni")
    List<Evento> findEventosPorInforme(@Param("anio") Integer anio, @Param("mes") String mes, @Param("dni") String dniEmpleado);


}
