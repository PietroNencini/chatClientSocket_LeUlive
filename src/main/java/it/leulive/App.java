package it.leulive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import it.leulive.utils.ClientManager;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 900, 600);
        stage.setTitle("Applicazione Chat");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void openChatView() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("secondary.fxml"));
        System.out.println(App.class.getResource("secondary.fxml"));
        Parent root = loader.load();
        SecondaryController c = loader.getController();
        ClientManager.setChatController(c);
        setRoot("secondary");
    }

    public static void main(String[] args) throws IOException {
        ClientManager.inizializeSocket();
        launch();
    }

}