package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO;
import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleadoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventoEmpleadoRepository extends JpaRepository<EventosEmpleado, EventosEmpleadoId> {
     List<EventosEmpleado> findByEventoId(Integer eventoId); // asumiendo que Evento tiene un atributo id

    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleadoDTO(YEAR(e.fecha), FUNCTION('MONTHNAME', e.fecha), emp.nombre, emp.dni, MIN(e.horaEntrada), MAX(e.horaSalida)) " +
            "FROM EventosEmpleado e " +
            "JOIN e.empleadoDni emp " +
            "JOIN e.evento evento " +
            "WHERE emp.dni = :dniEmpleado " +
            "GROUP BY YEAR(e.fecha), FUNCTION('MONTHNAME', e.fecha), emp.nombre, emp.dni")
    List<InformeEmpleadoDTO> findInformesPorEmpleado(@Param("dniEmpleado") String dniEmpleado);

    @Query("SELECT ee FROM EventosEmpleado ee WHERE ee.evento.id = :eventoId AND ee.empleadoDni.dni = :dniEmpleado AND YEAR(ee.fecha) = :anio AND FUNCTION('MONTHNAME', ee.fecha) = :mes ORDER BY ee.fecha ASC")
    List<EventosEmpleado> findByEventoIdAndEmpleadoDniAndAnioAndMesOrderByFechaAsc(@Param("eventoId") Integer eventoId, @Param("dniEmpleado") String dniEmpleado, @Param("anio") int anio, @Param("mes") String mes);

    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO(emp.nombre, emp.apellidos, ee.horaEntrada, ee.horaSalida, func.descripcion) " +
            "FROM EventosEmpleado ee " +
            "JOIN ee.empleadoDni emp " +
            "JOIN ee.funcion func " +
            "WHERE ee.evento.id = :eventoId")
    List<EventoEmpleadosDTO> findDetallesEmpleadosPorEvento(Integer eventoId);


    List<EventosEmpleado> findByEventoIdOrderByFechaAsc(Long eventoId);

}
