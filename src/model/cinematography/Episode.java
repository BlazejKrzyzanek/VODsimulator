package model.cinematography;

import java.util.Calendar;

/*
TODO Konstruktor tworzący losowy odcinek
TODO dokumentacja!
 */

public class Episode {
    private Calendar releaseDate;
    private int length;
    private int singlePrice;

    public Episode(Calendar releaseDate, int length, int singlePrice) {
        this.releaseDate = releaseDate;
        this.length = length;
        this.singlePrice = singlePrice;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(int singlePrice) {
        this.singlePrice = singlePrice;
    }


}
