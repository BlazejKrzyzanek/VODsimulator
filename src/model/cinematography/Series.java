package model.cinematography;

import java.util.ArrayList;
import java.util.List;

/*
TODO Konstruktor bezparametrowy powinien wypełniać serial losowymi parametrami (może z jakiegoś pliku albo IMDB?), tworząc przy tym sezony, a może nawet odcinki
TODO Dokumentacja!
 */

public class Series extends CWork {

    private List<Season> seasons;

    public Series(int id) {
        super(id);
        this.seasons = createSeasons(2,10);
    }

    private List<Season> createSeasons(int minLen, int maxLen){
        try {
            if (minLen >= maxLen) {
                throw new IllegalArgumentException("createSeasons: minLen must be smaller than maxLen");
            }
        }catch (IllegalArgumentException e){
            System.out.println((char)27 + "[33m" + e.getMessage() + (char)27 + "[0m");
        }
        int length = r.nextInt(maxLen - minLen + 1) + minLen;
        List<Season> seasons = new ArrayList<>(length);
        for (int i=0; i<length; i++){
            seasons.add(new Season(i+1));
        }

        return seasons;

    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    @Override
    public String toString() {
        return "Series{" +
                "seasons=" + seasons +
                '}';
    }
}
