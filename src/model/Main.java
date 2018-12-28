package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

/*
TODO obsługa okien i panelu kontrolnego
TODO dokumentacja
 */

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));

        Parent root = loader.load();


        MainController controller = new MainController();
        loader.setController(controller);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Symulator systemu VOD");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
