package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;


@Component
public class EmpleadosController implements Initializable {
    @FXML
    private AnchorPane pane;
    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnNewEmpleado;

    @FXML
    private Button btnReporteTotal;

    @FXML
    private Button btnVerDatos;
    @FXML
    private TableColumn<Empleado, String> columDni;

    @FXML
    private TableColumn<Empleado, String> columNombre;

    @FXML
    private TableColumn<Empleado, String> columApellidos;

    @FXML
    private TableColumn<Empleado, String> columDirecicon;

    @FXML
    private TableColumn<Empleado, String> columEmail;

    @FXML
    private TableColumn<Empleado, Integer> columnEdad;

    @FXML
    private TableColumn<Empleado, String> columCuenta;

    @FXML
    private TableColumn<Empleado, String> columTelefono;

    @FXML
    private TableView<Empleado> tablaEmpleados;

    @FXML
    private TextField txtBusqueda;

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columDirecicon.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        columCuenta.setCellValueFactory(new PropertyValueFactory<>("cuentaBancaria"));
        columTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        // Agrega un manejador de clics a la escena
        pane.setOnMouseClicked(event -> {
            // Comprueba si el clic fue fuera de la TableView
            if(!isDescendant(event.getTarget(),tablaEmpleados)){
                tablaEmpleados.getSelectionModel().clearSelection();
            }
        });
        FuncionesMenu.refrescarDatosTabla(tablaEmpleados,empleadoRepository);
    }

    // Verifica si el nodo es o está dentro de la TableView
    private boolean isDescendant(EventTarget target, Node node) {
        if (target instanceof Node) {
            Node current = (Node) target;
            while (current != null) {
                if (current.equals(node)) {
                    return true;
                }
                current = current.getParent();
            }
        }
        return false;
    }
}
