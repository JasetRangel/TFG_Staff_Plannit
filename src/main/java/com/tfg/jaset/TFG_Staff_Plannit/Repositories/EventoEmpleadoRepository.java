package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO;
import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleadoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface EventoEmpleadoRepository extends JpaRepository<EventosEmpleado, EventosEmpleadoId> {
    // List<EventosEmpleado> findByEventoId(Integer eventoId); // asumiendo que Evento tiene un atributo id
    @Query("SELECT ee FROM EventosEmpleado ee WHERE ee.empleadoDni.dni = :dni")
    List<EventosEmpleado> findEventosByEmpleadoDni(@Param("dni") String dni);

    @Query("SELECT ee FROM EventosEmpleado ee JOIN ee.empleadoDni emp WHERE emp.nombre = :nombreEmpleado AND emp.apellidos = :apellidosEmpleado AND ee.id.fecha = :fecha AND ee.id.horaEntrada = :horaEntrada AND ee.id.eventoId = :eventoId")
    EventosEmpleado findByEmpleadoDetails(
            @Param("nombreEmpleado") String nombreEmpleado,
            @Param("apellidosEmpleado") String apellidosEmpleado,
            @Param("fecha") LocalDate fecha,
            @Param("horaEntrada") LocalTime horaEntrada,
            @Param("eventoId") Integer eventoId
    );

    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleadoDTO(YEAR(e.id.fecha), FUNCTION('MONTHNAME', e.id.fecha), emp.nombre, emp.dni, MIN(e.id.horaEntrada), MAX(e.horaSalida)) " +
            "FROM EventosEmpleado e " +
            "JOIN e.empleadoDni emp " +
            "JOIN e.evento evento " +
            "WHERE emp.dni = :dniEmpleado " +
            "GROUP BY YEAR(e.id.fecha), FUNCTION('MONTHNAME', e.id.fecha), emp.nombre, emp.dni")
    List<InformeEmpleadoDTO> findInformesPorEmpleado(@Param("dniEmpleado") String dniEmpleado);


    @Query("SELECT ee FROM EventosEmpleado ee WHERE ee.evento.id = :eventoId AND ee.empleadoDni.dni = :dniEmpleado AND YEAR(ee.id.fecha) = :anio AND FUNCTION('MONTHNAME', ee.id.fecha) = :mes ORDER BY ee.id.fecha ASC")
    List<EventosEmpleado> findByEventoIdAndEmpleadoDniAndAnioAndMesOrderByFechaAsc(@Param("eventoId") Integer eventoId, @Param("dniEmpleado") String dniEmpleado, @Param("anio") int anio, @Param("mes") String mes);

    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO(emp.nombre, emp.apellidos,ee.id.fecha, ee.id.horaEntrada, ee.horaSalida, func.descripcion) " +
            "FROM EventosEmpleado ee " +
            "JOIN ee.empleadoDni emp " +
            "JOIN ee.funcion func " +
            "WHERE ee.evento.id = :eventoId")
    List<EventoEmpleadosDTO> findDetallesEmpleadosPorEvento(@Param("eventoId") Integer eventoId);

    @Query("SELECT e FROM EventosEmpleado e WHERE e.empleadoDni.dni = :dni AND e.evento.id = :eventoId AND e.id.fecha = :fecha")
    List<EventosEmpleado> findByEmpleadoDniAndEventoIdAndFecha(@Param("dni") String dni, @Param("eventoId") Integer eventoId, @Param("fecha") LocalDate fecha);

    @Query("SELECT ee FROM EventosEmpleado ee JOIN FETCH ee.evento e JOIN FETCH e.cliente c WHERE ee.id.empleadoDni = :dni AND ee.id.fecha = :fecha AND ((ee.id.horaEntrada <= :horaSalida AND ee.horaSalida >= :horaEntrada) OR (ee.id.horaEntrada <= :horaSalida AND ee.horaSalida IS NULL))")
    List<EventosEmpleado> findConflictingEvents(@Param("dni") String dni, @Param("fecha") LocalDate fecha, @Param("horaEntrada") LocalTime horaEntrada, @Param("horaSalida") LocalTime horaSalida);


}
