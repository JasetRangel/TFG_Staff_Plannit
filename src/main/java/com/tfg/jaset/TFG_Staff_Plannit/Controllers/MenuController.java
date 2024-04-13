package com.tfg.jaset.TFG_Staff_Plannit.Controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class MenuController{

    public  MenuController(){

    }

    public  void salirDelaApp(ActionEvent event){
        Stage stageActual=(Stage) ((Node)event.getSource()).getScene().getWindow();
        stageActual.close();
    }
    public void desplazarMenu(ActionEvent event, Node nodeSlider){
        TranslateTransition transition=new TranslateTransition();
        transition.setDuration(Duration.seconds(0.4));
        transition.setNode(nodeSlider);
        if(nodeSlider.getTranslateX()<0){
            // Si el menú está oculto (desplazado a la izquierda), lo movemos a la posición 0
            transition.setToX(0);
        }
        else {
            // Si el menú está visible, lo movemos hacia la izquierda fuera de la vista
            transition.setToX(-nodeSlider.getBoundsInParent().getWidth());
        }
        transition.play();
    }

}
