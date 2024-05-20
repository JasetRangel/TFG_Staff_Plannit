package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.Models.*;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventoEmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventosRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.FuncionesRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
public class EmpleadosEventos implements Initializable {

    @FXML
    private ComboBox<String> comboFunciones;

    @FXML
    private DialogPane padre;

    @FXML
    private HBox padre1;

    @FXML
    private VBox padreColun1;

    @FXML
    private VBox padreColun2;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtEmpleado;

    @FXML
    private DatePicker txtFechaDia;

    @FXML
    private DatePicker txtFechaFin;

    @FXML
    private DatePicker txtFechaInicio;

    @FXML
    private TextField txtHoraEntrada;

    @FXML
    private TextField txtHoraSalida;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombreCliente;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private FuncionesRepository funcionRepository;

    @Autowired
    private EventoEmpleadoRepository eventoEmpleadoRepository;

    @Autowired
    private EventosRepository eventosRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtHoraEntrada.setTextFormatter(formatoHora());
        txtHoraSalida.setTextFormatter(formatoHora());

        // ASIGNO AL COMBO BOX LAS OPCIONES DE FUNCIONES QUE HAY EN MI BBDD
        List<String> funciones = funcionRepository.findAll().stream().map(Funciones::getDescripcion).collect(Collectors.toList());
        comboFunciones.setItems(FXCollections.observableArrayList(funciones));
    }

    public void agregarEmpleado() {
        try {
            // Validar los campos
            if (validarCampos()) {
                String nombreEmpleado = txtEmpleado.getText();
                String apellidosEmpleado = txtApellidos.getText();
                Empleado empleado = empleadoRepository.findByNombreAndApellidos(nombreEmpleado, apellidosEmpleado)
                        .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

                String descripcionFuncion = comboFunciones.getValue();
                Funciones funcion = funcionRepository.findByDescripcion(descripcionFuncion)
                        .orElseThrow(() -> new RuntimeException("Función no encontrada"));

                Evento evento = eventosRepository.findById(Integer.parseInt(txtId.getText()))
                        .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

                // Crear el ID embebido
                EventosEmpleadoId eventoEmpleadoId = new EventosEmpleadoId();
                eventoEmpleadoId.setEmpleadoDni(empleado.getDni());
                eventoEmpleadoId.setEventoId(evento.getId());

                // Crear la entidad EventosEmpleado
                EventosEmpleado eventoEmpleado = new EventosEmpleado();
                eventoEmpleado.setId(eventoEmpleadoId);
                eventoEmpleado.setEmpleadoDni(empleado);
                eventoEmpleado.setEvento(evento);
                eventoEmpleado.setFecha(txtFechaDia.getValue());
                eventoEmpleado.setHoraEntrada(LocalTime.parse(txtHoraEntrada.getText()));
                eventoEmpleado.setHoraSalida(LocalTime.parse(txtHoraSalida.getText()));
                eventoEmpleado.setFuncion(funcion);

                // Guardar la entidad
                eventoEmpleadoRepository.save(eventoEmpleado);

                FuncionesMenu.mostrarMensajeAlerta("Éxito", "Empleado agregado al evento con éxito");
            } else {
                FuncionesMenu.mostrarMensajeAlerta("Error", "Debe completar todos los campos");
            }
        } catch (Exception e) {
            FuncionesMenu.mostrarMensajeAlerta("Error", "No se pudo agregar el empleado al evento: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void limpiarCampos(){
        txtEmpleado.setText("");
        txtApellidos.setText("");
        txtFechaDia.setValue(txtFechaDia.getValue());
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
    }

    public void setEventoInfo(String id, String nombreCliente, String direccion, LocalDate fechaInicio, LocalDate fechaFin) {
        txtId.setText(id);
        txtNombreCliente.setText(nombreCliente);
        txtDireccion.setText(direccion);
        txtFechaInicio.setValue(fechaInicio);
        txtFechaFin.setValue(fechaFin);

        // Restringir las fechas seleccionables en el DatePicker de fecha de trabajo
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(fechaInicio) || item.isAfter(fechaFin)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Color rosa para las fechas deshabilitadas
                }
            }
        };
        txtFechaDia.setDayCellFactory(dayCellFactory);

        // Establecer la fecha del DatePicker al mes y año del inicio del evento
        txtFechaDia.setValue(fechaInicio);

        // Escucha el evento de mostrar el DatePicker y actualizar el mes/año
        txtFechaDia.setOnShowing(event -> {
            txtFechaDia.setValue(fechaInicio);
        });
    }

    private TextFormatter<String> formatoHora() {
        UnaryOperator<TextFormatter.Change> formatter = change -> {
            String text = change.getControlNewText();

            // Permitir siempre la eliminación de caracteres
            if (change.isDeleted()) {
                return change;
            }

            // Validar formato de hora completo HH:mm
            if (text.matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                return change;
            }

            // Agregar dos puntos automáticamente después de dos dígitos
            if (text.matches("[0-9]{2}")) {
                change.setText(change.getText() + ":");
                change.setCaretPosition(change.getCaretPosition() + 1);
                change.setAnchor(change.getAnchor() + 1);
                return change;
            }

            // Validar entrada parcial de hora HH:
            if (text.matches("([01][0-9]|2[0-3]):")) {
                return change;
            }

            // Validar entrada parcial de hora HH:mm
            if (text.matches("([01][0-9]|2[0-3]):[0-5]")) {
                return change;
            }

            // Permitir ingreso de dígitos
            if (text.matches("[0-9]*")) {
                return change;
            }

            // Rechazar cualquier otra entrada
            return null;
        };

        return new TextFormatter<>(formatter);
    }

    private boolean validarCampos() {
        return !txtEmpleado.getText().isEmpty() &&
                txtFechaDia.getValue() != null &&
                !txtHoraEntrada.getText().isEmpty() &&
                !txtHoraSalida.getText().isEmpty() &&
                comboFunciones.getValue() != null;
    }

}
