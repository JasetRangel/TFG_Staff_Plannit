package com.tfg.jaset.TFG_Staff_Plannit.Utilidades;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioUtils {
    private static Usuario usuarioActual;// para almacenar el objeto correspondiente al usuario que ha iniciado session

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional// Anotación para manejar la transacción
    public String agregarNuevoUsuario(String dni, String nombreUsuario, String contrasenia, String permiso) {
        Optional<Empleado> empleado = empleadoRepository.findByDni(dni);
        if (!empleado.isPresent()) {
            return "Error: No existe un empleado con el DNI proporcionado.";
        }
        if (usuarioRepository.existsByDni(dni)) {
            return "Error: Ya existe un usuario con el DNI de usuario proporcionado.";
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmpleado(empleado.get());
        nuevoUsuario.setDni(dni);
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setContrasenia(contrasenia);
        nuevoUsuario.setPermiso(permiso);

        usuarioRepository.save(nuevoUsuario);
        return "Usuario agregado correctamente.";
    }
    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

}
