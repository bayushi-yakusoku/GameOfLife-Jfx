module alo.jfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens alo.jfx to javafx.fxml;
    exports alo.jfx;
}