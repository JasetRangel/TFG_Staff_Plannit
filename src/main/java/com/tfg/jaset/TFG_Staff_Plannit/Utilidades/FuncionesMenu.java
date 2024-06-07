package com.tfg.jaset.TFG_Staff_Plannit.Utilidades;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.function.UnaryOperator;

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
////////////////////---------- MÉTODOS PARA VALIDAR LA ENTRADA DE TEXTO EN LOS CAMPOS----------------//////////////////

    public static void validarTelefono(TextField textField, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));

        // Añadir listener para verificar el tamaño del número al perder el foco
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Al perder el foco
                String text = textField.getText();
                if(!textField.getText().isEmpty()){
                    if (text.length() < maxLength) {
                        mostrarMensajeAlerta("Número de teléfono inválido", "El número de teléfono debe contener exactamente " + maxLength + " dígitos.");
                        textField.requestFocus();
                    }
                }
            }
        });
    }

    // Método para restringir el texto en un TextField a solo letras
    public static void restringirSoloLetras(TextField textField, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z]*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    // Método para restringir el texto en un TextField a letras y números
    public static void restringirLetrasNumeros(TextField textField, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z0-9]*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    // Método para restringir el texto en un TextArea a letras, números, signos de puntuación y permitir saltos de línea
    public static void restringirLetrasNumerosSignos(TextArea textArea, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z0-9\\p{Punct}\\n]*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textArea.setTextFormatter(new TextFormatter<>(filter));
    }

    // Método para restringir el texto en un TextField a un formato de email
    public static void restringirEmail(TextField textField, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z0-9_@.]*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));

        // Añadir listener para verificar el formato de correo electrónico al perder el foco
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Al perder el foco
                String text = textField.getText();
                int atIndex = text.indexOf("@");
                if(!textField.getText().isEmpty()){
                    if (atIndex == -1 || atIndex >= text.length() - 3) {
                        mostrarMensajeAlerta("Formato de correo inválido", "El correo electrónico debe contener un '@' seguido de al menos tres caracteres.");
                        textField.requestFocus();
                    }
                }
            }
        });
    }

    // Método para hacer que tod0 el texto en los TextFields se convierta a mayúsculas
    public static void convertirTextoAMayusculas(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.equals(newValue.toUpperCase())) {
                    textField.setText(newValue.toUpperCase());
                }
            });
        }
    }

    // Método para validar la edad en un TextField
    public static void validarEdad(TextField textField, int maxLength) {
        // Configurar TextFormatter para limitar la entrada a números y restringir la longitud
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));

        // Añadir listener para verificar la validez de la edad al perder el foco
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Al perder el foco
                String text = textField.getText();
                if (!text.isEmpty()) {
                    int edad = Integer.parseInt(text);
                    if(!textField.getText().isEmpty()){
                        if (edad <= 16 || edad > 120) {
                            mostrarMensajeAlerta("Edad inválida", "La edad debe estar entre 0 y 120 años.");
                            textField.requestFocus();
                        }
                    }
                }
            }
        });
    }

    /////////////-------------- MÉTODOS PARA VALIDAR LOS DOCUMENTOS DE IDENTIDAD ----------//////////////////////

    public static void validarIdentificacion(TextField textField, int maxLength) {
        // Configurar TextFormatter para limitar el número máximo de caracteres y permitir solo caracteres válidos
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText().toUpperCase();
            if (newText.matches("[A-Z0-9]*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));

        // Añadir listener para verificar la validez de la identificación al perder el foco
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Al perder el foco
                String text = textField.getText();
                if(!textField.getText().isEmpty()){
                    if (!validarIdentificacion(text) || text.length() < maxLength) {
                        mostrarMensajeAlerta("Identificación inválida", "El DNI, NIE o CIF ingresado no es válido o no tiene la longitud correcta.");
                            textField.requestFocus();
                    }
                }
            }
        });
    }

    private static boolean validarIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.isEmpty()) {
            return false;
        }

        identificacion = identificacion.toUpperCase();

        if (validarDNI(identificacion) || validarNIE(identificacion) || validarCIF(identificacion)) {
            return true;
        }

        return false;
    }

    private static boolean validarDNI(String dni) {
        if (!dni.matches("\\d{8}[A-Z]")) {
            return false;
        }

        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int numero = Integer.parseInt(dni.substring(0, 8));
        char letra = dni.charAt(8);

        return letras.charAt(numero % 23) == letra;
    }

    private static boolean validarNIE(String nie) {
        if (!nie.matches("[XYZ]\\d{7}[A-Z]")) {
            return false;
        }

        // Reemplaza la letra inicial por su equivalente numérico
        char inicial = nie.charAt(0);
        String nieNumerico = "";
        switch (inicial) {
            case 'X':
                nieNumerico = "0" + nie.substring(1);
                break;
            case 'Y':
                nieNumerico = "1" + nie.substring(1);
                break;
            case 'Z':
                nieNumerico = "2" + nie.substring(1);
                break;
        }

        return validarDNI(nieNumerico);
    }

    private static boolean validarCIF(String cif) {
        if (!cif.matches("[A-HJ-NP-SUVW]\\d{7}[0-9A-J]")) {
            return false;
        }

        char letraInicial = cif.charAt(0);
        String digitos = cif.substring(1, 8);
        char digitoControl = cif.charAt(8);

        int sumaPar = 0;
        int sumaImpar = 0;

        for (int i = 0; i < digitos.length(); i++) {
            int digito = Integer.parseInt(String.valueOf(digitos.charAt(i)));

            if (i % 2 == 0) {
                int doble = digito * 2;
                sumaImpar += doble / 10 + doble % 10;
            } else {
                sumaPar += digito;
            }
        }

        int sumaTotal = sumaPar + sumaImpar;
        int digitoControlCalculado = (10 - (sumaTotal % 10)) % 10;

        if (Character.isDigit(digitoControl)) {
            return digitoControl == '0' + digitoControlCalculado;
        } else {
            String letrasControl = "JABCDEFGHI";
            return digitoControl == letrasControl.charAt(digitoControlCalculado);
        }
    }

