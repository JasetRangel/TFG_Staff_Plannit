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
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Getter
@Setter
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
    private boolean isAdmin = false;
    private boolean onlyChangePermiso = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar aquí las variables de visibilidad de las contraseñas.
        mostrar.setVisible(true);
        ocultar.setVisible(false);
    }


    public void initializeFields() {
        if (usuario != null) {
            txtDNI.setText(usuario.getDni());
            txtDNI.setStyle("-fx-text-fill: #070707; -fx-background-color:  rgba(77,92,92,0.53)");
            txtDNI.setEditable(false);
            txtDNI.setEditable(false);
            txtusuario.setText(usuario.getNombreUsuario());
            txtContrsenia.setText(usuario.getContrasenia().replaceAll(".", "*")); // Mostrar asteriscos por defecto
            comboPermisos.setValue(usuario.getPermiso());
        }

        // Lógica para manejar la visibilidad y editabilidad de campos según los permisos
        if (isAdmin && onlyChangePermiso) {
            txtContrsenia.setEditable(false);
            txtusuario.setEditable(false);
            mostrar.setVisible(false);
            ocultar.setVisible(false);
        } else if (isAdmin) {
            txtContrsenia.setEditable(true);
            txtusuario.setEditable(true);
            mostrar.setOnMouseClicked(event -> togglePasswordVisibility(true));
            ocultar.setOnMouseClicked(event -> togglePasswordVisibility(false));
        } else {
            txtContrsenia.setEditable(false);
            txtusuario.setEditable(false);
            mostrar.setVisible(false);
            ocultar.setVisible(false);
        }
    }

    public void cargarPermisos() {
        List<String> permisos = userRepository.findDistinctPermisos();
        comboPermisos.setItems(FXCollections.observableArrayList(permisos));
        for(String permiso : permisos) {
            if(permiso.equals("USER")){
                comboPermisos.setValue(permiso);
                return;
            }else{
                comboPermisos.setValue(permisos.getFirst());
            }
        }
    }

    private void togglePasswordVisibility(boolean visible) {
        if(usuario != null) {
            if (visible) {
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
        if (FuncionesMenu.camposCompletos(txtContrsenia, txtusuario)) {
            Usuario usuarioSinCambios = new Usuario();
            usuarioSinCambios.setContrasenia(isPasswordVisible ? txtContrsenia.getText() : usuario.getContrasenia());
            usuarioSinCambios.setNombreUsuario(txtusuario.getText());
            usuarioSinCambios.setDni(txtDNI.getText());
            usuarioSinCambios.setPermiso(comboPermisos.getValue());

            if (usuarioSinCambios.esDiferente(usuarioSinCambios, usuario)) {
                usuario.setDni(txtDNI.getText());
                usuario.setNombreUsuario(txtusuario.getText());
                if (!onlyChangePermiso) {
                    usuario.setContrasenia(isPasswordVisible ? txtContrsenia.getText() : usuario.getContrasenia());
                }
                usuario.setPermiso(comboPermisos.getValue());
                return usuario;
            } else {
                FuncionesMenu.mostrarMensajeAlerta("Usuario sin cambios.", "No se han realizado cambios.");
                return null;
            }
        } else {
            FuncionesMenu.mostrarMensajeAlerta("Campos Vacíos.", "Compruebe que no hay ningún campo vacío.");
            return null;
        }
    }
}
