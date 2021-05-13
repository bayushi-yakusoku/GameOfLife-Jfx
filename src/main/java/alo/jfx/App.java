package alo.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        URL url = this.getClass().getResource("/alo/jfx/fxml/hub/hub.fxml");
        FXMLLoader loader = new FXMLLoader(url);

        Scene scene = new Scene(loader.load(), 640, 480);

        stage.setScene(scene);

        logger.info("Stage primary stage");

        stage.show();

        logger.info("End Main!!");
    }
}
