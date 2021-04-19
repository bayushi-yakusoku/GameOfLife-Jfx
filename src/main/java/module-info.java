module alo.jfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;

    opens alo.jfx.controller to javafx.fxml;
    opens alo.jfx to javafx.fxml;

    exports alo.jfx;
    exports alo.jfx.controller;
    exports alo.jfx.model;
}
