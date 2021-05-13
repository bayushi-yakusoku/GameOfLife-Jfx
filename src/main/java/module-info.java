module alo.jfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;


    opens alo.jfx.controller to javafx.fxml;
//    opens alo.jfx to javafx.fxml;

    opens alo.jfx.fxml.gameoflife to javafx.fxml;
    opens alo.jfx.fxml.raytracing to javafx.fxml;
    opens alo.jfx.fxml.test to javafx.fxml;
    opens alo.jfx.fxml.hub to javafx.fxml;


    exports alo.jfx;
    exports alo.jfx.controller;

    exports alo.jfx.fxml.gameoflife;
    exports alo.jfx.fxml.raytracing;
    exports alo.jfx.fxml.test;
    exports alo.jfx.fxml.hub;
}
