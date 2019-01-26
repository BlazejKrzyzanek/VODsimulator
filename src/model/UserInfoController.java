package model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class UserInfoController implements Initializable{

    @FXML private Label idLabel;
    @FXML private Label mailLabel;
    @FXML private Label dateLabel;
    @FXML private Label nrLabel;
    @FXML private Label subscriptionLabel;
    @FXML private ImageView avatarImage;
    private User user;

    public UserInfoController(User u) {
        this.user = u;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            this.idLabel.setText((String.valueOf(this.user.getId())));
            String imageUrl = "/img/avatar" + user.getGenre() + ".jpg";
            this.avatarImage.setImage(new Image(imageUrl));
            this.mailLabel.setText(this.user.getEmail());
            this.dateLabel.setText(this.user.getBirthDate().toString());
            this.nrLabel.setText(this.user.getCardNumber());
            String subscriptionText;
            if(this.user.getVodSubscription() == null)
                subscriptionText = "Brak";
            else
                subscriptionText = this.user.getVodSubscription().toString();
            this.subscriptionLabel.setText(subscriptionText);
        });
    }
}
