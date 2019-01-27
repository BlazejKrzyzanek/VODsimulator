package model.cinematography;

import model.Distributor;

/**
 * Episode of series as a single movie
 */
public class Episode extends Movie{

    /**
     * Episode of Season / Series with episodeID
     * @param distributor creator of Series
     * @param episodeId represents number of episode in parent Season
     */
    public Episode(Distributor distributor, int episodeId) {
        super(distributor, episodeId);
    }
}
