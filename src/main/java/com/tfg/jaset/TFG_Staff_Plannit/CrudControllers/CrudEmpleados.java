package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventoEmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventosRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.FuncionesMenu;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.UsuarioUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;


@Component
public class CrudEmpleados implements Initializable {

    @FXML
    private TableView<InformeEmpleado> tablaInformes;

    @FXML
    private TableColumn<InformeEmpleado, String> anio;

    @FXML
    private TableColumn<InformeEmpleado, String> mes;

    @FXML
    private TableColumn<InformeEmpleado, String> informe;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnInformar;

    @FXML
    private Button btnVerInforme;

    @FXML
    private  Button btnVolver;

    @FXML
    private Button btnGuardar;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtBanco;

    @FXML
    private TextField txtDNI;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTel;

    @FXML
    private HBox padre;

    @FXML
    private HBox padreBotonones;

    @FXML
    private VBox padreColun1;

    @FXML
    private VBox padreColun2;

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    EventosRepository eventoRepository;
    @Autowired
    EventoEmpleadoRepository eventoEmpleadoRepository;


    private Empleado empleado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        anio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        mes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        informe.setCellValueFactory(new PropertyValueFactory<>("nombreInforme"));

        txtDNI.setFocusTraversable(true);
        txtDireccion.setFocusTraversable(true);
        txtEdad.setFocusTraversable(true);
        txtEmail.setFocusTraversable(true);
        txtNombre.setFocusTraversable(true);
        txtTel.setFocusTraversable(true);
        txtApellidos.setFocusTraversable(true);
        txtBanco.setFocusTraversable(true);

        padreColun2.setFocusTraversable(true);
        padreBotonones.setFocusTraversable(true);
        empleado= (Empleado) FuncionesMenu.getObjetoSeleccionado();
        if(empleado!=null){
            txtDNI.setText(empleado.getDni());
            txtNombre.setText(empleado.getNombre());
            txtApellidos.setText(empleado.getApellidos());
            txtEdad.setText(empleado.getEdad().toString());
            txtDireccion.setText(empleado.getDireccion());
            txtEmail.setText(empleado.getEmail());
            txtTel.setText(empleado.getTelefono());
            txtBanco.setText(empleado.getCuentaBancaria());

            btnGuardar.setCursor(Cursor.HAND);
            btnEliminar.setCursor(Cursor.HAND);
            btnInformar.setCursor(Cursor.HAND);
            btnVerInforme.setCursor(Cursor.HAND);
            btnVolver.setCursor(Cursor.HAND);
        }
        FuncionesMenu.tabular(padre,padreColun1,padreColun2,padreBotonones);
        FuncionesMenu.configurarEstiloBotones(btnEliminar,btnInformar,btnVerInforme,btnVolver,btnGuardar);

