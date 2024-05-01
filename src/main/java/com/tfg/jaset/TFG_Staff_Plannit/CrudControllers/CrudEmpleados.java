package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.UsuarioRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;


@Component
public class CrudEmpleados implements Initializable {
    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnInformar;

    @FXML
    private Button btnVerInforme;

    @FXML
    private  Button btnVolver;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtBanco;

    @FXML
    private TextField txtDNI;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTel;

    @FXML
    private HBox padre;

    @FXML
    private HBox padreBotonones;

    @FXML
    private VBox padreColun1;

    @FXML
    private VBox padreColun2;

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        txtDNI.setFocusTraversable(true);
        txtDireccion.setFocusTraversable(true);
        txtEdad.setFocusTraversable(true);
        txtEmail.setFocusTraversable(true);
        txtNombre.setFocusTraversable(true);
        txtTel.setFocusTraversable(true);
        txtApellidos.setFocusTraversable(true);
        txtBanco.setFocusTraversable(true);

        padreColun2.setFocusTraversable(true);
        padreBotonones.setFocusTraversable(true);
        Empleado empleado= (Empleado) FuncionesMenu.getObjetoSeleccionado();
        if(empleado!=null){
            txtDNI.setText(empleado.getDni());
            txtNombre.setText(empleado.getNombre());
            txtApellidos.setText(empleado.getApellidos());
            txtEdad.setText(empleado.getEdad().toString());
            txtDireccion.setText(empleado.getDireccion());
            txtEmail.setText(empleado.getEmail());
            txtTel.setText(empleado.getTelefono());
            txtBanco.setText(empleado.getCuentaBancaria());

            btnActualizar.setCursor(Cursor.HAND);
            btnEliminar.setCursor(Cursor.HAND);
            btnInformar.setCursor(Cursor.HAND);
            btnVerInforme.setCursor(Cursor.HAND);
            btnVolver.setCursor(Cursor.HAND);
        }
        FuncionesMenu.tabular(padre,padreColun1,padreColun2,padreBotonones);
        FuncionesMenu.configurarEstiloBotones(btnActualizar,btnEliminar,btnInformar,btnVerInforme,btnVolver);

    }
    @FXML
    private void volver(){
        Main.cambiarVista("/com/java/fx/empleados.fxml");
    }
    @FXML
    private void verInforme(){

    }
    @FXML
    private void informar(){

    }
    @FXML
    private void eliminar(){
        Empleado empleadoEliminar= (Empleado) FuncionesMenu.getObjetoSeleccionado();
        Empleado empleadoActual=UsuarioUtils.getUsuarioActual().getEmpleado();
        System.out.println(empleadoEliminar.getDni()+" empleado Actual: "+empleadoActual.getDni());

            if (empleadoEliminar.getDni().equals(UsuarioUtils.getUsuarioActual().getDni())) {
                FuncionesMenu.mostrarMensajeAlerta("Acción no permitida","No puede eliminar a este empleado, "
                +"ya que el usuario con el que ha iniciado sesión, pertenece a este empleado");
            } else {
                if(FuncionesMenu.mostrarDialogConfirmacion("Eliminación Empleado.","¿Está seguro de eliminar a este empleado?")){
                    empleadoRepository.delete(empleadoEliminar);
                }
            }



    }
    @FXML
    private void actualizar(){

    }

}
