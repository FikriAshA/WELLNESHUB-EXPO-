module com.example.appexpo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.google.gson;

    opens com.example.appexpo to javafx.fxml, com.google.gson;
    exports com.example.appexpo;
}