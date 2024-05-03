package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeClientes;
import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Cliente;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.ClientesRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class CrudClientes implements Initializable {


    private final ClientesRepository clientesRepository;
    @FXML
    private TableColumn<InformeClientes, String> anio;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnInformar;

    @FXML
    private Button btnVerInforme;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<InformeClientes, String> informe;

    @FXML
    private TableColumn<InformeClientes, String> mes;

    @FXML
    private HBox padre;

    @FXML
    private HBox padreBotonones;

    @FXML
    private VBox padreColun1;

    @FXML
    private VBox padreColun2;

    @FXML
    private TableView<Cliente> tablaInformes;

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

    private Cliente cliente;

    public CrudClientes(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FuncionesMenu.botonMano(btnGuardar, btnEliminar, btnInformar, btnVerInforme, btnVolver);

        txtId.setFocusTraversable(true);
        txtNombre.setFocusTraversable(true);
        txtTel.setFocusTraversable(true);
        txtDescripcion.setFocusTraversable(true);
        txtEmail.setFocusTraversable(true);

        padreColun2.setFocusTraversable(true);
        padreBotonones.setFocusTraversable(true);


        FuncionesMenu.tabular(padre,padreColun1,padreColun2,padreBotonones);
        FuncionesMenu.configurarEstiloBotones(btnEliminar,btnInformar,btnVerInforme,btnVolver,btnGuardar);

        cliente= (Cliente) FuncionesMenu.getObjetoSeleccionado();
        if(cliente!=null){
            txtId.setText(String.valueOf(cliente.getId()));
            txtNombre.setText(cliente.getNombre());
            txtTel.setText(cliente.getTelefono());
            txtEmail.setText(cliente.getEmail());
            txtDescripcion.setText(cliente.getDetalles());
        }

    }

    @FXML
    private void verInforme() {
        FuncionesMenu.mostrarMensajeAlerta("Desarrollando","Función en desarollo");
    }

    @FXML
    private void volver(){

        Main.cambiarVista("/com/java/fx/clientes.fxml");
    }

    @FXML
    private void informar(){
        FuncionesMenu.mostrarMensajeAlerta("Desarrollando","Función en desarollo");
    }

    @FXML
    private void eliminar(){
        Cliente clienteEliminar= (Cliente) FuncionesMenu.getObjetoSeleccionado();
        if(FuncionesMenu.mostrarDialogConfirmacion("Eliminación Cliente.","¿Está seguro de eliminar a este Cliente?")){
            clientesRepository.delete(clienteEliminar);
        }
    }


    @FXML
    private  void actualiza_InsertarEmpleado(){
        Cliente clienteNuevo=new Cliente();
        if(FuncionesMenu.camposCompletos(txtId,txtDescripcion,txtNombre,txtTel,txtEmail)){

            clienteNuevo.setId(Integer.parseInt(txtId.getText()));
            clienteNuevo.setNombre(txtNombre.getText());
            clienteNuevo.setTelefono(txtTel.getText());
            clienteNuevo.setEmail(txtEmail.getText());
            clienteNuevo.setDetalles(txtDescripcion.getText());
        }else {
            FuncionesMenu.mostrarMensajeAlerta("Campos vacíos","Asegurese de que todos los campos estás rellenos");
        }
        if (cliente==null || cliente.esDiferente(cliente,clienteNuevo)) {
            if(FuncionesMenu.mostrarDialogConfirmacion("Guardar Cambios","¿Quiere guardar los cambios?")){
                FuncionesMenu.actualizarObjeto(clientesRepository,clienteNuevo);
            }
        }else{
            FuncionesMenu.mostrarMensajeAlerta("Cambios requeridos","No se ha hecho ningún cambio en el Cliente");
        }
    }




}
