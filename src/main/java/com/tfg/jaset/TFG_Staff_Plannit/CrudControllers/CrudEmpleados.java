package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        Empleado empleado= (Empleado) FuncionesMenu.getObjetoSeleccionado();
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


}
