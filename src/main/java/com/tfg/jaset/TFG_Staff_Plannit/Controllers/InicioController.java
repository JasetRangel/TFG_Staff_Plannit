package com.tfg.jaset.TFG_Staff_Plannit.Controllers;



import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.SpringContextUtil;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
@Component
public class InicioController extends MenuController implements Initializable  {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnClienttes;

    @FXML
    private Button btnEmpleados;

    @FXML
    private Button btnMenu;

    @FXML Button btnEventos;

    @FXML
    private Button btnSalir;

    @FXML
    private Button btnUsuarios;

    @FXML
    private Label labelNombre;

    @FXML
    private Label labelHora;

    @FXML
    private Label labelFecha;

    @FXML
    private AnchorPane slider;

    @FXML
    private StackPane contenidoPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ALMACENO AL OBJETO USUARIO ACTUAL
        Usuario usuarioActual= UsuarioUtils.getUsuarioActual();
        //obtengo la hora actual
        LocalTime horaActual= LocalTime.now();
        LocalDate diaActual= LocalDate.now();

        // combio de forma al cursor al pasar por encima de un boton
        btnCerrarSesion.setCursor(Cursor.HAND);
        btnClienttes.setCursor(Cursor.HAND);
        btnEmpleados.setCursor(Cursor.HAND);
        btnSalir.setCursor(Cursor.HAND);
        btnEventos.setCursor(Cursor.HAND);
        btnUsuarios.setCursor(Cursor.HAND);
        btnMenu.setCursor(Cursor.HAND);
        labelNombre.setText(usuarioActual.getNombreUsuario());
        labelHora.setText(horaActual.toString());
        labelFecha.setText(diaActual.toString());
        btnCerrarSesion.setOnAction(event -> {
            try {
                FuncionesMenu.cambiarVentana(event,"/com/java/fx/log.fxml","Log",false);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });



    }
    @FXML
    private  void mostrarMenu(ActionEvent event){
        this.desplazarMenu(event,slider);

    }
    // Método para cambiar el contenido del StackPane
    private void mostrarVista(String fxmlArchivo) throws IOException {
        contenidoPane.getChildren().clear(); // Limpiar el contenido actual
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlArchivo));
        // Establecer la fábrica del controlador para integrar con Spring
        fxmlLoader.setControllerFactory(SpringContextUtil.getContext()::getBean);
        Node vista = fxmlLoader.load(); // Cargar la nueva vista usando el fxmlLoader configurado
        contenidoPane.getChildren().add(vista); // Añadir la vista al StackPane
    }

    @FXML
    private void mostrarClientes(ActionEvent event) throws IOException {
        mostrarVista("/com/java/fx/clientes.fxml");
    }
    @FXML
    private void mostrarEmpleados(ActionEvent event) throws IOException {
        mostrarVista("/com/java/fx/empleados.fxml");
    }
    @FXML
    private void mostrarUsuarios(ActionEvent event) throws IOException {
        mostrarVista("/com/java/fx/usuarios.fxml");
    }
    @FXML
    private void mostrarEventos(ActionEvent event) throws IOException {
        mostrarVista("/com/java/fx/eventos.fxml");
    }


}
