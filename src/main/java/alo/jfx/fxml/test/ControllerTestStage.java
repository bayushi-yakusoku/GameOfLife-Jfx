package alo.jfx.fxml.test;

import alo.jfx.controller.MyClosable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerTestStage implements MyClosable {
    private static final Logger logger = LogManager.getLogger(ControllerTestStage.class);

    @FXML
    private Button buttonTest;

    @FXML
    private Button buttonPrivate;

    public void initialize() {
        logger.info("Initialize: " + this.getClass().getCanonicalName());
    }

    public void actionPouf() {
        logger.debug("Hello");
    }

    public void actionTestClicked() {
        logger.debug("Hello");
    }

    @FXML
    private void actionPrivate() {
        logger.debug("Hello");
    }

    @Override
    public void onCloseEvent() {
        logger.info("Controller onCloseEvent: " + this.getClass().getCanonicalName());
    }
}
