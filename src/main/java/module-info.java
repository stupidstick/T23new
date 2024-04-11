module com.example.t23new {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.t23new to javafx.fxml;
    exports com.example.t23new;
    exports com.example.t23new.visual;
    opens com.example.t23new.visual to javafx.fxml;
}