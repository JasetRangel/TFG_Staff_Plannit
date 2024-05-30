package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Cliente;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.ClientesRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ClientesController implements Initializable {


    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnListar;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnReporteTotal;

    @FXML
    private TableColumn<Cliente, String> columDescripcion;

    @FXML
    private TableColumn<Cliente, String> columEmail;

    @FXML
    private TableColumn<Cliente, Integer> columId;

    @FXML
    private TableColumn<Cliente, String> columNombre;

    @FXML
    private TableColumn<Cliente, String> columTel;

    @FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private TextField txtBusqueda;

    @Autowired
    ClientesRepository clientesRepository;

    @Autowired
    private EventosRepository eventosRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtBusqueda.setPromptText("Introduzca el Nombre del Cliente");

        columId.prefWidthProperty().bind(tablaClientes.widthProperty().multiply(0.1));
        columNombre.prefWidthProperty().bind(tablaClientes.widthProperty().multiply(0.2));
        columEmail.prefWidthProperty().bind(tablaClientes.widthProperty().multiply(0.2));
        columTel.prefWidthProperty().bind(tablaClientes.widthProperty().multiply(0.2));
        columDescripcion.prefWidthProperty().bind(tablaClientes.widthProperty().multiply(0.3));


        FuncionesMenu.desactivarByPermiso(btnEliminar);
       // FuncionesMenu.configurarEstiloBotones(btnBuscar, btnEliminar, btnListar, btnNuevo, btnReporteTotal);

        columDescripcion.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        columId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        FuncionesMenu.refrescarDatosTabla(tablaClientes,clientesRepository);

        FuncionesMenu.configurarTabla(tablaClientes, cliente -> {
            Cliente clienteSelected=tablaClientes.getSelectionModel().getSelectedItem();
            FuncionesMenu.setObjetoSeleccionado(clienteSelected);
            Main.cambiarVista("/com/java/fx/crudClientes.fxml");
        });


    }
    @FXML
    private void eliminarCliente() {
        Cliente clienteEliminar = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteEliminar != null) {
            List<Evento> eventosAsignados = eventosRepository.findEventosByNombreClienteAndDetalles(clienteEliminar.getNombre(),clienteEliminar.getDetalles());
            if (!eventosAsignados.isEmpty()) {
                FuncionesMenu.mostrarMensajeAlerta("Error", "Este cliente no se puede eliminar, dado que tiene uno o más eventos asignados.");
            } else {
                FuncionesMenu.eliminarEntidad(clienteEliminar,cliente -> {
                        clientesRepository.delete(clienteEliminar);
                        FuncionesMenu.refrescarDatosTabla(tablaClientes,clientesRepository);
                    });


            }
        }
    }
    @FXML
    private void filtrarClientePorNombre(){
        String filtro = txtBusqueda.getText().trim();
        if (filtro.isEmpty()) {
            FuncionesMenu.mostrarMensajeAlerta("Campo requerido", "Debe rellenar el campo de búsqueda");
        } else {
            List<Cliente> clientesEncontrados = clientesRepository.findAllByNombre(filtro);
            if (clientesEncontrados.isEmpty()) {
                FuncionesMenu.mostrarMensajeAlerta("Búsqueda", "No se encontró ningún cliente con el nombre especificado.");
                tablaClientes.setItems(FXCollections.observableArrayList());
            } else {
                tablaClientes.setItems(FXCollections.observableArrayList(clientesEncontrados));
            }
        }
    }
    @FXML
    private void agregarCliente(){
        FuncionesMenu.limpiarObjetoSeleccionado();
        Main.cambiarVista("/com/java/fx/crudClientes.fxml");
    }
    @FXML
    private void listar(){
        txtBusqueda.setText("");
        FuncionesMenu.refrescarDatosTabla(tablaClientes,clientesRepository);
    }
    @FXML
    private void reporteTotal(){
        FuncionesMenu.mostrarMensajeAlerta("Componente Sin Función","En desarrollo");
    }
}
