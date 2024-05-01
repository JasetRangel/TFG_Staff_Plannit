package com.tfg.jaset.TFG_Staff_Plannit.Controllers;



import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.UsuarioRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class LogginController implements Initializable {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @FXML
    private AnchorPane logPane;

    @FXML
    private TextField txtNomUsuario;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txtNomUsuario.setFocusTraversable(true);
        txtPassword.setFocusTraversable(true);
        btnLogin.setFocusTraversable(true);
        FuncionesMenu.tabular(logPane);
        FuncionesMenu.configurarEstiloBotones(btnLogin);


        btnLogin.setCursor(Cursor.HAND);
    }

    @FXML
    private void verificarUsuario(ActionEvent event) throws IOException {
        String nombreUser=txtNomUsuario.getText();
        String password=txtPassword.getText();
        if(validarCampos()){
            FuncionesMenu.mostrarMensajeAlerta("Debe rellenar todos los campos del formulario", "Campo Vacío");
            return;
        }
        Optional<Usuario>userLogeado=usuarioRepository.findByNombreUsuario(nombreUser);
        if(userLogeado.isPresent() && userLogeado.get().getContrasenia().equals(password)){
            //el usuario existe
            //almaceno al usuario que ha iniciado sesión
            UsuarioUtils.setUsuarioActual(userLogeado.get());

            FuncionesMenu.cambiarVentana(event,"/com/java/fx/inicio.fxml","INICIO",true);
        }else{
            // Usuario no existe o contraseña incorrecta
            FuncionesMenu.mostrarMensajeAlerta("Usuario o contraseña incorrecto", "Campo Incorrecto");
        }

    }

    private boolean validarCampos(){
        boolean algunCampoVacio=false;
        for (Node node:logPane.getChildren()){
            //verifico si hay algun campo vacio
            if(node instanceof  TextField){
                TextField txt= (TextField) node;
                if(txt.getText().isEmpty()){
                    algunCampoVacio=true;
                    break;
                }
            }
        }
        return algunCampoVacio;
    }


}
