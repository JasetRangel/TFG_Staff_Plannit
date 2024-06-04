package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.UsuarioRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ModifyUser implements Initializable {

    @Autowired
    private UsuarioRepository userRepository;

    @FXML
    private TextField txtusuario;

    @FXML
    private TextField txtDNI;

    @FXML
    private TextField txtContrsenia;

    @FXML
    private ComboBox<String> comboPermisos;

    @FXML
    private ImageView mostrar;

    @FXML
    private ImageView ocultar;

    private Usuario usuario;
    private boolean isPasswordVisible = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mostrar.setVisible(true);
        ocultar.setVisible(false);

        mostrar.setOnMouseClicked(event -> togglePasswordVisibility(true));
        ocultar.setOnMouseClicked(event -> togglePasswordVisibility(false));
        setUsuario(usuario);
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            txtDNI.setText(usuario.getDni());
            txtusuario.setText(usuario.getNombreUsuario());
            txtContrsenia.setText(usuario.getContrasenia().replaceAll(".", "*")); // Mostrar asteriscos por defecto
            comboPermisos.setValue(usuario.getPermiso());
        }
    }

    public void cargarPermisos() {
        comboPermisos.setItems(FXCollections.observableArrayList(userRepository.findDistinctPermisos()));
    }

    private void togglePasswordVisibility(boolean visible) {
        if(usuario != null) {
            if (visible ) {
                txtContrsenia.setText(usuario.getContrasenia());
                mostrar.setVisible(false);
                ocultar.setVisible(true);
            } else {
                txtContrsenia.setText(usuario.getContrasenia().replaceAll(".", "*"));
                mostrar.setVisible(true);
                ocultar.setVisible(false);
            }
            isPasswordVisible = visible;
        }
    }

    public Usuario actualizarUsuario() {
        if (FuncionesMenu.camposCompletos(txtContrsenia,txtusuario)) {
            Usuario usuarioSinCambios = new Usuario();
            usuarioSinCambios.setContrasenia(isPasswordVisible ? txtContrsenia.getText() : usuario.getContrasenia());
            usuarioSinCambios.setNombreUsuario(txtusuario.getText());
            usuarioSinCambios.setDni(txtDNI.getText());
            usuarioSinCambios.setPermiso(comboPermisos.getValue());

            if(usuarioSinCambios.esDiferente(usuarioSinCambios,usuario)){
                usuario.setDni(txtDNI.getText());
                usuario.setNombreUsuario(txtusuario.getText());
                usuario.setContrasenia(isPasswordVisible ? txtContrsenia.getText() : usuario.getContrasenia());
                usuario.setPermiso(comboPermisos.getValue());
                return usuario;

            }else{
                FuncionesMenu.mostrarMensajeAlerta("Usuario sin cambios.","No se han realizado Cambios");
                return null;
            }
        } else {
            FuncionesMenu.mostrarMensajeAlerta("Campos Vacíos..","Comprube que no hay ningun campo vacío");
            return null;
        }
    }

    public void cerrarVentana() {
        // No se usa aquí, se manejará en el controlador del diálogo
    }
}
