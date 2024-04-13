package com.tfg.jaset.TFG_Staff_Plannit.Utilidades;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class FuncionesMenu {
    public static void cambiarVentana( ActionEvent event, String rutaFXML, String tituloVentana, boolean forma) throws IOException {
        FXMLLoader fxml=new FXMLLoader(FuncionesMenu.class.getResource(rutaFXML));//cargo el fxml de la ventana nueva
        // Obteniendo el contexto de Spring desde la clase de utilidad
        fxml.setControllerFactory(SpringContextUtil.getContext()::getBean);

        // Crear una nueva escena con la vista cargada
        Scene scene=new Scene(fxml.load());

        // Obtengo la ventana actual y la sustituyo con la nueva ventana
        Stage stageActual= (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageActual.setScene(scene);
        stageActual.setMaximized(forma);
        stageActual.setResizable(forma);
        stageActual.setTitle(tituloVentana);
        Image logo=new Image(FuncionesMenu.class.getResourceAsStream("/images/staffPlannit.jpg"));
        stageActual.getIcons().add(logo);
        stageActual.show();
    }

    public static  boolean camposCompletos(TextInputControl ... campos){
        for (TextInputControl ti:campos){
            if (ti.getText()==null || ti.getText().trim().isEmpty())
                return  false;
        }
        return  true;
    }


}
