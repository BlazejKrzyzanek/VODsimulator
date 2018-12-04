package model.cinematography;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
TODO Konstruktor
TODO Dokumentacja
 */

public class CWork {

    private Image posterImage;
    private String title;
    private String description;
    private List<String> countryList;
    private List<Integer> productionYearList;
    private float userRating;

    public CWork(){
        this.posterImage = new Image("/view/img/Poster1.jpg");
        this.title = "Tytuł";
        this.description = "Opis";
        this.countryList = new ArrayList<>();
        for(int i=0; i<10; i++) this.countryList.add("adsasd");
        this.productionYearList = new ArrayList<>();
        this.productionYearList.add(1989);
        this.userRating = 6.5f;
    }

    public CWork(String title, String description, List<String> countryList, List<Integer> productionYearList, float userRating) {
        this.title = title;
        this.description = description;
        this.countryList = countryList;
        this.productionYearList = productionYearList;
        this.userRating = userRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<String> countryList) {
        this.countryList = countryList;
    }

    public List<Integer> getProductionYearList() {
        return productionYearList;
    }

    public void setProductionYearList(List<Integer> productionYearList) {
        this.productionYearList = productionYearList;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CWork cWork = (CWork) o;
        return Objects.equals(title, cWork.title) &&
                Objects.equals(description, cWork.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }
}
