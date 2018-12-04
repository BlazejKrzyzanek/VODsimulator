package model.cinematography;

import jdk.jshell.spi.ExecutionControl;
import model.cinematography.CWork;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/*
TODO Konstruktor bezparametrowy powinien wypełniać film losowymi parametrami (może z jakiegoś pliku albo IMDB?)
TODO Dokumentacja!
 */

public class Movie extends CWork {
    private String category;
    private int length;
    private float singlePrice;
    private Calendar availableDate;

    public Movie(){
        this.category = "Akcja";
        this.length = 119;
        this.singlePrice = 19.99f;
        this.availableDate = new GregorianCalendar(2013, 5, 23);
    }

    public Movie(String title, String description, List<String> countryList, List<Integer> productionYearList,
                 float userRating, String category, int length, int singlePrice, Calendar availableDate) {
        super(title, description, countryList, productionYearList, userRating);
        this.category = category;
        this.length = length;
        this.singlePrice = singlePrice;
        this.availableDate = availableDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public float getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(int singlePrice) {
        this.singlePrice = singlePrice;
    }

    public Calendar getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Calendar availableDate) {
        this.availableDate = availableDate;
    }

    public void startPromotion() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Movie.startPromotion() - TODO");
    }
}
