package model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.cinematography.CWork;

import java.net.URL;
import java.util.ResourceBundle;

public class DistributorInfoController implements Initializable {

    @FXML private Label nameLabel;
    @FXML private Label paymentLabel;
    @FXML private TableColumn<CWork, Integer> idColumn;
    @FXML private TableColumn<CWork, String> typeColumn;
    @FXML private TableColumn<CWork, String> titleColumn;
    @FXML private TableView<CWork> cWorkTableView;

    private Distributor d;

    public DistributorInfoController(Distributor d){
        this.d = d;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() ->{
            this.nameLabel.setText(this.d.getDistributorName());
            this.paymentLabel.setText(String.valueOf(this.d.getPayment()));

            this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            this.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            this.titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            this.cWorkTableView.setItems(d.getCWorks());
            this.cWorkTableView.setOnMouseClicked(event -> {
                MainController.productOA(this.cWorkTableView);
            });
            this.cWorkTableView.refresh();

        });
    }
}
