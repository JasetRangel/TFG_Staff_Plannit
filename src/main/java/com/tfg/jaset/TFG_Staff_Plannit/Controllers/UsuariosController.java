package com.tfg.jaset.TFG_Staff_Plannit.Controllers;


import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.UsuarioRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class UsuariosController implements Initializable {

    @Autowired
    UsuarioRepository userRepository;
    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioUtils userUtils;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnNewUser;

    @FXML
    private Button btnPassword;

    @FXML
    private Button btnVerDatos;

    @FXML
    private TableView<Usuario> table;

    @FXML
    private TableColumn<Usuario, String> columDNI;

    @FXML
    private TableColumn<Usuario, String> columPermiso;

    @FXML
    private TableColumn<Usuario, String> columUser;

    @FXML
    AnchorPane pane;

    @FXML
    private TextField txtBusqueda;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // hago que el raton cambie de forma cuando pasa por un boton
        btnModificar.setCursor(Cursor.HAND);
        btnBuscar.setCursor(Cursor.HAND);
        btnEliminar.setCursor(Cursor.HAND);
        btnPassword.setCursor(Cursor.HAND);
        btnVerDatos.setCursor(Cursor.HAND);
        btnNewUser.setCursor(Cursor.HAND);

        refrescarTablaUsuarios();

        //configuro los datos que quiero mostrar y los asocio con las propiedades de la clase Usuario
        columDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columUser.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        columPermiso.setCellValueFactory(new PropertyValueFactory<>("permiso"));

        // Agrega un manejador de clics a la escena
        pane.setOnMouseClicked(event -> {
            // Comprueba si el clic fue fuera de la TableView
            if(!isDescendant(event.getTarget(),table)){
                table.getSelectionModel().clearSelection();
            }
        });
///////////////////////----------------------------ACIONES DE LOS BOTONES -------------------------///////////////////////////7
        btnModificar.setOnAction(event -> {
            if(table.getSelectionModel().getSelectedItem()==null){
                FuncionesMenu.mostrarMensajeAlerta("Selleción Requerida","Debe seleccionar un campo de la tabla");
            }
            else
                mostrarDialogModificarUsuario();
        });

        btnNewUser.setOnAction(event -> {
            mostrarDialogAgregarUsuario();
        });

        btnEliminar.setOnAction(event->{
            eliminarUsuario();
        });


    }

    ///////////////////////----------------------------MÉTODOS -------------------------///////////////////////////7

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
    public void mostrarDialogAgregarUsuario() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Usuario");
        dialog.setHeaderText("Ingrese los datos del nuevo usuario");

        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField dniUser = new TextField();
        dniUser.setPromptText("DNI del Usuario");
        dniUser.requestFocus();
        TextField nombreUsuario = new TextField();
        nombreUsuario.setPromptText("Nombre de Usuario");


        PasswordField password = new PasswordField();
        password.setPromptText("Contraseña");
        TextField permiso = new TextField();
        permiso.setPromptText("Permiso");

        grid.add(new Label("DNI Usuario:"), 0, 0);
        grid.add(dniUser, 1, 0);
        grid.add(new Label("Nombre Usuario:"), 0, 1);
        grid.add(nombreUsuario, 1, 1);
        grid.add(new Label("Contraseña:"), 0, 2);
        grid.add(password, 1, 2);
        grid.add(new Label("Permiso:"), 0, 3);
        grid.add(permiso, 1, 3);

        dialog.getDialogPane().setContent(grid);


        Node guardarButton = dialog.getDialogPane().lookupButton(guardarButtonType);
        guardarButton.addEventFilter(ActionEvent.ACTION, event -> {
            String resultado = userUtils.agregarNuevoUsuario(dniUser.getText(), nombreUsuario.getText(), password.getText(), permiso.getText());
            if (!resultado.equals("Usuario agregado correctamente.")) {
                event.consume();
                FuncionesMenu.mostrarMensajeAlerta("Error en la Insersión",resultado);
            }
            else{
                FuncionesMenu.mostrarMensajeAlerta("Insersión exitosa",resultado);
            }
        });
        dialog.getDialogPane().getScene().getWindow().addEventFilter(WindowEvent.WINDOW_SHOWN, event -> {
            dniUser.requestFocus(); // Establece el foco inicial en nombreUsuario cuando el diálogo se muestra
        });
        FuncionesMenu.tabular(grid);
        dialog.showAndWait();
    }
    @FXML
   private  void mostrarDialogModificarUsuario(){
        Usuario userSelected=table.getSelectionModel().getSelectedItem();
        //creo el DIALOG para modificar al usuario
       Dialog<Usuario>dialog=new Dialog<>();
       dialog.setTitle("Modificar Usuario");
       dialog.setHeaderText("Editar los detalles del usuario seleccionado.");
       // Configurar botones
       ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
       dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

       // Crear el formulario para editar
       GridPane grid = new GridPane();
       grid.setHgap(10);
       grid.setVgap(10);
       grid.setPadding(new Insets(20, 150, 10, 10));

       TextField nombreUsuario = new TextField();
       nombreUsuario.setPromptText("Nombre de Usuario");
       nombreUsuario.setText(userSelected.getNombreUsuario());

       TextField permiso = new TextField();
       permiso.setPromptText("Permiso");
       permiso.setText(userSelected.getPermiso());

       grid.add(new Label("Nombre de Usuario:"), 0, 0);
       grid.add(nombreUsuario, 1, 0);
       grid.add(new Label("Permiso:"), 0, 1);
       grid.add(permiso, 1, 1);

       // Habilita/deshabilita el botón de guardar dependiendo de si el nombreUsuario está vacío
       Node guardarButton = dialog.getDialogPane().lookupButton(guardarButtonType);
       guardarButton.setDisable(true);

       // Validación en tiempo real para el campo nombreUsuario
       nombreUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
           guardarButton.setDisable(newValue.trim().isEmpty());
       });

       dialog.getDialogPane().setContent(grid);
       // Request focus en el nombre de usuario inicialmente
       Platform.runLater(nombreUsuario::requestFocus);
       // Convertir el resultado del diálogo a un par nombre de usuario/permiso cuando se presiona el botón de guardar
       dialog.setResultConverter(dialogButton -> {
           if (dialogButton == guardarButtonType) {
               userSelected.setNombreUsuario(nombreUsuario.getText());
               userSelected.setPermiso(permiso.getText());

               return userSelected;
           }
           return null;
       });
       Optional<Usuario> resultado = dialog.showAndWait();

       resultado.ifPresent(usuario -> {
           // si el usuario no es nulo
           // Mostrar un mensaje al usuario
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Actualización Exitosa");
           alert.setHeaderText(null);
           alert.setContentText("Los cambios en el usuario han sido guardados correctamente.");
           actualizarUsuario(userSelected);
           alert.showAndWait();
       });

   }

   @FXML
   private  void eliminarUsuario(){
        Usuario userSelected=table.getSelectionModel().getSelectedItem();
        if(userSelected==null){
            FuncionesMenu.mostrarMensajeAlerta("Selección Requerida", "Debe seleccionar un usuario de la tabla");
            return;
        }
        FuncionesMenu.eliminarEntidad(userSelected,usuario -> {
            userRepository.delete(usuario);
            refrescarTablaUsuarios();
            FuncionesMenu.mostrarMensajeAlerta("Eliminación Existosa.","El usuario se ha eliminado correctamente.");
        });
   }

    private void actualizarUsuario(Usuario userSelected) {
        // Guardar el usuario actualizado en la base de datos
        userRepository.save(userSelected);
        refrescarTablaUsuarios();
    }

    private void refrescarTablaUsuarios() {
        // Recargar la lista de usuarios desde la base de datos y actualizar la TableView
        List<Usuario> usuarios = userRepository.findAll();
        table.setItems(FXCollections.observableArrayList(usuarios));
    }


}
