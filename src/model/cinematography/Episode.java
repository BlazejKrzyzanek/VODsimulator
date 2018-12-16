package model.cinematography;

import java.time.LocalDate;

/*
TODO Konstruktor tworzący losowy odcinek
TODO dokumentacja!
 */

public class Episode extends Movie{
    private LocalDate releaseDate;

    public Episode(int id) {
        super(id);
        this.releaseDate = createReleaseDate(1950,2020);

    }

    private LocalDate createReleaseDate(int minY, int maxY) {
        int year = r.nextInt((maxY - minY + 1)) + minY;
        int bound = 365;
        if (year%400==0 || year%4==0 && year%100!=0) bound = 366;
        int day = r.nextInt(bound)+1;
        return LocalDate.ofYearDay(year, day);
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "releaseDate=" + releaseDate +
                '}';
    }
}
