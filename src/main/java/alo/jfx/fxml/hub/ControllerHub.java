package alo.jfx.fxml.hub;

import alo.jfx.controller.MyClosable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ControllerHub {

    private static final Logger logger = LogManager.getLogger(ControllerHub.class);

    public void initialize() throws IOException {
        logger.info("Initialize: " + this.getClass().getCanonicalName());
    }

    @FXML
    Button buttonTestStage;

    @FXML
    private void switchToSecondary() throws IOException {
        runAndWait("/alo/jfx/fxml/gameoflife/gameOfLife.fxml");
    }

    @FXML
    private void testClosable() throws IOException {
        runAndWait("/alo/jfx/fxml/raytracing/rayTracing.fxml");
    }

    @FXML
    void openTestStage() throws IOException {
        runAndWait("/alo/jfx/fxml/test/testStage.fxml");
    }

    private void runAndWait(String fxml) throws IOException {
//        URL url = App.class.getResource("/alo/jfx/fxml/gameoflife/gameOfLife.fxml");
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxml));

        Parent root = loader.load();

        MyClosable myClosableController = loader.getController();

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 1024, 768));
        stage.setOnCloseRequest(event -> myClosableController.onCloseEvent());
        stage.initModality(Modality.APPLICATION_MODAL);

        logger.info("Switched to closable test");

        stage.showAndWait();
    }
}
