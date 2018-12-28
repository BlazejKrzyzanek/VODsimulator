package model.cinematography;

import model.ControlPanel;
import model.Distributor;

import java.util.ArrayList;
import java.util.List;

/*
TODO Lista aktorów
TODO Dokumentacja!
 */

public class Series extends CWork {

    private int singlePrice;
    private List<Season> seasons;

    public Series(Distributor distributor) {
        super(distributor);
        this.seasons = createSeasons(2,10, distributor);
        this.singlePrice = ControlPanel.getSeriesSinglePrice();
    }

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

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons.clear();
        this.seasons.addAll(seasons);
    }

    @Override
    public String toString() {
        return "Series{" +
                "seasons=" + seasons +
                "}\n\n";
    }
}
