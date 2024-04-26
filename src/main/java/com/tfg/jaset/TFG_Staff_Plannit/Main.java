package com.tfg.jaset.TFG_Staff_Plannit;

import com.tfg.jaset.TFG_Staff_Plannit.Controllers.InicioController;
import com.tfg.jaset.TFG_Staff_Plannit.Utilidades.SpringContextUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Main extends Application {

	private static Main instance;
	public Main() {
		instance = this;
	}
	@Getter
	@Setter
	private static Object objetoSeleccionado;

	public static Main getInstance() {
		return instance;
	}

	public static ConfigurableApplicationContext context;
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		context=SpringApplication.run(Main.class);
		SpringContextUtil.setContext(context);// Aquí configuro el contexto en la clase de utilidad

		FXMLLoader fxml = new FXMLLoader(getClass().getResource("/com/java/fx/log.fxml"));
		fxml.setControllerFactory(context::getBean);

		Scene scene=new Scene(fxml.load());
		stage.setScene(scene);
		Image logo=new Image(getClass().getResourceAsStream("/images/staffPlannit.jpg"));
		stage.getIcons().add(logo);
		//stage.setFullScreen(true);// pone la ventana en pantalla completa
		stage.setResizable(false);// el usuario no podrá cambiar de tamaño esta ventana
		stage.show();
	}


	public static void cambiarVista(String rutaFXML) {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource(rutaFXML));
			loader.setControllerFactory(context::getBean);
			Node vista = loader.load();
			InicioController.getContenidoPane().getChildren().setAll(vista);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}


}