        cargarInformesEmpleado();


    }
    // Método para cargar los informes en la tabla
    // Método para cargar los informes y los eventos relacionados en la tabla
    public void cargarInformesEmpleado() {
        String dniEmpleado = txtDNI.getText().trim(); // Obtener el DNI del empleado
        List<InformeEmpleado> informes = eventoEmpleadoRepository.findInformesPorEmpleado(dniEmpleado);

        for (InformeEmpleado informe : informes) {
            List<Evento> eventos = eventoRepository.findEventosPorInforme(informe.getAnio(), informe.getMes(), dniEmpleado);
            informe.setEventos(eventos);  // Asegúrate de que esta línea esté agregando correctamente los eventos
        }

        tablaInformes.setItems(FXCollections.observableArrayList(informes));
    }

    @FXML
    private void volver(){

        Main.cambiarVista("/com/java/fx/empleados.fxml");
    }
    @FXML
    private void verInforme() {
        if (tablaInformes.getSelectionModel().getSelectedItem() != null) {
            InformeEmpleado informeSeleccionado = tablaInformes.getSelectionModel().getSelectedItem();
            mostrarDetallesInforme(informeSeleccionado);
        } else {
            FuncionesMenu.mostrarMensajeAlerta("Selección requerida", "Seleccione un informe de la tabla para ver los detalles.");
        }
    }
    @FXML
    private void informar(){

    }
    @FXML
    private void eliminar(){
        Empleado empleadoEliminar= (Empleado) FuncionesMenu.getObjetoSeleccionado();
        Empleado empleadoActual=UsuarioUtils.getUsuarioActual().getEmpleado();
        System.out.println(empleadoEliminar.getDni()+" empleado Actual: "+empleadoActual.getDni());

            if (empleadoEliminar.getDni().equals(UsuarioUtils.getUsuarioActual().getDni())) {
                FuncionesMenu.mostrarMensajeAlerta("Acción no permitida","No puede eliminar a este empleado, "
                +"ya que el usuario con el que ha iniciado sesión, pertenece a este empleado");
            } else {
                if(FuncionesMenu.mostrarDialogConfirmacion("Eliminación Empleado.","¿Está seguro de eliminar a este empleado?")){
                    empleadoRepository.delete(empleadoEliminar);
                }
            }
    }

    @FXML
    private  void actualiza_InsertarEmpleado(){
        Empleado empleNuevo=new Empleado();
        if(FuncionesMenu.camposCompletos(txtDNI,txtDireccion,txtApellidos,txtNombre,txtTel,txtEdad,txtBanco,txtEmail)){
            empleNuevo.setDni(txtDNI.getText());
            empleNuevo.setNombre(txtNombre.getText());
            empleNuevo.setApellidos(txtApellidos.getText());
            empleNuevo.setEmail(txtEmail.getText());
            empleNuevo.setEdad(Integer.parseInt(txtEdad.getText()));
            empleNuevo.setCuentaBancaria(txtBanco.getText());
            empleNuevo.setDireccion(txtDireccion.getText());
            empleNuevo.setTelefono(txtTel.getText());
        }else {
            FuncionesMenu.mostrarMensajeAlerta("Campos vacíos","Asegurese de que todos los campos estás rellenos");
        }
        if (empleado==null || empleado.esDiferente(empleado,empleNuevo)) {
            if(FuncionesMenu.mostrarDialogConfirmacion("Guardar Cambios","¿Quiere guardar los cambios?")){
                FuncionesMenu.actualizarObjeto(empleadoRepository,empleNuevo);
            }
        }else{
            FuncionesMenu.mostrarMensajeAlerta("Cambios requeridos","No se ha hecho ningún cambio en el Empleado");
        }
    }

    private void mostrarDetallesInforme(InformeEmpleado informe) {
        System.out.println("Detalles del Informe:");
        System.out.println("Año: " + informe.getAnio());
        System.out.println("Mes: " + informe.getMes());
        System.out.println("Informe: " + informe.getNombreInforme());
        System.out.println("Eventos Participados:");
        for (Evento evento : informe.getEventos()) {
            System.out.println(" - Evento: " + evento.getDetalles()
                    + ", Fecha Inicio: " + evento.getFechaInicio()
                    + ", Fecha Fin: " + evento.getFechaFin()
                    + ", Horas Trabajadas: " + encontrarHoras(evento));
        }
    }

    private BigDecimal encontrarHoras(Evento evento) {
        // Recuperar todos los registros EventosEmpleado para un evento específico
        List<EventosEmpleado> registros = eventoEmpleadoRepository.findByEventoId(evento.getId());
        BigDecimal horasTotales = BigDecimal.ZERO;

        // Sumar todas las horas trabajadas para este evento
        for (EventosEmpleado registro : registros) {
            LocalTime entrada = registro.getHoraEntrada();
            LocalTime salida = registro.getHoraSalida();
            if (entrada != null && salida != null && !salida.isBefore(entrada)) {
                // Calcula la duración entre la hora de entrada y la hora de salida
                Duration duracion = Duration.between(entrada, salida);
                // Convierte la duración a horas en formato decimal
                BigDecimal horas = BigDecimal.valueOf(duracion.toMinutes() / 60.0);
                // Añade al total
                horasTotales = horasTotales.add(horas);
            }
        }

        return horasTotales;
    }




}
