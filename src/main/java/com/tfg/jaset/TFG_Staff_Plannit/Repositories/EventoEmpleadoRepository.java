package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface EventoEmpleadoRepository extends JpaRepository<EventosEmpleado, EventosEmpleadoId> {
    Optional<EventosEmpleado> findByIdCompuesto(EventosEmpleadoId id);
    Optional<EventosEmpleado> findByIdEmpleado(EventosEmpleadoId id);
    Optional<EventosEmpleado> findByIdEmpleadoId(EventosEmpleadoId id);
    Optional<EventosEmpleado>findByFecha(Date fecha);
}
