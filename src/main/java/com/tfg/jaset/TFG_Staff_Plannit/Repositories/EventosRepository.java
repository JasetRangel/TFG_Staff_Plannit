package com.tfg.jaset.TFG_Staff_Plannit.Repositories;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Cliente;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface EventosRepository extends JpaRepository<Evento,Integer> {
    Optional<Evento> findById(Integer id);
    Optional<Evento> findByDireccionEvento(String direccionEvento);
    Optional<Evento> findByCliente(Cliente cliente);
    Optional<Evento> findByFechaInicio(Date fechaInicio);
    Optional<Evento> findByFechaFin(Date fechaFin);
}
