package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeClientesDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Cliente;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.ClientesRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventosRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class CrudClientes implements Initializable {


    private final ClientesRepository clientesRepository;
    @FXML
    private TableColumn<InformeClientesDTO, String> anio;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnInformar;

    @FXML
    private Button btnVerInforme;

    @FXML
    private Button btnVolver;

    @FXML
    private TableView<InformeClientesDTO> tablaInformes;

    @FXML
    private TableColumn<InformeClientesDTO, String> informe;

    @FXML
    private TableColumn<InformeClientesDTO, String> mes;

    @FXML
    private HBox padre;

    @FXML
    private HBox padreBotonones;

    @FXML
    private VBox padreColun1;

    @FXML
    private VBox padreColun2;


    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTel;

    @Autowired
    private ClientesRepository ClientesRepository;

    @Autowired
    EventosRepository eventosRepository;

    private Cliente cliente;

    public CrudClientes(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FuncionesMenu.botonMano(btnGuardar, btnInformar, btnVerInforme, btnVolver);
        anio.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.3));
        mes.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.3));
        informe.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.4));

        initializeTableColumns();
        txtId.setStyle("-fx-text-fill: #070707; -fx-background-color:  rgba(77,92,92,0.53)");
        txtNombre.setFocusTraversable(true);
        txtTel.setFocusTraversable(true);
        txtDescripcion.setFocusTraversable(true);
        txtEmail.setFocusTraversable(true);

        padreColun2.setFocusTraversable(true);
        padreBotonones.setFocusTraversable(true);


        FuncionesMenu.tabular(padre,padreColun1,padreColun2,padreBotonones);
        FuncionesMenu.configurarEstiloBotones(btnInformar,btnVerInforme,btnVolver,btnGuardar);

        cliente= (Cliente) FuncionesMenu.getObjetoSeleccionado();
        if(cliente!=null){
            txtId.setText(String.valueOf(cliente.getId()));
            txtNombre.setText(cliente.getNombre());
            txtTel.setText(cliente.getTelefono());
            txtEmail.setText(cliente.getEmail());
            txtDescripcion.setText(cliente.getDetalles());
            cargarInformesClientes(Integer.parseInt(txtId.getText()) );
        }

    }

    private void initializeTableColumns() {
        anio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        mes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        informe.setCellValueFactory(new PropertyValueFactory<>("nombreInforme"));
    }

    private void cargarInformesClientes(Integer clienteId) {
        List<InformeClientesDTO> informes = clientesRepository.findInformesPorCliente(clienteId);
        informes.forEach(informe -> {
            List<Evento> eventos = eventosRepository.findEventosByClienteAndYearAndMonth(clienteId, informe.getAnio(), informe.getMes());
            informe.setEventos(eventos);
        });
        tablaInformes.setItems(FXCollections.observableArrayList(informes));
    }


    @FXML
    private void verInforme() {
        FuncionesMenu.mostrarMensajeAlerta("Desarrollando","Función en desarollo");
    }

    @FXML
    private void volver(){
        Main.setObjetoSeleccionado(null);
        Main.cambiarVista("/com/java/fx/clientes.fxml");
    }

    @FXML
    private void informar(){
        FuncionesMenu.mostrarMensajeAlerta("Desarrollando","Función en desarollo");
    }



    @FXML
    private  void actualiza_InsertarEmpleado(){
        Cliente clienteNuevo=new Cliente();
        if(FuncionesMenu.camposCompletos(txtDescripcion,txtNombre,txtTel,txtEmail)){

            if(!txtId.getText().isEmpty()){
                clienteNuevo.setId(Integer.parseInt(txtId.getText()));
            }
            clienteNuevo.setNombre(txtNombre.getText());
            clienteNuevo.setTelefono(txtTel.getText());
            clienteNuevo.setEmail(txtEmail.getText());
            clienteNuevo.setDetalles(txtDescripcion.getText());

            if (cliente==null || cliente.esDiferente(cliente,clienteNuevo)) {
                if(FuncionesMenu.mostrarDialogConfirmacion("Guardar Cambios","¿Quiere guardar los cambios?")){
                    FuncionesMenu.actualizarObjeto(clientesRepository,clienteNuevo);
                    limpiarCampos();
                }
            }else{
                FuncionesMenu.mostrarMensajeAlerta("Cambios requeridos","No se ha hecho ningún cambio en el Cliente");
            }
        }else {
            FuncionesMenu.mostrarMensajeAlerta("Campos vacíos","Asegurese de que todos los campos estás rellenos");
        }

    }

    private  void limpiarCampos(){
        txtId.setText("");
        txtDescripcion.setText("");
        txtTel.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
    }




}
