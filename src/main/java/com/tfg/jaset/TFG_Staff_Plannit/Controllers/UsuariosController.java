package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import com.tfg.jaset.TFG_Staff_Plannit.CrudControllers.ModifyUser;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.UsuarioRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.SpringContextUtil;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;

import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
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
    private Button btnNewUser;

    @FXML
    private Button btnCancelar;

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
        columDNI.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        columPermiso.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        columUser.prefWidthProperty().bind(table.widthProperty().multiply(0.4));

        columDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columUser.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        columPermiso.setCellValueFactory(new PropertyValueFactory<>("permiso"));

        table.setRowFactory(tv -> {
            TableRow<Usuario> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Usuario rowData = table.getSelectionModel().getSelectedItem();
                    Usuario usuarioActual = UsuarioUtils.getUsuarioActual();
                    if(usuarioActual.getDni().equals(rowData.getDni())){
                        mostrarDialogUsuario(rowData);
                    }else {
                        FuncionesMenu.mostrarMensajeAlerta("Acción Prohibida.","No se puede modificar este usuario ya que " +
                                " no pertenece a usted.");
                    }

                }
            });
            return row;
        });

        pane.setOnMouseClicked(event -> {
            if(!isDescendant(event.getTarget(),table)){
                table.getSelectionModel().clearSelection();
            }
        });

        FuncionesMenu.botonMano(btnBuscar, btnEliminar, btnNewUser);

        FuncionesMenu.desactivarByPermiso(btnEliminar);

        FuncionesMenu.refrescarDatosTabla(table, userRepository);

        columDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columUser.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        columPermiso.setCellValueFactory(new PropertyValueFactory<>("permiso"));

        pane.setOnMouseClicked(event -> {
            if(!isDescendant(event.getTarget(),table)){
                table.getSelectionModel().clearSelection();
            }
        });

        btnNewUser.setOnAction(event -> {
            mostrarDialogUsuario(null);
        });

        btnEliminar.setOnAction(event->{
            eliminarUsuario();
        });
    }

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

    private void mostrarDialogUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/fx/modificarUsuario.fxml"));
            loader.setControllerFactory(SpringContextUtil.getContext()::getBean);
            DialogPane dialogPane = loader.load();

            ModifyUser modifyUserController = loader.getController();
            modifyUserController.setUsuario(usuario);
            modifyUserController.cargarPermisos();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(usuario == null ? "Agregar Usuario" : "Modificar Usuario");

            ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cerrarButtonType = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, cerrarButtonType);

            Button guardarButton = (Button) dialog.getDialogPane().lookupButton(guardarButtonType);
            guardarButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                Usuario updatedUser = modifyUserController.actualizarUsuario();
                if (updatedUser != null) {
                    userRepository.save(updatedUser);
                    FuncionesMenu.refrescarDatosTabla(table, userRepository);
                    FuncionesMenu.mostrarMensajeAlerta("Actualización Exitosa", "Los cambios en el usuario han sido guardados correctamente.");
                }else {
                    event.consume();
                }

            });

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            FuncionesMenu.mostrarMensajeAlerta("Error", "No se pudo cargar el diálogo de modificación.");
        }
    }

    @FXML
    private void eliminarUsuario(){
        Usuario userSelected = table.getSelectionModel().getSelectedItem();
        if(userSelected == null){
            FuncionesMenu.mostrarMensajeAlerta("Selección Requerida", "Debe seleccionar un usuario de la tabla");
            return;
        }

        Usuario userActual = UsuarioUtils.getUsuarioActual();

        if(userActual != null && userSelected.getDni().equals(userActual.getDni())){
            FuncionesMenu.mostrarMensajeAlerta("Acción Prohibida", "No puede eliminar el usuario con el que se ha iniciado sesión");
            return;
        }
        FuncionesMenu.eliminarEntidad(userSelected, usuario -> {
            userRepository.delete(usuario);
            FuncionesMenu.refrescarDatosTabla(table, userRepository);
            FuncionesMenu.mostrarMensajeAlerta("Eliminación Exitosa", "El usuario se ha eliminado correctamente.");
        });
    }
    @FXML
    private void cancelar(){
        FuncionesMenu.refrescarDatosTabla(table, userRepository);
    }
}
