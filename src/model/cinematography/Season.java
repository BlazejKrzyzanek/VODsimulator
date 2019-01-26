package model.cinematography;

import model.Distributor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/*
TODO Dokumentacja!
 */

public class Season implements Serializable {
    private Distributor distributor;
    private int number;
    private List<Episode> episodes;

    public Season(Distributor distributor, int number) {
        this.distributor = distributor;
        this.number = number;
        this.episodes = createEpisodes(2, 10);
    }

    private List<Episode> createEpisodes(int minLen, int maxLen) {
        if (minLen >= maxLen)
            throw new IllegalArgumentException("minLen must be smaller than maxLen");
        int length = new Random().nextInt(maxLen - minLen + 1) + minLen;
        List<Episode> episodes = new ArrayList<>(length);
        for (int i=0; i<length; i++){
            episodes.add(new Episode(distributor, i+1));
        }
        return episodes;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }



    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes.clear();
        this.episodes.addAll(episodes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return number == season.number &&
                Objects.equals(episodes, season.episodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, episodes);
    }

    @Override
    public String toString() {
        return "Season{" +
                "number=" + number +
                ", episodes=" + episodes +
                '}';
    }
}
