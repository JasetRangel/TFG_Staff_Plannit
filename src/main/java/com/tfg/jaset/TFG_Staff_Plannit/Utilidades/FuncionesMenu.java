package com.tfg.jaset.TFG_Staff_Plannit.Utilidades;

import com.sun.javafx.scene.traversal.Direction;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;


public class FuncionesMenu {
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




}
