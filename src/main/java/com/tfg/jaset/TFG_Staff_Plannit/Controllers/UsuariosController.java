package com.tfg.jaset.TFG_Staff_Plannit.Controllers;


import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.UsuarioRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
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

        btnModificar.setOnAction(event -> {
            if(table.getSelectionModel().getSelectedItem()==null){
                mostrarMensajeAlerta("Selleción Requerida","Debe seleccionar un campo de la tabla");
            }
            else
                mostrarDialogModificarUsuario();
        });

        btnNewUser.setOnAction(event -> {
            mostrarDialogAgregarUser();
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
    private void mostrarDialogAgregarUser(){
        Usuario userSelected=table.getSelectionModel().getSelectedItem();
        Dialog<Usuario>dialog=new Dialog<>();
        dialog.setTitle("Nuevo usuario");
        dialog.setHeaderText("Agregar un nuevo Usuario a la lista de usuarios");
        //configuro los botones
        ButtonType guardar=new ButtonType("Guardar",ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardar,ButtonType.CANCEL);

        //creo el formulario para el nuevo Usuario
        GridPane grid=new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField dniUser=new TextField();
        dniUser.setPromptText("DNI Usuario");


        TextField nombreUsuario=new TextField();
        nombreUsuario.setPromptText("Numbre de usuario");


        TextField password=new TextField();
        password.setPromptText("Contraseña");


        TextField permiso=new TextField();
        permiso.setPromptText("Permiso");


        grid.add( new Label("DNI Usuario: "),0,0);
        grid.add(dniUser,1,0);
        grid.add(new Label("Nombre Usuario: "),0,1);
        grid.add(nombreUsuario,1,1);
        grid.add(new Label("Contraseña: "),0,2);
        grid.add(password,1,2);
        grid.add(new Label("Permiso: "),0,3);
        grid.add(permiso,1,3);

        dialog.getDialogPane().setContent(grid);

        //configuro el boton de guardado
        Node guardarButton=dialog.getDialogPane().lookupButton(guardar);
        guardarButton.addEventFilter(ActionEvent.ACTION, event ->{
            if(!FuncionesMenu.camposCompletos(dniUser,nombreUsuario,password,permiso)) {
                event.consume();// evita que la ventana se cierre, no enteresa que se cierre la ventana ya que
                // no se ha podido insertar el usuario
                mostrarMensajeAlerta("Campo Incompleto", "Debe rellenar todos los campos");
            }else /*if(userRepository.existsByEmpleadoDni(dniUser.getText())) */{

                Optional<Empleado>empleadoExiste=empleadoRepository.findByDni(dniUser.getText());
                if(empleadoExiste.isPresent()){
                    System.out.println("El empleado ya existe");
                    Usuario nuevoUsuario = new Usuario();

                    nuevoUsuario.setEmpleado(empleadoExiste.get());// establezco la relacion con el empleado existente
                    //nuevoUsuario.setDni(empleadoExiste.get().getDni());
                    nuevoUsuario.setNombreUsuario(nombreUsuario.getText());
                    nuevoUsuario.setContrasenia(password.getText());
                    nuevoUsuario.setPermiso(permiso.getText());
                    userRepository.save(nuevoUsuario);//guardo al nuevo usuario
                    mostrarMensajeAlerta("Ingreso Correcto", "Usuario agregado correctamente");
                    refrescarTablaUsuarios();
                    dialog.close();
                }else{
                    event.consume(); // Evita que la ventana se cierre
                    mostrarMensajeAlerta("Error", "Empleado con DNI dado no existe");
                }

            }
        });

        Optional<Usuario>resultado= dialog.showAndWait();

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
               // Aquí puedes llamar a tu método para actualizar la base de datos con el usuario modificado
               actualizarUsuario(userSelected);
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

           alert.showAndWait();
       });

   }

    private void actualizarUsuario(Usuario userSelected) {
        // Guardar el usuario actualizado en la base de datos
        userSelected= userRepository.save(userSelected);

        // Opcionalmente, si necesitas refrescar la tabla para mostrar los datos actualizados:
        refrescarTablaUsuarios();
    }

    private void refrescarTablaUsuarios() {
        // Recargar la lista de usuarios desde la base de datos y actualizar la TableView
        List<Usuario> usuarios = userRepository.findAll();
        table.setItems(FXCollections.observableArrayList(usuarios));
    }
    public void mostrarMensajeAlerta(String title, String msj){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msj);
        alert.showAndWait();
    }

}
