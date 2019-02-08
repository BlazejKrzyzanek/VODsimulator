package controller;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class to start application
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    /**
     * Creates new window with simulation
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));

        Parent root = loader.load();


        MainController controller = new MainController();
        loader.setController(controller);

        Scene scene = new Scene(root);

        primaryStage.setTitle("VOD Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
