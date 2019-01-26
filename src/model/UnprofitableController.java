package model;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class UnprofitableController implements Initializable {

    @FXML private Button endButton;
    private Stage parentStage;

    public UnprofitableController(Window stage){
        this.parentStage = (Stage) stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() ->{
        });
    }

    @FXML private void endButtonOA(ActionEvent e){
        Stage stage1 = (Stage) endButton.getScene().getWindow();
        stage1.close();
        parentStage.close();
    }
}
