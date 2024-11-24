package it.leulive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.ConnectException;

import java.io.IOException;

import it.leulive.utils.ClientManager;

public class App extends Application {

    private static Scene scene;

    private static boolean first_time = true;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 1500, 900);
        stage.setTitle("Applicazione Chat");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        if (!first_time) {
            ClientManager.setChatController(fxmlLoader.getController());
        }
        first_time = false;
        return root;
    }

    public static void main(String[] args) throws IOException {
        try {
            ClientManager.inizializeSocket();
        } catch (ConnectException e) {
            System.out.println("Server non raggiungibile");
            System.exit(0);
        }
        launch();
    }

}