module mvh.app.cpsc233w22a3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens mvh.app to javafx.fxml;
    exports mvh.app;
}