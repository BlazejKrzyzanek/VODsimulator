package model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.cinematography.CWork;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private Button resetButton;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button distributorButton;
    @FXML private Button userButton;
    @FXML private Button saveButton;

    @FXML private Slider basicPriceSlider;
    @FXML private Slider familyPriceSlider;
    @FXML private Slider premiumPriceSlider;
    @FXML private Slider singlePriceSlider;

    @FXML private TextField basicPriceText;
    @FXML private TextField familyPriceText;
    @FXML private TextField premiumPriceText;

    @FXML private Label simulationTimeLabel;
    @FXML private Label moneyLabel;
    @FXML private Label monthsWithoutProfitLabel;

    @FXML private ListView<Distributor> distributorsListView;
    @FXML private ListView<User> usersListView;

    @FXML private TableView<CWork> cWorkTableView;
    @FXML private TableColumn<CWork, Integer> cWorksIDColumn;
    @FXML private TableColumn<CWork, String> cWorksTypeColumn;
    @FXML private TableColumn<CWork, String> cWorksTitleColumn;


    private Simulation sim;
    private ControlPanel cp;
    private Thread simTh;






    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cp = ControlPanel.getInstance();

        this.sim = new Simulation();
        this.simTh = new Thread(sim);
        this.simTh.setDaemon(true);
        this.simTh.start();

        this.simulationTimeLabel.textProperty().bind(sim.messageProperty());
        this.moneyLabel.textProperty().bind(cp.getMoney().asString());
        this.monthsWithoutProfitLabel.textProperty().bind(cp.getMonthsWithoutProfit().asString());
        this.monthsWithoutProfitLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (t1.equals("3")) {
                    unprofitable();
                }
            }
        });

        this.distributorsListView.setItems(cp.getDistributors());
        this.distributorsListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2)
                    distributorOA(distributorsListView);
        });

        this.usersListView.setItems(cp.getUsers());
        this.usersListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2)
                    userOA(usersListView);
        });

        this.cWorksIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.cWorksTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.cWorksTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        this.cWorkTableView.setItems(cp.getCWorks());
        this.cWorkTableView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2)
                productOA(cWorkTableView);
        });

        cp.setBasicPrice(((int) basicPriceSlider.getValue()));
        basicPriceSlider.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    cp.setBasicPrice(newvalue.intValue());
                    basicPriceText.setText(Integer.toString(newvalue.intValue()));
                } );
        cp.setFamilyPrice((int) familyPriceSlider.getValue());
        familyPriceSlider.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    cp.setFamilyPrice(newvalue.intValue());
                    familyPriceText.setText(Integer.toString(newvalue.intValue()));
                } );
        cp.setPremiumPrice((int) premiumPriceSlider.getValue());
        premiumPriceSlider.valueProperty().addListener(
                (observable, oldvalue, newvalue) ->
                {
                    cp.setPremiumPrice(newvalue.intValue());
                    premiumPriceText.setText(Integer.toString(newvalue.intValue()));
                } );

    }

    private void unprofitable(){
        resetButtonOA();
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("UnprofitableView.fxml"));

        UnprofitableController unprofitableController = new UnprofitableController(this.monthsWithoutProfitLabel.getScene().getWindow());
        loader.setController(unprofitableController);

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("You are bankrupt :)");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    static void distributorOA(ListView<Distributor> distributorsListView){
        Distributor d = distributorsListView.getSelectionModel().getSelectedItem();
        if(d != null) {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("DistributorInfoView.fxml"));

            DistributorInfoController distributorInfoController = new DistributorInfoController(d);
            loader.setController(distributorInfoController);

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(d.getDistributorName());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
    }

    static void userOA(ListView<User> usersListView){
        User u = usersListView.getSelectionModel().getSelectedItem();
        if(u != null) {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("UserInfoView.fxml"));

            UserInfoController userInfoController = new UserInfoController(u);
            loader.setController(userInfoController);

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(u.getEmail());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
    }

    static void productOA(TableView<CWork> cWorkTableView){
        CWork c = cWorkTableView.getSelectionModel().getSelectedItem();
        if(c != null) {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("ProductInfoView.fxml"));

            ProductInfoController productInfoController = new ProductInfoController(c);
            loader.setController(productInfoController);

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(c.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
    }

    @FXML protected void resetButtonOA(){
        if(sim != null){
            // Must be first, it stops all running threads in simulation
            sim.cancel();
            //simTh.interrupt();
            cp.resetAll();

            sim = new Simulation();
            simTh = new Thread(sim);
            simTh.setDaemon(true);

            simTh.start();
            simulationTimeLabel.textProperty().bind(sim.messageProperty());

            distributorsListView.setItems(cp.getDistributors());
            distributorsListView.refresh();

            usersListView.setItems(cp.getUsers());
            usersListView.refresh();

            cWorkTableView.setItems(cp.getCWorks());
        }

    }

    @FXML protected void startButtonOA(ActionEvent e){
        if (sim == null) {
            sim = new Simulation();
            simTh = new Thread(sim);
            simTh.setDaemon(true);
            simTh.start();
        } else{
            sim.startSimulation();
        }

    }

    @FXML protected void pauseButtonOA(ActionEvent e){
        if(sim != null){
            sim.pauseSimulation();
        }

    }

    @FXML protected void distributorButtonOA(ActionEvent e){
        if(sim != null && !sim.isPaused()) {
            if(cp.getDistributors().size() == 0){
                this.distributorsListView.setItems(cp.getDistributors());
                this.distributorsListView.getItems().addListener(new ListChangeListener<Distributor>() {
                    @Override
                    public void onChanged(Change<? extends Distributor> c) {
                        distributorsListView.scrollTo(c.getList().size() - 1);
                    }
                });
            }
            cp.addDistributor();
            this.distributorsListView.refresh();
        }
    }

    @FXML protected synchronized void userButtonOA(ActionEvent e) {
        if (sim != null && !sim.isPaused()) {
            if (cp.getUsers().size() == 0) {
                this.usersListView.setItems(cp.getUsers());
                this.usersListView.getItems().addListener(new ListChangeListener<User>() {
                    @Override
                    public void onChanged(Change<? extends User> c) {
                        usersListView.scrollTo(c.getList().size() - 1);
                    }
                });
            }
            cp.addUser();
            usersListView.refresh();
        }
    }

    @FXML protected void saveButtonOA(ActionEvent e){
        try {
            if(sim != null){
                sim.pauseSimulation();
            }
            cp.write();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML protected void readButtonOA(ActionEvent e){
        try{
            cp.read();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        distributorsListView.setItems(ControlPanel.getInstance().getDistributors());
        usersListView.setItems(ControlPanel.getInstance().getUsers());
        cWorkTableView.setItems(ControlPanel.getInstance().getCWorks());
    }
}
