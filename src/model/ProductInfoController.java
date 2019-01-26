package model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.cinematography.CWork;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static java.lang.Math.abs;

public class ProductInfoController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label countryLabel;
    @FXML private Label actorLabel;
    @FXML private ImageView image;
    @FXML private Button statisticsButton;

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
            this.countryLabel.setText(this.product.getCountryList().toString());
        });
    }


}
