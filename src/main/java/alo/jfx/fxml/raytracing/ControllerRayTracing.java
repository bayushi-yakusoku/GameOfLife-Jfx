package alo.jfx.fxml.raytracing;

import alo.jfx.controller.MyClosable;
import alo.jfx.fxml.hub.ControllerHub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ControllerRayTracing implements MyClosable {

    private static final Logger logger = LogManager.getLogger(ControllerHub.class);

    @Override
    public void onCloseEvent() {
        logger.info("Controller onCloseEvent: " + this.getClass().getCanonicalName());
    }

    public void initialize() throws IOException {
        logger.info("Controller initialize...");
    }
}
