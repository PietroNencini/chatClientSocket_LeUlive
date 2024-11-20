module it.leulive {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens it.leulive to javafx.fxml;
    exports it.leulive;
}
