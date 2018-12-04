package model.cinematography;

import java.util.Calendar;
import java.util.List;

/*
TODO Konstruktor bezparametrowy powinien wypełniać live losowymi parametrami (może z jakiegoś pliku?)
TODO Dokumentacja!
 */

public class LiveStreaming extends CWork{
    private float singlePrice;
    private Calendar displayDate;

    public LiveStreaming(float singlePrice, Calendar displayDate) {
        this.singlePrice = singlePrice;
        this.displayDate = displayDate;
    }

    public LiveStreaming(String title, String description, List<String> countryList, List<Integer> productionYearList, float userRating, float singlePrice, Calendar displayDate) {
        super(title, description, countryList, productionYearList, userRating);
        this.singlePrice = singlePrice;
        this.displayDate = displayDate;
    }

    public float getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(float singlePrice) {
        this.singlePrice = singlePrice;
    }

    public Calendar getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(Calendar displayDate) {
        this.displayDate = displayDate;
    }
}