//////////////-------- VALIDAR LA CUENTA BANCARIA -----------//////////////////

// Método para validar número de cuenta bancaria (CCC e IBAN)
private static boolean validarNumeroCuenta(String cuenta) {
    if (cuenta == null || cuenta.isEmpty()) {
        return false;
    }

    cuenta = cuenta.replaceAll("\\s+", "").toUpperCase();

    if (cuenta.matches("\\d{20}")) { // CCC
        return validarCCC(cuenta);
    } else if (cuenta.matches("ES\\d{22}")) { // IBAN
        return validarIBAN(cuenta);
    }

    return false;
}

    private static boolean validarCCC(String ccc) {
        if (ccc.length() != 20) {
            return false;
        }

        String entidad = ccc.substring(0, 4);
        String oficina = ccc.substring(4, 8);
        String dc = ccc.substring(8, 10);
        String cuenta = ccc.substring(10, 20);

        String dcCalculado = calcularDigitosControl(entidad, oficina, cuenta);

        return dc.equals(dcCalculado);
    }

    private static String calcularDigitosControl(String entidad, String oficina, String cuenta) {
        String ceros = "00";
        String dc1 = calcularDigitoControl(ceros + entidad + oficina);
        String dc2 = calcularDigitoControl(cuenta);

        return dc1 + dc2;
    }

    private static String calcularDigitoControl(String parte) {
        int[] factores = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};
        int suma = 0;

        for (int i = 0; i < parte.length(); i++) {
            suma += Character.getNumericValue(parte.charAt(i)) * factores[i];
        }

        int resto = 11 - (suma % 11);

        if (resto == 10) {
            return "1";
        } else if (resto == 11) {
            return "0";
        } else {
            return String.valueOf(resto);
        }
    }

    private static boolean validarIBAN(String iban) {
        if (iban.length() != 24) {
            return false;
        }

        // Mover los 4 primeros caracteres al final del string
        String reformattedIban = iban.substring(4) + iban.substring(0, 4);

        // Convertir las letras en números (A=10, B=11, ..., Z=35)
        StringBuilder numericIban = new StringBuilder();
        for (char c : reformattedIban.toCharArray()) {
            int numericValue;
            if (Character.isDigit(c)) {
                numericValue = Character.getNumericValue(c);
            } else {
                numericValue = c - 'A' + 10;
            }
            numericIban.append(numericValue);
        }

        // Validar el IBAN usando aritmética modular
        return new java.math.BigInteger(numericIban.toString()).mod(java.math.BigInteger.valueOf(97)).intValue() == 1;
    }

    // Método para validar número de cuenta en un TextField
    public static void validarNumeroCuenta(TextField textField, int maxLength) {
        // Configurar TextFormatter para limitar el número máximo de caracteres y permitir solo caracteres válidos
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText().toUpperCase();
            if (newText.matches("[A-Z0-9]*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));

        // Añadir listener para verificar la validez de la cuenta al perder el foco
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Al perder el foco
                String text = textField.getText();
                if(!textField.getText().isEmpty()){
                    if (!validarNumeroCuenta(text) || text.length() < maxLength) {
                        mostrarMensajeAlerta("Número de cuenta inválido", "El número de cuenta bancaria ingresado no es válido o no tiene la longitud correcta.");
                            textField.requestFocus();
                    }
                }
            }
        });
    }

}


