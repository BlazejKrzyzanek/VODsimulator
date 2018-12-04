package model.cinematography;

import java.util.List;

/*
TODO Konstruktor bezparametrowy powinien wypełniać serial losowymi parametrami (może z jakiegoś pliku albo IMDB?), tworząc przy tym sezony, a może nawet odcinki
TODO Dokumentacja!
 */

public class Series extends CWork {

    private String category;
    private List<Season> seasons;

    public Series(String title, String description, List<String> countryList, List<Integer> productionYearList,
                  float userRating, String category, List<Season> seasons) {
        super(title, description, countryList, productionYearList, userRating);
        this.category = category;
        this.seasons = seasons;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }
}
