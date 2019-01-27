package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for window at the end of simulation
 */
public class UnprofitableController implements Initializable {

    @FXML private Button endButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    /**
     * Closing window
     */
    @FXML private void endButtonOA(ActionEvent e){
        Stage stage1 = (Stage) endButton.getScene().getWindow();
        stage1.close();
    }
}
