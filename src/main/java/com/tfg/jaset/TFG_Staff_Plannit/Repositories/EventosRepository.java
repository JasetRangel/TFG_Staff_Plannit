package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventosRepository extends JpaRepository<Evento,Integer> {
    Optional<Evento> findById(Integer id);

    void deleteById(Integer id);

    @Query("SELECT COUNT(ee) > 0 FROM EventosEmpleado ee WHERE ee.evento.id = :eventoId")
    boolean existsEmpleadosByEventoId(@Param("eventoId") Integer eventoId);

// Recupero los eventos por año, mes y DNI del empleado para informes detallados de actividades.
// Filtro los eventos donde participa un empleado específico durante un mes y año dados.
    @Query("SELECT e FROM Evento e JOIN e.eventosEmpleados ee JOIN ee.empleadoDni emp WHERE EXTRACT(YEAR FROM e.fechaInicio) = :anio AND FUNCTION('MONTHNAME', e.fechaInicio) = :mes AND emp.dni = :dni")
    List<Evento> findEventosPorInforme(@Param("anio") Integer anio, @Param("mes") String mes, @Param("dni") String dniEmpleado);

// Obtengo los eventos para un cliente específico durante un mes y año determinados.
// Utilizo el nombre del mes y el año de la fecha de inicio del evento para filtrar por cliente.
    @Query("SELECT e FROM Evento e WHERE e.cliente.id = :clienteId AND YEAR(e.fechaInicio) = :anio AND FUNCTION('MONTHNAME',e.fechaInicio)  = :mes")
    List<Evento> findEventosByClienteAndYearAndMonth(@Param("clienteId") Integer clienteId, @Param("anio") Integer anio, @Param("mes") String mes);

// Recupero los datos de los eventos y los almaceno en EventoDTO.
    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoDTO(e.id, c.nombre,c.id, e.fechaInicio, e.fechaFin, e.direccionEvento, e.detalles) " +
            "FROM Evento e JOIN e.cliente c")
    List<EventoDTO> findAllEventosWithClientDetails();

    Optional<Object> findByDireccionEvento(String nombre);

    @Query("SELECT e FROM Evento e JOIN FETCH e.cliente c WHERE c.nombre = :nombreCliente")
    List<Evento> findEventosByNombreCliente(@Param("nombreCliente") String nombreCliente);


    // FETCH me permite acceder a una propiedad, aunque esta sea LAZY
    @Query("SELECT e FROM Evento e JOIN FETCH e.cliente c WHERE c.nombre = :nombreCliente AND c.detalles = :detalles")
    List<Evento> findEventosByNombreClienteAndDetalles(@Param("nombreCliente") String nombreCliente,@Param("detalles")String detalles);
}
