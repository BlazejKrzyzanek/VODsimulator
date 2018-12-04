package model.cinematography;

import java.util.List;
import java.util.Objects;

/*
TODO Generator odcinków
TODO Dokumentacja!
 */

public class Season {
    private int number;
    private List<Episode> episodes;

    public Season(int number, List<Episode> episodes) {
        this.number = number;
        this.episodes = episodes;
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
        this.episodes = episodes;
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
