package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.cinematography.Movie;

/*
TODO obsługa okien i panelu kontrolnego
TODO dokumentacja
 */

public class Main extends Application {

    public static void main(String[] args) {

        System.out.println("Test");
        Movie film = new Movie();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/mainView.fxml"));

        primaryStage.setTitle("Symulator systemu VOD");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
