package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.ClientesRepository;
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
import javafx.scene.layout.HBox;
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
    private Button btnListar;

    @FXML
    private Button btnNewEmpleado;

    @FXML
    private Button btnReporteTotal;

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

    @Autowired
    ClientesRepository clientesRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        columDni.prefWidthProperty().bind(tablaEmpleados.widthProperty().multiply(0.1));
        columNombre.prefWidthProperty().bind(tablaEmpleados.widthProperty().multiply(0.15));
        columnEdad.prefWidthProperty().bind(tablaEmpleados.widthProperty().multiply(0.05));
        columApellidos.prefWidthProperty().bind(tablaEmpleados.widthProperty().multiply(0.15));
        columCuenta.prefWidthProperty().bind(tablaEmpleados.widthProperty().multiply(0.15));
        columDirecicon.prefWidthProperty().bind(tablaEmpleados.widthProperty().multiply(0.2));
        columTelefono.prefWidthProperty().bind(tablaEmpleados.widthProperty().multiply(0.1));
        columEmail.prefWidthProperty().bind(tablaEmpleados.widthProperty().multiply(0.10));

        FuncionesMenu.desactivarByPermiso(btnEliminar);
       // FuncionesMenu.configurarEstiloBotones(btnEliminar,btnBuscar,btnNewEmpleado,btnListar,btnReporteTotal);

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

    }

    @FXML
    private void eliminarEmpleado() {
        Empleado empleadoSelected = tablaEmpleados.getSelectionModel().getSelectedItem();
        if(empleadoSelected ==null){
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
            //tablaEmpleados.setItems(FXCollections.observableArrayList(empleadoRepository.findAll()));
            FuncionesMenu.mostrarMensajeAlerta("Campo vacío","Debe rellenar el campo de búsqueda");
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
        FuncionesMenu.limpiarObjetoSeleccionado();
        Main.cambiarVista("/com/java/fx/crudEmpleados.fxml");
    }

    @FXML
    private void reporteTotal(){
        FuncionesMenu.mostrarMensajeAlerta("Componente vacío","En desarrollo");
    }
    @FXML
    private void listar(){
        txtBusqueda.setText("");
        FuncionesMenu.refrescarDatosTabla(tablaEmpleados,empleadoRepository);
    }


}
