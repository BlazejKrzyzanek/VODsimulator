package model.cinematography;

import model.Distributor;

import java.time.LocalDate;

/*
TODO dokumentacja!
 */

public class Episode extends Movie{
    private int number;
    private LocalDate releaseDate;

    public Episode(Distributor distributor, int number) {
        super(distributor);
        this.number = number;
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
                "number=" + number +
                ", title=" + this.getTitle() +
                ", releaseDate=" + releaseDate +
                "}\n";
    }
}
