package com.tfg.jaset.TFG_Staff_Plannit.Controllers;



import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    public StackPane contenidoPane;
    private static StackPane contenidoPaneStatic;

    private final DateTimeFormatter formatter= DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contenidoPaneStatic=contenidoPane;// Asigno el StackPane de la instancia al estático
        //ALMACENO AL OBJETO USUARIO ACTUAL
        Usuario usuarioActual= UsuarioUtils.getUsuarioActual();

        FuncionesMenu.configurarEstiloBotones(btnCerrarSesion,btnClienttes,btnEmpleados,btnSalir,btnEventos,btnUsuarios);

        // combio de forma al cursor al pasar por encima de un boton
        btnCerrarSesion.setCursor(Cursor.HAND);
        btnClienttes.setCursor(Cursor.HAND);
        btnEmpleados.setCursor(Cursor.HAND);
        btnSalir.setCursor(Cursor.HAND);
        btnEventos.setCursor(Cursor.HAND);
        btnUsuarios.setCursor(Cursor.HAND);
        btnMenu.setCursor(Cursor.HAND);
        labelNombre.setText(usuarioActual.getEmpleado().getNombre());

        inicializarReloj();

        btnCerrarSesion.setOnAction(event -> {
            try {
                FuncionesMenu.cambiarVentana(event,"/com/java/fx/log.fxml","Log",false);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static StackPane getContenidoPane() {
        return contenidoPaneStatic;
    }

    private void inicializarReloj() {
        Timeline reloj=new Timeline(new KeyFrame(Duration.ZERO, e ->{
            LocalTime horaActual= LocalTime.now();
            labelHora.setText(horaActual.format(formatter));
            LocalDate diaActual= LocalDate.now();
            labelFecha.setText(diaActual.toString());
        }), new KeyFrame(Duration.minutes(1)));
        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();
    }

    @FXML
    private  void mostrarMenu(ActionEvent event){
        this.desplazarMenu(event,slider);
    }
    @FXML
    private void mostrarClientes(ActionEvent event) throws IOException {
        Main.cambiarVista("/com/java/fx/clientes.fxml");
    }
    @FXML
    private void mostrarEmpleados(ActionEvent event) throws IOException {
        Main.cambiarVista("/com/java/fx/empleados.fxml");
    }
    @FXML
    private void mostrarUsuarios(ActionEvent event) throws IOException {
        Main.cambiarVista("/com/java/fx/usuarios.fxml");
    }
    @FXML
    private void mostrarEventos(ActionEvent event) throws IOException {
        Main.cambiarVista("/com/java/fx/eventos.fxml");
    }


}
