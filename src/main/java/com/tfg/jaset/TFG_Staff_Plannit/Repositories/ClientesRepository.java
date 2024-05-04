package com.tfg.jaset.TFG_Staff_Plannit.Repositories;


import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeClientes;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientesRepository extends JpaRepository<Cliente,Integer> {
    Optional<Cliente>findByNombre(String nombre);

    @Query("SELECT new com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeClientes(YEAR(e.fechaInicio), MONTHNAME(e.fechaInicio), CONCAT('Informe ', c.nombre)) " +
            "FROM Evento e JOIN e.cliente c " +
            "WHERE c.id = :clienteId " +
            "GROUP BY YEAR(e.fechaInicio), MONTHNAME(e.fechaInicio), c.nombre")
    List<InformeClientes> findInformesPorCliente(@Param("clienteId") Integer clienteId);



}
