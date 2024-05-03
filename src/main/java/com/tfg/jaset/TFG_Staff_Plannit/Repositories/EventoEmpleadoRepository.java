package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventoEmpleadoRepository extends JpaRepository<EventosEmpleado, EventosEmpleadoId> {
    List<EventosEmpleado> findByEmpleadoDniDni(String EmpleadoDni); // asumiendo que Empleado tiene un atributo dni
    List<EventosEmpleado> findByEventoId(Integer eventoId); // asumiendo que Evento tiene un atributo id
    List<EventosEmpleado> findByFuncionId(Integer funcionId); // asumiendo que Funciones tiene un atributo id
    Optional<EventosEmpleado> findByEmpleadoDniDniAndEventoId(String dni, Integer id); // asumiendo que Empleado y Evento tienen atributos dni y id respectivamente

    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleado(YEAR(e.id.fecha), MONTHNAME(e.id.fecha), emp.nombre,MIN (e.horaEntrada),MAX (e.horaSalida)) " +
            "FROM EventosEmpleado e " +
            "JOIN e.empleadoDni emp " +
            "JOIN e.evento evento "+
            "WHERE emp.dni = :dniEmpleado "+
            "GROUP BY YEAR (e.id.fecha),MONTHNAME(e.id.fecha), emp.nombre"
    )

    List<InformeEmpleado> findInformesPorEmpleado(String dniEmpleado);

}
