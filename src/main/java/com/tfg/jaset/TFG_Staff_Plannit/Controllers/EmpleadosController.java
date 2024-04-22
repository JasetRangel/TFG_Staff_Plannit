package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.application.Platform;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
        FuncionesMenu.desactivarByPermiso(btnEliminar, btnModificar);
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
        // Agregar un listener a la propiedad selectedItemProperty de la tabla.
        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    FuncionesMenu.mostrarVista(Main.context,InicioController.contenidoPane,"com/java/fx/crudEmpleados.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);//MANEJAR ESTA EXCEPCIÓN
                }
            }
        });
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
    private void datosEmpleados(){

    }

    @FXML
    private void modificarEmpleado(){
        Empleado empleadoSelected=tablaEmpleados.getSelectionModel().getSelectedItem();
        if(empleadoSelected==null){
            FuncionesMenu.mostrarMensajeAlerta("Selección Requerida", "Debe seleccionar un empleado de la tabla");
            return;
        }
        Dialog<Empleado>dialog=new Dialog<>();
        dialog.setTitle("Modificar Empleado");
        dialog.setHeaderText("Editar los detalles del empleado");
        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        GridPane grid=new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtDni=new TextField();
        txtDni.setDisable(true);
        TextField txtNombre=new TextField();
        TextField txtApellidos=new TextField();
        TextField txtDireccion=new TextField();
        TextField txtEmail=new TextField();
        TextField txtEdad=new TextField();
        TextField txtCuentaBancaria=new TextField();
        TextField txtTelefono=new TextField();

        txtNombre.setPromptText("Nombre");
        txtApellidos.setPromptText("Apellidos");
        txtDireccion.setPromptText("Direccion");
        txtEmail.setPromptText("Email");
        txtEdad.setPromptText("Edad");
        txtCuentaBancaria.setPromptText("CuentaBancaria");
        txtTelefono.setPromptText("Telefono");

        txtDni.setText(empleadoSelected.getDni());
        txtNombre.setText(empleadoSelected.getNombre());
        txtApellidos.setText(empleadoSelected.getApellidos());
        txtDireccion.setText(empleadoSelected.getDireccion());
        txtEmail.setText(empleadoSelected.getEmail());
        txtEdad.setText(empleadoSelected.getEdad().toString());
        txtCuentaBancaria.setText(empleadoSelected.getCuentaBancaria().toString());
        txtTelefono.setText(empleadoSelected.getTelefono().toString());


        grid.add(new Label("DNI"), 0, 0);
        grid.add(txtDni, 1, 0);
        grid.add(new Label("Nombre"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new Label("Apellidos"), 0, 2);
        grid.add(txtApellidos, 1, 2);
        grid.add(new Label("Edad"), 0, 3);
        grid.add(txtEdad, 1, 3);
        grid.add(new Label("Email"), 0, 4);
        grid.add(txtEmail, 1, 4);
        grid.add(new Label("Direccion"), 0, 5);
        grid.add(txtDireccion, 1, 5);
        grid.add(new Label("Cuenta Bancaria"), 0, 6);
        grid.add(txtCuentaBancaria, 1, 6);
        grid.add(new Label("Telefono"), 0, 7);
        grid.add(txtTelefono, 1, 7);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(txtBusqueda::requestFocus);
        dialog.setResultConverter(dialogButton ->{
            if(dialogButton == guardarButtonType){
                empleadoSelected.setNombre(txtNombre.getText());
                empleadoSelected.setApellidos(txtApellidos.getText());
                empleadoSelected.setDireccion(txtDireccion.getText());
                empleadoSelected.setEmail(txtEmail.getText());
                empleadoSelected.setCuentaBancaria(txtCuentaBancaria.getText());
                empleadoSelected.setTelefono(txtTelefono.getText());
                empleadoSelected.setEdad(Integer.parseInt(txtEdad.getText()));
                return empleadoSelected;
            }
            return null;
        });
        Optional<Empleado> result = dialog.showAndWait();
        result.ifPresent(empleado -> {
            FuncionesMenu.mostrarMensajeAlerta("Modificación exitosa.","El empleado se ha modificado correctamente.");
            FuncionesMenu.actualizarObjeto(empleadoRepository, empleadoSelected);
            FuncionesMenu.refrescarDatosTabla(tablaEmpleados, empleadoRepository);


        });
    }
}
