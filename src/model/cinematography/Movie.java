package model.cinematography;

import model.ControlPanel;
import model.Distributor;

//TODO Lista aktorów

/**
 * Represents movie as CWork
 */
public class Movie extends CWork{
    private int duration; // minutes
    private int availableTime; // minutes
    private Promotion promotion;

    /**
     * Creates a random movie
     */
    public Movie(Distributor distributor, int id){
        super(distributor, id);
        this.duration = r.nextInt(61) + 20;
        this.availableTime = r.nextInt(10081) + 1440; // between one day and one week
        this.promotion = null;
    }

    public Movie(Distributor distributor){
        super(distributor);
        this.duration = r.nextInt(61) + 20;
        this.availableTime = r.nextInt(10081) + 1440; // between one day and one week
        this.promotion = null;
    }

    /**
     * @return duration of the movie (minutes)
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the movie (minutes)
     * @param duration duration of the movie (minutes)
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return time by which movie can be watched after purchase
     */
    public int getAvailableTime() {
        return availableTime;
    }

    /**
     * Sets time by which movie can be watched after purchase
     * @param availableTime time by which movie can be watched after purchase
     */
    public void setAvailableTime(int availableTime) {
        this.availableTime = availableTime;
    }

    /**
     * @return promotion, null if does not exist
     */
    public Promotion getPromotion() {
        return promotion;
    }

    /**
     * Sets the promotion
     * @param promotion new promotion, null to remove
     */
    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    /**
     * Begins new random promotion
     */
    public void startPromotion() {
        this.promotion = new Promotion();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "duration=" + duration +
                ", singlePrice=" + getSinglePrice() +
                ", availableTime=" + availableTime +
                ", promotion=" + promotion +
                "}\n\n";
    }
}
