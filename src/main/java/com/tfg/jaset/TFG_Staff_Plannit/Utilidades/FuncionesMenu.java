package com.tfg.jaset.TFG_Staff_Plannit.Utilidades;

import com.sun.javafx.scene.traversal.Direction;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;


public class FuncionesMenu {

    @Getter
    @Setter
    private static Object objetoSeleccionado;
/*
    public static Object getObjetoSeleccionado() {
        return objetoSeleccionado;
    }

    public static void setObjetoSeleccionado(Object objeto) {
        objetoSeleccionado = objeto;
    } */

    public static void cambiarVentana( ActionEvent event, String rutaFXML, String tituloVentana, boolean forma) throws IOException {
        FXMLLoader fxml=new FXMLLoader(FuncionesMenu.class.getResource(rutaFXML));//cargo el fxml de la ventana nueva
        // Obteniendo el contexto de Spring desde la clase de utilidad
        fxml.setControllerFactory(SpringContextUtil.getContext()::getBean);

        // Crear una nueva escena con la vista cargada
        Scene scene=new Scene(fxml.load());

        // Obtengo la ventana actual y la sustituyo con la nueva ventana
        Stage stageActual= (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageActual.setScene(scene);
        stageActual.setMaximized(forma);
        stageActual.setResizable(forma);
        stageActual.setTitle(tituloVentana);
        Image logo=new Image(FuncionesMenu.class.getResourceAsStream("/images/staffPlannit.jpg"));
        stageActual.getIcons().add(logo);
        stageActual.show();
    }

    // MÉTODO PARA PONER UNA VISTA DENTRO DEL PANE DE LA VENTANA PRINCIPAL
    public static void mostrarVista(ApplicationContext springContext, StackPane contenedor, String rutaFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(FuncionesMenu.class.getResource(rutaFXML));
        loader.setControllerFactory(springContext::getBean);
        Node vista = loader.load();
        contenedor.getChildren().setAll(vista);
    }

    // MÉTODO PARA CARGAR LOS DATOS EN LAS TABLAS MI UI
    public static <T> void refrescarDatosTabla(TableView<T> tabla, JpaRepository<T,?> repository){
        List<T> registros=repository.findAll();
        tabla.setItems(FXCollections.observableArrayList(registros));
    }
    //METODO PARA VALIDAR QUE TODOS LOS CAMPOS DE UNA ESCENA, ESTEN RELLENOS
    public static  boolean camposCompletos(TextInputControl ... campos){
        for (TextInputControl ti:campos){
            if (ti.getText()==null || ti.getText().trim().isEmpty())
                return  false;
        }
        return  true;
    }
    //MÉTODO PARA MOSTRAR DIALOG CON MENSAJES
    public static void mostrarMensajeAlerta(String title, String msj){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msj);
        alert.showAndWait();
    }

    //MÉTODO PARA CONFIRMAR UNA ACCIÓN
    public static boolean mostrarDialogConfirmacion(String title, String msj){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msj);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    //MÉTODO GENÉRICO PARA ELIMINAR CUALQUIER OBJETO DE LA BBDD
    public static  <T> void eliminarEntidad(T entidade, Consumer<T> accionElimina){
        if(mostrarDialogConfirmacion("Confirmación","¿Está seguro de que desea eliminar este registro?")){
            accionElimina.accept(entidade);
        }
    }

    // MÉTODO PARA TABULAR, ES DECIR, CAMIAR DE UN CAMPO A OTRO
    public static void tabular(Parent parent) {
        // Ordena los nodos por su posición en el eje Y para seguir el flujo visual top-down
        List<Node> focusableNodes = parent.getChildrenUnmodifiable().stream()
                .filter(Node::isFocusTraversable)
                .sorted(Comparator.comparingDouble(Node::getLayoutY))
                .collect(Collectors.toList());

        parent.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node currentNode = (Node) event.getTarget();
                int currentIndex = focusableNodes.indexOf(currentNode);
                if (currentIndex != -1) {
                    if (currentNode instanceof Button) {
                        ((Button) currentNode).fire();  // Ejecuta la acción del botón
                    } else {
                        // Calcula el índice del siguiente nodo enfocable y solicita el foco
                        int nextIndex = (currentIndex + 1) % focusableNodes.size();
                        focusableNodes.get(nextIndex).requestFocus();
                    }
                    event.consume();
                }
            }
        });
    }

    //MÉTODO PARA ELIMINAR DESACTIVAR O NO LOS BOTONES SEGÚN EL PERMISO DEL USUARIO
    public static  void desactivarByPermiso(Button ... botones){
        if(UsuarioUtils.getUsuarioActual().getPermiso().equals("USER")){
            for(Button b:botones){
                b.setDisable(true);
            }
        }
    }

    //MÉTODO PARA ACTUALIZAR LOS CAMBIOS HECHOS SOBRE UN OBJETO
    public static <T> void actualizarObjeto(JpaRepository<T,?> repository, T entidad){
         repository.save(entidad);
    }






}
