module it.leulive {
    requires javafx.controls;
    requires javafx.fxml;

    opens it.leulive to javafx.fxml;
    exports it.leulive;
}
