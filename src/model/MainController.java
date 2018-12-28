package model;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.cinematography.CWork;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private Button resetButton;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button fasterButton;
    @FXML private Button distributorButton;
    @FXML private Button userButton;

    @FXML private Slider basicPriceSlider;
    @FXML private Slider familyPriceSlider;
    @FXML private Slider premiumPriceSlider;
    @FXML private Slider singlePriceSlider;

    @FXML private Label simulationTimeLabel;

    @FXML private ListView<Distributor> distributorsListView;
    @FXML private ListView<User> usersListView;

    @FXML private TableView<ObservableList<CWork>> cWorkTableView;
    @FXML private TableColumn<ObservableList<CWork>, CWork> cWorksTypeColumn;
    @FXML private TableColumn<ObservableList<CWork>, CWork> cWorksTitleColumn;


    private Simulation sim;
    private Thread simTh;






    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.sim = new Simulation();
        this.simTh = new Thread(sim);
        this.simTh.setDaemon(true);
        simTh.start();
        this.simulationTimeLabel.textProperty().bind(sim.messageProperty());
        this.usersListView.setItems(sim.getUsers());
        cWorksTypeColumn.setCellValueFactory(new PropertyValueFactory<ObservableList<CWork>, CWork>("getTitle"));
        cWorkTableView.getItems().setAll(sim.getcWorks());

    }

    @FXML protected void resetButtonOA(ActionEvent e){
        if(this.sim != null){
            // Must be first, it stops all running threads in simulation
            this.sim.cancel();
            this.simTh.interrupt();
            this.sim = new Simulation();
            this.simTh = new Thread(sim);
            this.simTh.setDaemon(true);
            simTh.start();
            this.simulationTimeLabel.textProperty().bind(sim.messageProperty());

            this.distributorsListView.setItems(sim.getDistributors());
            this.distributorsListView.refresh();

            this.usersListView.setItems(sim.getUsers());
            this.usersListView.refresh();


        }

    }

    @FXML protected void startButtonOA(ActionEvent e){
        if (sim == null) {
            this.sim = new Simulation();
            this.simTh = new Thread(sim);
            this.simTh.setDaemon(true);
            simTh.start();
        } else{
            sim.startSimulation();
        }

    }

    @FXML protected void pauseButtonOA(ActionEvent e){
        if(this.sim != null){
            this.sim.pauseSimulation();
        }

    }

    @FXML protected void fasterButtonOA(ActionEvent e){
        if(!sim.isPaused())
            Simulation.faster();
    }

    @FXML protected void distributorButtonOA(ActionEvent e){
        if(sim != null && !sim.isPaused()) {
            if(sim.getDistributors().size() == 0){
                this.distributorsListView.setItems(sim.getDistributors());
                this.distributorsListView.getItems().addListener(new ListChangeListener<Distributor>() {
                    @Override
                    public void onChanged(Change<? extends Distributor> c) {
                        distributorsListView.scrollTo(c.getList().size() - 1);
                    }
                });
            }
            sim.addDistributor();
            this.distributorsListView.refresh();
        }
    }

    @FXML protected synchronized void userButtonOA(ActionEvent e) {
        if (sim != null && !sim.isPaused()) {
            if (sim.getUsers().size() == 0) {
                this.usersListView.setItems(sim.getUsers());
                this.usersListView.getItems().addListener(new ListChangeListener<User>() {
                    @Override
                    public void onChanged(Change<? extends User> c) {
                        usersListView.scrollTo(c.getList().size() - 1);
                    }
                });
            }
            sim.addUser();
            this.usersListView.refresh();
        }
    }

}
