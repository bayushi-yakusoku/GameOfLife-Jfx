package alo.jfx.controller;

import alo.jfx.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PrimaryController {

    private static final Logger logger = LogManager.getLogger(PrimaryController.class);

    public void initialize() throws IOException {
        logger.info("Initialize: " + this.getClass().getCanonicalName());
    }

    @FXML
    private void switchToSecondary() throws IOException {
        runAndWait("gameOfLife");
    }

    @FXML
    private void testClosable() throws IOException {
        runAndWait("rayTracing");
    }

    private void runAndWait(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

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
