package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.collections.FXCollections;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;


@Component
public class EmpleadosController implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnNewEmpleado;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnBuscar;

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



    @Autowired
    EmpleadoRepository empleadoRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FuncionesMenu.desactivarByPermiso(btnEliminar, btnModificar);
        columDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columDirecicon.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        columCuenta.setCellValueFactory(new PropertyValueFactory<>("cuentaBancaria"));
        columTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        FuncionesMenu.refrescarDatosTabla(tablaEmpleados,empleadoRepository);

        FuncionesMenu.configurarTabla(tablaEmpleados, empleado -> {
            Empleado empleadoSelected=tablaEmpleados.getSelectionModel().getSelectedItem();
            FuncionesMenu.setObjetoSeleccionado(empleadoSelected);
            Main.cambiarVista("/com/java/fx/crudEmpleados.fxml");
        });

        txtBusqueda.setPromptText("Ingrese el DNI/NIE del empleado");

        // Agrega un manejador de clics a la escena
        pane.setOnMouseClicked(event -> {
            // Comprueba si el clic fue fuera de la TableView
            if(!isDescendant(event.getTarget(),tablaEmpleados)){
                tablaEmpleados.getSelectionModel().clearSelection();
            }
        });
    }

    // Verifica si el nodo es o está dentro de la TableView
    private boolean isDescendant(EventTarget target, Node node) {
        if (target instanceof Node current) {
            while (current != null) {
                if (current.equals(node)) {
                    return true;
                }
                current = current.getParent();
            }
        }
        return false;
    }
    @FXML
    private void eliminarEmpleado() {
        Empleado empleadoSelected=tablaEmpleados.getSelectionModel().getSelectedItem();
        if(empleadoSelected==null){
            FuncionesMenu.mostrarMensajeAlerta("Selección Requerida", "Debe seleccionar un usuario de la tabla");
            return;
        }
        Empleado empleadoActual=UsuarioUtils.getUsuarioActual().getEmpleado();
        if(empleadoActual !=null && empleadoSelected.getDni().equals(empleadoActual.getDni())){
            FuncionesMenu.mostrarMensajeAlerta("Acción Prohibida","No puede eliminar a este empleado ya que el usuario" +
                    "con el que ha iniciado sesión, pertenece a este empleado.");
            return;
        }
        FuncionesMenu.eliminarEntidad(empleadoSelected, empleado -> {
            empleadoRepository.delete(empleado);
            FuncionesMenu.refrescarDatosTabla(tablaEmpleados, empleadoRepository);
            FuncionesMenu.mostrarMensajeAlerta("Eliminación Existosa.","El usuario se ha eliminado correctamente.");
        });
    }
    @FXML
    private void filtrarDatosPorDNI() {
        String filtro = txtBusqueda.getText().trim();
        if (filtro.isEmpty()) {
            // Si no hay filtro, cargar todos los empleados
            tablaEmpleados.setItems(FXCollections.observableArrayList(empleadoRepository.findAll()));
            FuncionesMenu.mostrarMensajeAlerta("Campo requerido","Debe rellenar el campo de búsqueda");
        } else {
            empleadoRepository.findByDni(filtro).ifPresentOrElse(
                    empleadoEncontrado -> {
                        // Si encuentra el empleado, actualizar la tabla mostrando solo ese empleado
                        tablaEmpleados.setItems(FXCollections.observableArrayList(Collections.singletonList(empleadoEncontrado)));
                    },
                    () -> {
                        // Si no encuentra nada, limpiar la tabla
                        tablaEmpleados.setItems(FXCollections.observableArrayList());
                        FuncionesMenu.mostrarMensajeAlerta("Búsqueda", "No se encontró un empleado con el DNI especificado.");
                    }
            );
        }
    }
    @FXML
    private void agregarEmpleado(){
        Main.cambiarVista("/com/java/fx/crudEmpleados.fxml");
    }


}
