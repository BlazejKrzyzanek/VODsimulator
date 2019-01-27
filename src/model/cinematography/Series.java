package model.cinematography;

import model.Distributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents Tv series
 */
public class Series extends CWork {
    private List<Season> seasons;

    /**
     * Creates new series with seasons
     * @param distributor creator
     */
    public Series(Distributor distributor) {
        super(distributor);
        this.seasons = createSeasons(2,10, distributor);
    }

    /**
     * Creates seasons
     * @param minLen
     * @param maxLen
     * @param distributor
     * @return
     */
    private List<Season> createSeasons(int minLen, int maxLen, Distributor distributor){
        if (minLen >= maxLen)
            throw new IllegalArgumentException("minLen must be smaller than maxLen");
        int length = r.nextInt(maxLen - minLen + 1) + minLen;
        List<Season> seasons = new ArrayList<>(length);
        for (int i=0; i<length; i++){
            seasons.add(new Season(distributor, i+1));
        }

        return seasons;

    }

    /**
     * @return list of seasons
     */
    public List<Season> getSeasons() {
        return seasons;
    }


    @Override
    public String toString() {
        return "Series{" +
                "seasons=" + seasons +
                "}\n\n";
    }
}
