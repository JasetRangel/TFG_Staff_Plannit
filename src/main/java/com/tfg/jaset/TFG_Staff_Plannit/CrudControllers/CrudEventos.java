package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoEmpleadosDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Cliente;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class CrudEventos implements Initializable {

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnEliminarEmpleado;

    @FXML
    private Button btnAddEmpleado;

    @FXML
    private Button btnVolver;


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
        columNombre.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.2));
        columApellidos.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.3));
        columEntrada.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.15));
        columSalida.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.15));
        columFuncion.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.2));



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

    }

    @FXML
    private void actualizar_insertarEvento() {
        if (validarDatosEvento()) {
            try {
                System.out.println(eventoDTO.getIdCliente());
                Cliente cliente = clientesRepository.findById(eventoDTO.getIdCliente())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                Evento eventoModificar = new Evento();
                
                eventoModificar.setCliente(cliente);
                eventoModificar.setFechaInicio(txtFechaInicio.getValue());
                eventoModificar.setFechaFin(txtFechaFin.getValue());
                eventoModificar.setDireccionEvento(txtDireccion.getText());
                eventoModificar.setDetalles(txtDetalles.getText());
                if(eventoDTO==null || eventoModificar.esDiferente(evento,eventoModificar)){
                    eventosRepository.save(eventoModificar);
                    FuncionesMenu.actualizarObjeto(eventosRepository,evento);
                    FuncionesMenu.mostrarMensajeAlerta("Evento guardado", "El evento fue guardado con éxito");
                    volver();
                }else{
                    FuncionesMenu.mostrarMensajeAlerta("Objeto sin cambios", "No se han realizado cambios.");
                }

            } catch (Exception e) {
                FuncionesMenu.mostrarMensajeAlerta("Error al guardar", "No se pudo guardar el evento: " + e.getMessage());
            }
        } else {
            FuncionesMenu.mostrarMensajeAlerta("Validación", "Verifique que todos los campos estén llenos");
        }
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

            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Empleado");
            // Crear los botones dinámicamente
            ButtonType agregarButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cerrarButtonType = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(agregarButtonType, cerrarButtonType);


            EmpleadosEventos controller = loader.getController();
            // Pasar la información del evento al controlador del diálogo
            controller.setEventoInfo(
                    txtId.getText(),
                    txtNombreCliente.getText(),
                    txtDireccion.getText(),
                    txtFechaInicio.getValue(),
                    txtFechaFin.getValue()
            );

            // Estableco el callback para actualizar la tabla al cerrar el diálogo
            dialog.setOnHidden(event -> cargarInformacionEnTabla());

            // Obtener el botón "Agregar" y definir su acción manualmente. Lo hago para que no se cierre el dialog
            Button agregarButton = (Button) dialog.getDialogPane().lookupButton(agregarButtonType);
            agregarButton.addEventFilter(ActionEvent.ACTION, event -> {

                controller.agregarEmpleado();
                controller.limpiarCampos();
                // Consumir el evento para evitar que el diálogo se cierre
                event.consume();
            });

            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
