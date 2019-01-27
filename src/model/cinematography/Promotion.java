package model.cinematography;

import model.Simulation;

import java.beans.SimpleBeanInfo;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Represents promotion with selected discount
 */
public class Promotion implements Serializable {
    private int duration;
    private LocalDateTime expirationDateTime;
    private int discountPercent;
    protected final Logger log = Logger.getLogger(getClass().getName());

    /**
     * Creates random promotion in duration between minutes
     */
    public Promotion(){this(new Random().nextInt(13210) + 1200);}

    /**
     * Creates new promotion from now for duration (minutes)
     * @param duration duration of promotion in minutes
     */
    public Promotion(int duration) {
        this(duration, Promotion.createDiscount());
    }

    /**
     * Creates new promotion from now for duration (minutes) with the appropriate discount (percent)
     * @param duration duration of promotion in minutes
     * @param discountPercent discount
     */
    public Promotion(int duration, int discountPercent) {
        this.duration = duration;
        this.expirationDateTime = Simulation.getDateTime().plusMinutes(duration);
        this.discountPercent = discountPercent;
    }

    /**
     * Returns random percent of discount in range [0, 100]
     * @return int
     */
    private static int createDiscount(){
        return Promotion.createDiscount(0,100);
    }

    /**
     * Creates discount percent
     * @param a min discount > 0
     * @param b max discount <= 100
     * @return random percent of discount in range
     */
    private static int createDiscount(int a, int b){
        Random generator = new Random();
        return generator.nextInt((b-a)+1) + a;
    }

    /**
     * @return promotion duration (minutes)
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration sets duration of promotion (in minutes)
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return discount percent
     */
    public int getDiscountPercent() {
        return discountPercent;
    }

    /**
     * Sets discount percent in range [0,100]
     * @param discountPercent percent in range [0,100]
     */
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    @Override
    public String toString() {
        return "-" + discountPercent +
                "%!";
    }
}
