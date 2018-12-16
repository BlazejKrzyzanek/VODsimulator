package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
TODO obsługa okien i panelu kontrolnego
TODO dokumentacja
 */

public class Main extends Application {

    public static void main(String[] args) {

        ControlPanel cp = new ControlPanel(0, false, 50);

        try {
            cp.runSimulation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/mainView.fxml"));

        primaryStage.setTitle("Symulator systemu VOD");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.close();
    }
}
