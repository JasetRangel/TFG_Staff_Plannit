package com.tfg.jaset.TFG_Staff_Plannit.Utilidades;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import javafx.scene.input.KeyEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;


public class FuncionesMenu {

    @Getter
    @Setter
    private static Object objetoSeleccionado;


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
        Image logo=new Image(Objects.requireNonNull(FuncionesMenu.class.getResourceAsStream("/images/staffPlannit.jpg")));
        stageActual.getIcons().add(logo);
        stageActual.show();
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
    public static void tabular(Parent... parents) {
        List<Node> focusableNodes = new ArrayList<>();

        // Agregamos todos los nodos enfocables de todos los contenedores.
        for (Parent parent : parents) {
            focusableNodes.addAll(parent.getChildrenUnmodifiable().stream()
                    .filter(Node::isFocusTraversable)
                    .toList());
        }

        // Ordenamos todos los nodos por su posición en el eje Y para una navegación coherente.
        focusableNodes.sort(Comparator.comparingDouble(Node::getLayoutY));

        for (Parent parent : parents) {
            parent.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                Node currentNode = (Node) event.getTarget();
                int currentIndex = focusableNodes.indexOf(currentNode);
                int nextIndex = currentIndex;

                if (currentIndex != -1) {
                    if (event.getCode() == KeyCode.ENTER && currentNode instanceof Button) {
                        ((Button) currentNode).fire();  // Ejecuta la acción del botón
                        event.consume();
                    } else if (event.getCode() == KeyCode.DOWN) {
                        nextIndex = (currentIndex + 1) % focusableNodes.size(); // Siguiente nodo
                    } else if (event.getCode() == KeyCode.UP) {
                        nextIndex = (currentIndex - 1 + focusableNodes.size()) % focusableNodes.size(); // Nodo anterior
                    } else if (event.getCode() == KeyCode.ENTER) {
                        nextIndex = (currentIndex + 1) % focusableNodes.size(); // Siguiente nodo en Enter si no es un botón
                    }

                    if (nextIndex != currentIndex) {
                        focusableNodes.get(nextIndex).requestFocus();
                        event.consume();
                    }
                }
            });
        }
    }
    // MÉTODO PARA NAVEGAR DENTRO DE LA TABLA
    public static <T> void configurarTabla(TableView<T> tabla, Consumer<T> accion) {
        tabla.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    T rowData = row.getItem();
                    accion.accept(rowData);
                }
            });
            return row;
        });

        tabla.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                T selected = tabla.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    accion.accept(selected);
                }
            }
        });
    }


    public static void configurarEstiloBotones(Button... botones) {
        for (Button boton : botones) {
            boton.setCursor(Cursor.HAND);
            // Añadir listeners para el enfoque y el hover del mouse
            boton.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused) {
                    boton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                } else if (!boton.isHover()) {
                    boton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                }
            });

            boton.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered) {
                    boton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                } else if (!boton.isFocused()) {
                    boton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                }
            });
        }
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

    public static void botonMano(Button... botones) {
        for (Button boton : botones) {
            boton.setCursor(Cursor.HAND);
        }

    }

    public static void limpiarObjetoSeleccionado() {
        setObjetoSeleccionado(null);
    }


}
