package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO;
import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleadoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventoEmpleadoRepository extends JpaRepository<EventosEmpleado, EventosEmpleadoId> {
     List<EventosEmpleado> findByEventoId(Integer eventoId); // asumiendo que Evento tiene un atributo id

    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleadoDTO(YEAR(e.id.fecha), MONTHNAME(e.id.fecha), emp.nombre,MIN (e.horaEntrada),MAX (e.horaSalida)) " +
            "FROM EventosEmpleado e " +
            "JOIN e.empleadoDni emp " +
            "JOIN e.evento evento "+
            "WHERE emp.dni = :dniEmpleado "+
            "GROUP BY YEAR (e.id.fecha),MONTHNAME(e.id.fecha), emp.nombre"
    )
    List<InformeEmpleadoDTO> findInformesPorEmpleado(String dniEmpleado);

    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO(emp.nombre, emp.apellidos, ee.horaEntrada, ee.horaSalida, func.descripcion) " +
            "FROM EventosEmpleado ee " +
            "JOIN ee.empleadoDni emp " +
            "JOIN ee.funcion func " +
            "WHERE ee.evento.id = :eventoId")
    List<EventoEmpleadosDTO> findDetallesEmpleadosPorEvento(Integer eventoId);


}
