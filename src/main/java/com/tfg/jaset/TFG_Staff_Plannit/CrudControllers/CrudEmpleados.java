package com.tfg.jaset.TFG_Staff_Plannit.CrudControllers;

import com.tfg.jaset.TFG_Staff_Plannit.DTOs.InformeEmpleadoDTO;
import com.tfg.jaset.TFG_Staff_Plannit.Main;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Empleado;
import com.tfg.jaset.TFG_Staff_Plannit.Models.Evento;
import com.tfg.jaset.TFG_Staff_Plannit.Models.EventosEmpleado;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EmpleadoRepository;
import com.tfg.jaset.TFG_Staff_Plannit.Repositories.EventoEmpleadoRepository;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;


@Component
public class CrudEmpleados implements Initializable {

    @FXML
    private TableView<InformeEmpleadoDTO> tablaInformes;

    @FXML
    private TableColumn<InformeEmpleadoDTO, String> anio;

    @FXML
    private TableColumn<InformeEmpleadoDTO, String> mes;

    @FXML
    private TableColumn<InformeEmpleadoDTO, String> informe;

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

        FuncionesMenu.botonMano(btnGuardar,  btnInformar, btnVerInforme, btnVolver);

        anio.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.3));
        mes.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.3));
        informe.prefWidthProperty().bind(tablaInformes.widthProperty().multiply(0.4));

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
        }
        FuncionesMenu.tabular(padre,padreColun1,padreColun2,padreBotonones);
        FuncionesMenu.configurarEstiloBotones(btnInformar,btnVerInforme,btnVolver,btnGuardar);

        cargarInformesEmpleado();
    }
    // Método para cargar los informes en la tabla
    // Método para cargar los informes y los eventos relacionados en la tabla
    public void cargarInformesEmpleado() {
        String dniEmpleado = txtDNI.getText().trim(); // Obtener el DNI del empleado
        List<InformeEmpleadoDTO> informes = eventoEmpleadoRepository.findInformesPorEmpleado(dniEmpleado);

        for (InformeEmpleadoDTO informe : informes) {
            List<Evento> eventos = eventoRepository.findEventosPorInforme(informe.getAnio(), informe.getMes(), dniEmpleado);
            informe.setEventos(eventos);
        }
        tablaInformes.setItems(FXCollections.observableArrayList(informes));
    }

    @FXML
    private void volver(){
        Main.setObjetoSeleccionado(null);
        Main.cambiarVista("/com/java/fx/empleados.fxml");
    }
    @FXML
    private void verInforme() {
        if (tablaInformes.getSelectionModel().getSelectedItem() != null) {
            InformeEmpleadoDTO informeSeleccionado = tablaInformes.getSelectionModel().getSelectedItem();
            double totalHoras = calcularHorasTrabajadasEnMes(informeSeleccionado);
            mostrarDetallesInforme(informeSeleccionado, totalHoras);
        } else {
            FuncionesMenu.mostrarMensajeAlerta("Selección requerida", "Seleccione un informe de la tabla para ver los detalles.");
        }
    }

    private double calcularHorasTrabajadasEnMes(InformeEmpleadoDTO informe) {
        List<Evento> eventos = informe.getEventos();
        double totalHoras = 0.0;

        for (Evento evento : eventos) {
            totalHoras += calcularHorasEvento(evento, informe.getDniEmpleado(), informe.getAnio(), informe.getMes());
        }


        return totalHoras;
    }

    private double calcularHorasEvento(Evento evento, String dniEmpleado, int anio, String mes) {
        List<EventosEmpleado> registros = eventoEmpleadoRepository.findByEventoIdAndEmpleadoDniAndAnioAndMesOrderByFechaAsc(evento.getId(), dniEmpleado, anio, mes);
        double horasTotales = 0.0;

        for (EventosEmpleado registro : registros) {
            System.out.println("Hora entrada: " + registro.getId().getHoraEntrada());
            System.out.println("Hora salida: " + registro.getHoraSalida());
            LocalTime entrada = registro.getId().getHoraEntrada();
            LocalTime salida = registro.getHoraSalida();

            if (entrada != null && salida != null) {
                double horasEntrada = convertirHoraADouble(entrada);
                double horasSalida = convertirHoraADouble(salida);
                System.out.println("Hora entrada double: " + horasEntrada);
                System.out.println("Hora salida double: " + horasSalida);
                if (horasSalida >= horasEntrada) {
                    horasTotales+=horasSalida-horasEntrada;
                    System.out.println("Horas totales mias: "+ horasTotales);
                }else{
                    horasTotales+=(24-horasEntrada)+horasSalida;
                    System.out.println("Horas totales mias: "+ horasTotales);
                }
            }
        }

        System.out.println("Total horas: " + horasTotales);
        return horasTotales;
    }

    private double convertirHoraADouble(LocalTime hora) {
        return hora.getHour() + hora.getMinute() / 60.0 + hora.getSecond() / 3600.0;
    }

    private void mostrarDetallesInforme(InformeEmpleadoDTO informe, double totalHoras) {
        // Mostrar el total de horas trabajadas en un diálogo
        FuncionesMenu.mostrarMensajeAlerta("Total de Horas Trabajadas",
                "Total de horas trabajadas en " + informe.getMes() + " " + informe.getAnio() + ": " + totalHoras);
    }



    @FXML
    private void informar(){
        FuncionesMenu.mostrarMensajeAlerta("Desarrollando","Función en desarollo");
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
                if(!empleado.getDni().equals(empleNuevo.getDni())){
                    try{
                        empleadoRepository.delete(empleado);
                        FuncionesMenu.actualizarObjeto(empleadoRepository,empleNuevo);
                        limpiarCampos();
                    }catch(Exception e){
                        FuncionesMenu.mostrarMensajeAlerta("Fallo al modificar al empleado","El DNI de este empleado no puede ser modificado");
                        System.out.println(e.getMessage());
                    }
                }else{
                    FuncionesMenu.actualizarObjeto(empleadoRepository,empleNuevo);
                }
            }
        }else{
            FuncionesMenu.mostrarMensajeAlerta("Cambios requeridos","No se ha hecho ningún cambio en el Empleado");
        }
    }

    private  void limpiarCampos(){
        Main.setObjetoSeleccionado(null);
        txtDNI.clear();
        txtDireccion.clear();
        txtApellidos.clear();
        txtNombre.clear();
        txtTel.clear();
        txtEdad.clear();
        txtBanco.clear();
        txtEmail.clear();

    }







}
