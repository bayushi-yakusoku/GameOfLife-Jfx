package alo.jfx.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ControllerRayTracing implements MyClosable {

    private static final Logger logger = LogManager.getLogger(PrimaryController.class);

    @Override
    public void onCloseEvent() {
        logger.info("Controller onCloseEvent: " + this.getClass().getCanonicalName());
    }

    public void initialize() throws IOException {
        logger.info("Controller initialize...");
    }
}
