package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Usuario;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventosRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class EventosController implements Initializable {


    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnListar;

    @FXML
    private Button btnNewEvento;

    @FXML
    private TableColumn<EventoDTO, String> columCliente;

    @FXML
    private TableColumn<EventoDTO, String> columDetalles;

    @FXML
    private TableColumn<EventoDTO, String> columDireccion;

    @FXML
    private TableColumn<EventoDTO, LocalDate> columFin;

    @FXML
    private TableColumn<EventoDTO, Integer> columId;

    @FXML
    private TableColumn<EventoDTO, LocalDate> columInicio;

    @FXML
    private TableView<EventoDTO> tablaEventos;

    @FXML
    private TextField txtBusqueda;

    @Autowired
    private EventosRepository eventosRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtBusqueda.setPromptText("INGRESE EL NOMBRE DEL CLIENTE");
        columId.prefWidthProperty().bind(tablaEventos.widthProperty().multiply(0.1));
        columCliente.prefWidthProperty().bind(tablaEventos.widthProperty().multiply(0.2));
        columInicio.prefWidthProperty().bind(tablaEventos.widthProperty().multiply(0.1));
        columFin.prefWidthProperty().bind(tablaEventos.widthProperty().multiply(0.1));
        columDetalles.prefWidthProperty().bind(tablaEventos.widthProperty().multiply(0.25));
        columDireccion.prefWidthProperty().bind(tablaEventos.widthProperty().multiply(0.25));

        columId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        columInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        columFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        columDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columDetalles.setCellValueFactory(new PropertyValueFactory<>("detalles"));

        cargarDatos();

        FuncionesMenu.configurarTabla(tablaEventos, eventoDTO -> {
            EventoDTO eventoSelected=tablaEventos.getSelectionModel().getSelectedItem();
            FuncionesMenu.setObjetoSeleccionado(eventoSelected);
            Main.cambiarVista("/com/java/fx/eventosCrud.fxml");
        });


    }

    @FXML
    private  void filtrarDatosPorNombreCliente() {
        String filtro = txtBusqueda.getText();
        if (filtro.isEmpty()) {
            FuncionesMenu.mostrarMensajeAlerta("Campo vacío","Debe rellenar el campo de búsqueda");
            txtBusqueda.requestFocus();
        }else{
            List<Evento> eventos = eventosRepository.findEventosByNombreCliente(filtro);
            List<EventoDTO> eventosDTO = eventos.stream()
                    .map(evento -> new EventoDTO(evento.getId(), evento.getCliente().getNombre(),evento.getCliente().getId(), evento.getFechaInicio(),
                            evento.getFechaFin(), evento.getDireccionEvento(), evento.getDetalles()))
                    .collect(Collectors.toList());
            tablaEventos.setItems(FXCollections.observableArrayList(eventosDTO));
        }
    }

    private void cargarDatos(){
        List<EventoDTO> eventos = eventosRepository.findAllEventosWithClientDetails();
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));
    }

    @FXML
    private void eliminar(){
        EventoDTO eventoSelected=tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSelected != null) {
            Usuario user=UsuarioUtils.getUsuarioActual();
            if(user.getPermiso().equals("ADMIN")){
                if(FuncionesMenu.mostrarDialogConfirmacion("Eliminación Empleado.","¿Está seguro de eliminar a este empleado?")){
                    eventosRepository.deleteById(eventoSelected.getId());
                    cargarDatos();
                }
            }
        } else {
            FuncionesMenu.mostrarMensajeAlerta("Selección Requerida", "Debe seleccionar un usuario de la tabla");
            return;
        }
    }

    @FXML
    private void agregarEvento(){
        FuncionesMenu.limpiarObjetoSeleccionado();
        Main.cambiarVista("/com/java/fx/eventosCrud.fxml");
    }
    @FXML
    private void listar(){
        txtBusqueda.setText("");
        cargarDatos();
    }
}
