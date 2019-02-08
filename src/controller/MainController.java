package controller;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ControlPanel;
import model.Distributor;
import model.Simulation;
import model.User;
import model.cinematography.CWork;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main Controller for main window
 */
public class MainController implements Initializable {
    @FXML private Slider basicPriceSlider;
    @FXML private Slider familyPriceSlider;
    @FXML private Slider premiumPriceSlider;
    @FXML private Slider singlePriceSlider;

    @FXML private TextField basicPriceText;
    @FXML private TextField familyPriceText;
    @FXML private TextField premiumPriceText;
    @FXML private TextField singlePriceText;
    @FXML private TextField searchText;

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

        // Setting labels
        this.simulationTimeLabel.textProperty().bind(sim.messageProperty()); // binding local date from simulation
        this.moneyLabel.textProperty().bind(cp.getMoney().asString());
        this.monthsWithoutProfitLabel.textProperty().bind(cp.getMonthsWithoutProfit().asString());
        this.monthsWithoutProfitLabel.textProperty().addListener((ov, o, n) -> {
            if (n.equals("3")) {
                unprofitable(); // Opening new window
            }
        });

        // Setting lists
        this.distributorsListView.setItems(cp.getDistributors());
        this.distributorsListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2)
                    distributorOA(distributorsListView.getSelectionModel().getSelectedItem()); // opening new window
        });

        this.usersListView.setItems(cp.getUsers());
        this.usersListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2)
                    userOA(usersListView.getSelectionModel().getSelectedItem()); // opening new window
        });

        // Setting table with products
        this.cWorksIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.cWorksTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.cWorksTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Searching through products
        FilteredList<CWork> fCWork = new FilteredList(cp.getCWorks(), p -> true); // Creating filtered list with predicate
        this.cWorkTableView.setItems(fCWork);
        this.cWorkTableView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2)
                productOA(cWorkTableView.getSelectionModel().getSelectedItem() );
        });

        // Searching for searchText in filtered list, based on title
        searchText.setOnKeyReleased(keyEvent -> fCWork.setPredicate(p ->
                p.getTitle().toLowerCase().contains(searchText.getText().toLowerCase().trim())));


        // Setting sliders
        cp.setBasicPrice(((int) basicPriceSlider.getValue()));
        basicPriceSlider.valueProperty().addListener(
                (ov, o, n) ->
                {
                    cp.setBasicPrice(n.intValue());
                    basicPriceText.setText(Integer.toString(n.intValue()));
                } );
        cp.setFamilyPrice((int) familyPriceSlider.getValue());
        familyPriceSlider.valueProperty().addListener(
                (ov, o, n) ->
                {
                    cp.setFamilyPrice(n.intValue());
                    familyPriceText.setText(Integer.toString(n.intValue()));
                } );
        cp.setPremiumPrice((int) premiumPriceSlider.getValue());
        premiumPriceSlider.valueProperty().addListener(
                (ov, o, n) ->
                {
                    cp.setPremiumPrice(n.intValue());
                    premiumPriceText.setText(Integer.toString(n.intValue()));
                } );
        cp.setSinglePrice((int) singlePriceSlider.getValue());
        singlePriceSlider.valueProperty().addListener(
                (ov, o, n) ->
                {
                    cp.setSinglePrice(n.intValue());
                    singlePriceText.setText(Integer.toString(n.intValue()));
                } );
    }

    /**
     * Opens new window when simulation ends
     */
    private void unprofitable(){
        pauseButtonOA(); // resets simulation
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("UnprofitableView.fxml"));

        UnprofitableController unprofitableController = new UnprofitableController();
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

    /**
     * Opens new window with details about selected distributor
     * @param d selected distributor
     */
    static void distributorOA(Distributor d){
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

    /**
     * Opens new window with details about selected user
     * @param u selected user
     */
    static void userOA(User u){
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

    /**
     * Opens new window with details about selected product
     * @param c selected product (CWork)
     */
    static void productOA(CWork c){
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

    /**
     * Resets simulation to default
     * FIXME: pieniadze dodaja sie jeszcze chwile po zatrzymaniu (dodać semafor)
     */
    @FXML protected void resetButtonOA(){
        if(sim != null && !sim.isPaused()){
            // Must be first, it stops all running threads in simulation
            sim.cancel();
            simTh.interrupt();
            cp.resetAll();

            // Creating new simulation
            sim = new Simulation();
            simTh = new Thread(sim);
            simTh.setDaemon(true);

            simTh.start();
            simulationTimeLabel.textProperty().bind(sim.messageProperty());

            distributorsListView.setItems(cp.getDistributors());
            distributorsListView.refresh();

            usersListView.setItems(cp.getUsers());
            usersListView.refresh();
            // Searching through products
            FilteredList<CWork> fCWork = new FilteredList(cp.getCWorks(), p -> true); // Creating filtered list with predicate
            this.cWorkTableView.setItems(fCWork);
            this.cWorkTableView.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2)
                    productOA(cWorkTableView.getSelectionModel().getSelectedItem() );
            });

            // Searching for searchText in filtered list, based on title
            searchText.setOnKeyReleased(keyEvent -> fCWork.setPredicate(p ->
                    p.getTitle().toLowerCase().contains(searchText.getText().toLowerCase().trim())));

            // Setting prices based on sliders
            cp.setBasicPrice(((int) basicPriceSlider.getValue()));
            cp.setFamilyPrice(((int) familyPriceSlider.getValue()));
            cp.setPremiumPrice(((int) premiumPriceSlider.getValue()));
            cp.setSinglePrice(((int) singlePriceSlider.getValue()));
            cp.setMoney(0);
        }

    }

    /**
     * Starting or resuming simulation
     */
    @FXML protected void startButtonOA(){
        if (sim == null) {
            sim = new Simulation();
            simTh = new Thread(sim);
            simTh.setDaemon(true);
            simTh.start();
        } else{
            sim.startSimulation();
        }
    }

    /**
     * Pausing simulation
     */
    @FXML protected void pauseButtonOA(){
        if(sim != null){
            sim.pauseSimulation();
        }
    }

    /**
     * Adding new distributor
     */
    @FXML protected void distributorButtonOA(){
        if(sim != null  && !sim.isPaused()) { // only to running simulation
            if(cp.getDistributors().size() == 0){
                this.distributorsListView.setItems(cp.getDistributors());
                this.distributorsListView.getItems().addListener((ListChangeListener<Distributor>) c ->
                        distributorsListView.scrollTo(c.getList().size() - 1));
            }
            cp.addDistributor();
            this.distributorsListView.refresh();
        }
    }

    /**
     * Adding new user
     */
    @FXML protected synchronized void userButtonOA() {
        if (sim != null && !sim.isPaused()) { // only to running simulation
            if (cp.getUsers().size() == 0) {
                this.usersListView.setItems(cp.getUsers());
                this.usersListView.getItems().addListener((ListChangeListener<User>) c ->
                        usersListView.scrollTo(c.getList().size() - 1));
            }
            cp.addUser();
            usersListView.refresh();
        }
    }

    /**
     * Saving actual state
     * FIXME: zapisywanie aktualnego stanu symulacji
     */
    @FXML protected void saveButtonOA(){
        try {
            if(sim != null){
                sim.pauseSimulation();
                cp.write();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Reading simulation state from a file
     * FIXME: najpierw naprawic zapisywanie, pozniej zajac sie odczytem
     */
    @FXML protected void readButtonOA(){
        try{
            cp.read();
            sim.startSimulation();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        distributorsListView.setItems(cp.getDistributors());
        usersListView.setItems(cp.getUsers());
        cWorkTableView.setItems(cp.getCWorks());
    }
}
