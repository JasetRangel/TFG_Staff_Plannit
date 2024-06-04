package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
public class CrudEmpleadosEventos implements Initializable {

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

    // Declaración de la variable eventoEmpleado
    private EventosEmpleado eventoEmpleado;

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

                LocalDate fecha = txtFechaDia.getValue();
                LocalTime horaEntrada = LocalTime.parse(txtHoraEntrada.getText());
                LocalTime horaSalida = LocalTime.parse(txtHoraSalida.getText());

                // Verificar que no haya conflictos de horarios con otros eventos
                List<EventosEmpleado> eventosConflicto = eventoEmpleadoRepository.findConflictingEvents(empleado.getDni(), fecha, horaEntrada, horaSalida);
                if (!eventosConflicto.isEmpty()) {
                    EventosEmpleado eventoConflicto = eventosConflicto.get(0);
                    Cliente clienteConflicto = eventoConflicto.getEvento().getCliente();
                    throw new RuntimeException("El empleado ya está asignado a otro evento en el mismo intervalo de tiempo. Detalles del conflicto: " +
                            "\nCliente: " + clienteConflicto.getNombre() +
                            "\nFecha: " + eventoConflicto.getId().getFecha() +
                            "\nDirección: " + eventoConflicto.getEvento().getDireccionEvento());
                }

                // Crear el ID embebido
                EventosEmpleadoId eventoEmpleadoId = new EventosEmpleadoId();
                eventoEmpleadoId.setEmpleadoDni(empleado.getDni());
                eventoEmpleadoId.setEventoId(evento.getId());
                eventoEmpleadoId.setFecha(fecha);
                eventoEmpleadoId.setHoraEntrada(horaEntrada);

                // Crear la entidad EventosEmpleado
                EventosEmpleado eventoEmpleado = new EventosEmpleado();
                eventoEmpleado.setId(eventoEmpleadoId);
                eventoEmpleado.setEmpleadoDni(empleado);
                eventoEmpleado.setHoraSalida(horaSalida);
                eventoEmpleado.setFuncion(funcion);
                eventoEmpleado.setEvento(evento);

                // Guardar la entidad
                eventoEmpleadoRepository.save(eventoEmpleado);

                FuncionesMenu.mostrarMensajeAlerta("Éxito", "Empleado agregado al evento con éxito");
                limpiarCampos(); // Limpiar los campos si la inserción es exitosa
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

    public void cargarDatosEmpleado(EventoEmpleadosDTO empleadoEvento) {
        Empleado empleado = empleadoRepository.findByNombreAndApellidos(empleadoEvento.getNombreEmpleado(), empleadoEvento.getApellidosEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Llenar los campos con los datos del empleado
        txtEmpleado.setText(empleado.getNombre());
        txtApellidos.setText(empleado.getApellidos());
        txtFechaDia.setValue(empleadoEvento.getFecha());
        txtHoraEntrada.setText(empleadoEvento.getHoraEntrada().toString());
        txtHoraSalida.setText(empleadoEvento.getHoraSalida().toString());
        comboFunciones.setValue(empleadoEvento.getDescripcionFuncion());

        // Buscar la relación EventoEmpleado para la modificación
        EventosEmpleadoId eventoEmpleadoId = new EventosEmpleadoId();

        eventoEmpleadoId.setEmpleadoDni(empleado.getDni());
        eventoEmpleadoId.setEventoId(Integer.parseInt(txtId.getText()));
        eventoEmpleadoId.setFecha(empleadoEvento.getFecha());
        // Parsear la hora con segundos y milisegundos opcionales
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm[:ss][.SSSSSS]");
        eventoEmpleadoId.setHoraEntrada(LocalTime.parse(txtHoraEntrada.getText(), formatter));

        System.out.println("Evento id "+eventoEmpleadoId.getEventoId());
        System.out.println("Evento fecha "+eventoEmpleadoId.getFecha());
        System.out.println("Empleado Hora Entrada "+eventoEmpleadoId.getHoraEntrada());
        System.out.println("Empleado DNI "+eventoEmpleadoId.getEmpleadoDni());

        // Intentar encontrar la relación Evento-Empleado
        eventoEmpleado = eventoEmpleadoRepository.findById(eventoEmpleadoId)
                .orElseThrow(() -> new RuntimeException("Relación Evento-Empleado no encontrada"));

    }

    public boolean modificarEmpleado() {
        boolean modificado=false;
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

                // Crear el nuevo ID embebido con la nueva hora de entrada
                EventosEmpleadoId nuevoEventoEmpleadoId = new EventosEmpleadoId();
                nuevoEventoEmpleadoId.setEmpleadoDni(empleado.getDni());
                nuevoEventoEmpleadoId.setEventoId(Integer.parseInt(txtId.getText()));
                nuevoEventoEmpleadoId.setFecha(txtFechaDia.getValue());
                nuevoEventoEmpleadoId.setHoraEntrada(LocalTime.parse(txtHoraEntrada.getText()));

                // Crear la nueva entidad EventosEmpleado
                EventosEmpleado nuevoEventoEmpleado = new EventosEmpleado();
                nuevoEventoEmpleado.setId(nuevoEventoEmpleadoId);
                nuevoEventoEmpleado.setEmpleadoDni(empleado);
                nuevoEventoEmpleado.setHoraSalida(LocalTime.parse(txtHoraSalida.getText()));
                nuevoEventoEmpleado.setFuncion(funcion);
                nuevoEventoEmpleado.setEvento(eventoEmpleado.getEvento());
                if(eventoEmpleado.equals(nuevoEventoEmpleado)){
                    // Eliminar el registro antiguo
                    eventoEmpleadoRepository.delete(eventoEmpleado);
                    // Guardar el nuevo registro
                    eventoEmpleadoRepository.save(nuevoEventoEmpleado);
                    FuncionesMenu.mostrarMensajeAlerta("Éxito", "Empleado modificado con éxito");
                    modificado=true;
                }else{
                    FuncionesMenu.mostrarMensajeAlerta("Cambios Requeridos.", "No se han realizado cambios");
                }

            } else {
                FuncionesMenu.mostrarMensajeAlerta("Error", "Debe completar todos los campos");
            }
        } catch (Exception e) {
            FuncionesMenu.mostrarMensajeAlerta("Error", "No se pudo modificar el empleado: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return modificado;
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
