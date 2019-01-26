package model;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.cinematography.CWork;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class ProductInfoController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label countryLabel;
    @FXML private Label actorLabel;
    @FXML private ImageView image;

    @FXML private TextField priceText;
    @FXML private Slider priceSlider;
    @FXML private Button deleteButton;

    @FXML private BarChart<String, Integer> audienceChart;
    @FXML private CategoryAxis x;
    @FXML private NumberAxis y;



    private CWork product;

    public ProductInfoController(CWork product) {
        this.product = product;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            this.titleLabel.setText(this.product.getTitle());
            String imageUrl = "/img/" + this.product.getImageId() + ".jpg";
            this.image.setImage(new Image(imageUrl));
            this.descriptionLabel.setText(this.product.getDescription());

            String countryList = this.product.getCountryList().stream()
                    .map(n -> n.toString())
                    .collect(Collectors.joining(", ", "", ""));
            this.countryLabel.setText(countryList);

            String actorList = this.product.getActorList().stream()
                    .map(n -> n.toString())
                    .collect(Collectors.joining(", ", "", ""));
            this.actorLabel.setText(actorList);

            XYChart.Series series = new XYChart.Series();
            Map<LocalDate, Integer> map = new TreeMap<>(product.getAudienceMap());
            for (Map.Entry<LocalDate, Integer> entry: map.entrySet()){
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }

            priceSlider.valueProperty().setValue(product.getSinglePrice());
            priceText.setText(Integer.toString(product.getSinglePrice()));

            priceSlider.valueProperty().addListener(
                    (observable, oldvalue, newvalue) ->
                    {
                        product.setSinglePrice(newvalue.intValue());
                        priceText.setText(Integer.toString(newvalue.intValue()));
                    } );

            audienceChart.getData().add(series);
        });
    }

    @FXML private void deleteButtonOA(ActionEvent e){
        this.product.delete();
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }


}
