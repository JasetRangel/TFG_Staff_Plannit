package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.EventoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventosRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
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
import java.util.List;
import java.util.ResourceBundle;

@Component
public class EventosController implements Initializable {


    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

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

    private void cargarDatos(){
        List<EventoDTO> eventos = eventosRepository.findAllEventosWithClientDetails();
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));
    }

    @FXML
    private void agregarEvento(){
        FuncionesMenu.limpiarObjetoSeleccionado();
        Main.cambiarVista("/com/java/fx/eventosCrud.fxml");
    }
}
