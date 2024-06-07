package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.*;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.ClientesRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventoEmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventosRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.SpringContextUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class CrudEventos implements Initializable {

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnEliminarEmpleado;

    @FXML
    private Button btnAddEmpleado;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<EventoEmpleadosDTO, LocalDate> columFecha;

    @FXML
    private TableColumn<EventoEmpleadosDTO, String> columApellidos;

    @FXML
    private TableColumn<EventoEmpleadosDTO, LocalTime> columEntrada;

    @FXML
    private TableColumn<EventoEmpleadosDTO, String> columFuncion;

    @FXML
    private TableColumn<EventoEmpleadosDTO, String> columNombre;

    @FXML
    private TableColumn<EventoEmpleadosDTO, LocalTime> columSalida;

    @FXML
    private HBox padre;

    @FXML
    private HBox padreBotonones;

    @FXML
    AnchorPane modificable;

    @FXML
    private VBox padreColun1;

    @FXML
    private VBox padreColun2;

    @FXML
    private TableView<EventoEmpleadosDTO> tablaInformes;

    @FXML
    private TextField txtDetalles;

    @FXML
    private TextField txtDireccion;

    @FXML
    private DatePicker txtFechaFin;

    @FXML
    private DatePicker txtFechaInicio;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombreCliente;

    @Autowired
    private EventoEmpleadoRepository eventoEmpleadoRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private EventosRepository eventosRepository;

    private EventoDTO eventoDTO;

    private Evento evento;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columNombre.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.15));
        columApellidos.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.25));
        columEntrada.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.15));
        columSalida.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.15));
        columFuncion.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.15));
        columFecha.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.15));

        txtId.setStyle("-fx-text-fill: #070707; -fx-background-color:  rgba(77,92,92,0.53)");
        txtId.setEditable(false);
        txtId.setEditable(false);
        txtId.setFocusTraversable(false);

        padreColun2.setFocusTraversable(true);
        padreBotonones.setFocusTraversable(true);

        eventoDTO= (EventoDTO) FuncionesMenu.getObjetoSeleccionado();

        if(eventoDTO!=null){
            txtId.setText(String.valueOf(eventoDTO.getId()));
            txtNombreCliente.setText(eventoDTO.getNombreCliente());
            txtDireccion.setText(eventoDTO.getDireccion());
            txtFechaFin.setValue(eventoDTO.getFechaFin());
            txtFechaInicio.setValue(eventoDTO.getFechaInicio());
            txtDetalles.setText(String.valueOf(eventoDTO.getDetalles()));

            evento=new Evento();
            evento.setId(eventoDTO.getId());
            evento.setFechaInicio(eventoDTO.getFechaInicio());
            evento.setFechaFin(eventoDTO.getFechaFin());
            evento.setDetalles(eventoDTO.getDetalles());
            evento.setDireccionEvento(eventoDTO.getDireccion());
            cargarInformacionEnTabla();
        }

        // Detectar doble clic en la tabla
        tablaInformes.setRowFactory(tv -> {
            TableRow<EventoEmpleadosDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    EventoEmpleadosDTO rowData = row.getItem();
                    mostrarDialogoModificarEmpleado(rowData);
                }
            });
            return row;
        });

        FuncionesMenu.configurarEstiloBotones(btnAddEmpleado,btnEliminarEmpleado,btnGuardar,btnVolver);
        FuncionesMenu.tabular(padreColun1,padreColun2,padreBotonones);

        FuncionesMenu.tabular(txtNombreCliente,txtFechaFin,txtFechaInicio,txtDetalles,txtDireccion);
        FuncionesMenu.restringirLetrasNumeros(txtNombreCliente,50);
        FuncionesMenu.restringirLetrasNumeros(txtDireccion,60);
        FuncionesMenu.restringirLetrasNumeros(txtDetalles,70);
        FuncionesMenu.convertirTextoAMayusculas(txtDireccion,txtNombreCliente,txtDetalles);

    }

    @FXML
    private void actualizar_insertarEvento() {
        if (validarDatosEvento()) {
            try {
                Cliente cliente;
                if (eventoDTO == null) {
                    // Si es un evento nuevo, crea un nuevo objeto Cliente o selecciona un cliente existente de otra manera
                    cliente = clientesRepository.findByNombre(txtNombreCliente.getText())
                            .orElseThrow(() -> new RuntimeException("No se encontro el cliente"));
                } else {
                    // Si es un evento existente, obtiene el cliente del eventoDTO
                    cliente = clientesRepository.findById(eventoDTO.getIdCliente())
                            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                }

                Evento eventoModificar = new Evento();
                eventoModificar.setCliente(cliente);
                eventoModificar.setFechaInicio(txtFechaInicio.getValue());
                eventoModificar.setFechaFin(txtFechaFin.getValue());
                eventoModificar.setDireccionEvento(txtDireccion.getText());
                eventoModificar.setDetalles(txtDetalles.getText());

                if (eventoDTO == null || eventoModificar.esDiferente(evento, eventoModificar)) {
                    eventosRepository.save(eventoModificar);
                    FuncionesMenu.actualizarObjeto(eventosRepository, eventoModificar); // Actualiza usando el nuevo objeto
                    FuncionesMenu.mostrarMensajeAlerta("Evento guardado", "El evento fue guardado con éxito");
                    volver();
                } else {
                    FuncionesMenu.mostrarMensajeAlerta("Objeto sin cambios", "No se han realizado cambios.");
                }

            } catch (Exception e) {
                FuncionesMenu.mostrarMensajeAlerta("Error al guardar", "No se pudo guardar el evento: " + e.getMessage());
            }
        } else {
            FuncionesMenu.mostrarMensajeAlerta("Validación", "Verifique que todos los campos estén llenos");
        }
    }

    // Método para obtener un nuevo cliente o seleccionar uno existente
    private Cliente obtenerClienteNuevo() {
        // Aquí puedes implementar la lógica para seleccionar o crear un nuevo cliente
        // Esto es solo un ejemplo, ajústalo a tus necesidades
        List<Cliente> clientes = clientesRepository.findAll();
        if (clientes.isEmpty()) {
            throw new RuntimeException("No hay clientes disponibles. Por favor, crea un cliente primero.");
        }
        return clientes.get(0); // Selecciona el primer cliente de la lista, por ejemplo
    }

    //MODIFICAR EL METODO DE LA CLASE FUNCIONES MENU PARA QUE TAMBIEN ME ACEPTE CAMPOS DE FECHA
    private boolean validarDatosEvento() {
        return txtNombreCliente.getText() != null && !txtNombreCliente.getText().isEmpty() &&
                txtFechaInicio.getValue() != null &&
                txtFechaFin.getValue() != null &&
                txtDireccion.getText() != null && !txtDireccion.getText().isEmpty();
    }

    private void cargarInformacionEnTabla() {
        List<EventoEmpleadosDTO> detalles = eventoEmpleadoRepository.findDetallesEmpleadosPorEvento(eventoDTO.getId());
        tablaInformes.setItems(FXCollections.observableArrayList(detalles));

        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        columApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidosEmpleado"));
        columEntrada.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));
        columSalida.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));
        columFuncion.setCellValueFactory(new PropertyValueFactory<>("descripcionFuncion"));
        columFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    @FXML
    private void volver(){
        Main.cambiarVista("/com/java/fx/eventos.fxml");
    }
    @FXML
    private void agregarEmpleadoAEvento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/fx/empleadosEventos.fxml"));
            loader.setControllerFactory(SpringContextUtil.getContext()::getBean);

            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Empleado");
            // Crear los botones dinámicamente
            ButtonType agregarButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cerrarButtonType = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(agregarButtonType, cerrarButtonType);

            CrudEmpleadosEventos controller = loader.getController();
            // Pasar la información del evento al controlador del diálogo
            controller.setEventoInfo(
                    txtId.getText(),
                    txtNombreCliente.getText(),
                    txtDireccion.getText(),
                    txtFechaInicio.getValue(),
                    txtFechaFin.getValue()
            );

            // Establezco el callback para actualizar la tabla al cerrar el diálogo
            dialog.setOnHidden(event -> cargarInformacionEnTabla());

            // Obtener el botón "Agregar" y definir su acción manualmente. Lo hago para que no se cierre el dialog
            Button agregarButton = (Button) dialog.getDialogPane().lookupButton(agregarButtonType);
            agregarButton.addEventFilter(ActionEvent.ACTION, event -> {
                try {
                    controller.agregarEmpleado();
                } catch (Exception e) {
                    event.consume(); // Consumir el evento para evitar que el diálogo se cierre en caso de error
                }
            });

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void mostrarDialogoModificarEmpleado(EventoEmpleadosDTO empleadoEvento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/fx/empleadosEventos.fxml"));
            loader.setControllerFactory(SpringContextUtil.getContext()::getBean);

            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Modificar Empleado");

            // Crear los botones dinámicamente
            ButtonType modificarButtonType = new ButtonType("Modificar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cerrarButtonType = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(modificarButtonType, cerrarButtonType);



            CrudEmpleadosEventos controller = loader.getController();
            // Pasar la información del evento al controlador del diálog
            controller.setEventoInfo(
                    txtId.getText(),
                    txtNombreCliente.getText(),
                    txtDireccion.getText(),
                    txtFechaInicio.getValue(),
                    txtFechaFin.getValue()
            );
            // Pasar la información del empleado al controlador del diálogo
            controller.cargarDatosEmpleado(empleadoEvento);

            // Establezco el callback para actualizar la tabla al cerrar el diálogo
            dialog.setOnHidden(event -> cargarInformacionEnTabla());

            // Obtener el botón "Modificar" y definir su acción manualmente para guardar los cambios
            Button modificarButton = (Button) dialog.getDialogPane().lookupButton(modificarButtonType);
            modificarButton.addEventFilter(ActionEvent.ACTION, event -> {
                if(controller.modificarEmpleado()){
                    dialog.close(); // Cerrar el diálogo después de modificar
                }else{
                    event.consume();
                }

            });

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarEmpleadoDeEvento() {
        EventoEmpleadosDTO selectedEmpleado = tablaInformes.getSelectionModel().getSelectedItem();
        if (selectedEmpleado != null) {
            // Usar la nueva consulta para obtener el `EventosEmpleado`
            EventosEmpleado eventosEmpleado = eventoEmpleadoRepository.findByEmpleadoDetails(
                    selectedEmpleado.getNombreEmpleado(),
                    selectedEmpleado.getApellidosEmpleado(),
                    selectedEmpleado.getFecha(),
                    selectedEmpleado.getHoraEntrada(),
                    eventoDTO.getId()
            );

            if (eventosEmpleado != null) {
                // Crear el ID compuesto usando el `dniEmpleado` obtenido
                EventosEmpleadoId id = new EventosEmpleadoId(
                        eventosEmpleado.getId().getEventoId(),
                        eventosEmpleado.getId().getEmpleadoDni(),
                        eventosEmpleado.getId().getFecha(),
                        eventosEmpleado.getId().getHoraEntrada()
                );
                if(FuncionesMenu.mostrarDialogConfirmacion("Eliminación Empleado.","¿Está seguro que desea eliminar este registro?")){
                    eventoEmpleadoRepository.deleteById(id); // Eliminar el registro
                    cargarInformacionEnTabla(); // Actualizar la tabla
                }
            } else {
                FuncionesMenu.mostrarMensajeAlerta("Error", "No se pudo encontrar el empleado para eliminar.");
            }
        } else {
            FuncionesMenu.mostrarMensajeAlerta("Selección Requerida", "Debe seleccionar un empleado de la tabla para eliminar.");
        }
    }


}
