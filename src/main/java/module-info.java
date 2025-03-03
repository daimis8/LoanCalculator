module com.example.loancalculator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.loancalculator to javafx.fxml;
    opens com.example.loancalculator.controllers to javafx.fxml;

    exports com.example.loancalculator;
    exports com.example.loancalculator.controllers;
}