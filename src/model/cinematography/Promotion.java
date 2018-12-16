package model.cinematography;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Represents promotion with selected discount
 */
public class Promotion {
    private int duration;
    private int discountPercent;
    protected final Logger log = Logger.getLogger(getClass().getName());

    /**
     * Creates random promotion in duration between 120 - 1440 minutes (2 to 24 hour)
     */
    public Promotion(){this(new Random().nextInt(1321) + 120);}

    /**
     * Creates new promotion from now for duration (minutes)
     * @param duration
     */
    public Promotion(int duration) {
        this(duration, Promotion.createDiscount());
    }

    /**
     * Creates new promotion from now for duration (minutes) with the appropriate discount (percent)
     * @param duration
     * @param discountPercent
     */
    public Promotion(int duration, int discountPercent) {
        this.duration = duration;
        this.discountPercent = discountPercent;
    }

    /**
     * Returns random percent of discount in range [0, 100]
     * @return int
     */
    public static int createDiscount(){
        return Promotion.createDiscount(0,100);
    }

    /**
     * Creates discount percent
     * @param a min discount > 0
     * @param b max discount <= 100
     * @return random percent of discount in range
     * @throws IllegalArgumentException
     */
    public static int createDiscount(int a, int b){
        try {
            if (a >= b || a < 0 || b > 100) {
                throw new IllegalArgumentException("a must be smaller than  b");
            }
        } catch (IllegalArgumentException e){
            System.out.println((char)27 + "[33m" + e.getMessage() + (char)27 + "[0m");
        }
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
        try {
            if (duration < 1)
                throw new IllegalArgumentException("Duration must be positive");
            this.duration = duration;
        } catch (IllegalArgumentException e){
            System.out.println((char)27 + "[33m" + e.getMessage() + (char)27 + "[0m");
        }
    }

    /**
     * @return discount percent
     */
    public int getDiscountPercent() {
        return discountPercent;
    }

    /**
     * Sets discount percent from range [0,100]
     * @param discountPercent
     */
    public void setDiscountPercent(int discountPercent) {
        try {
            if (discountPercent < 0 || discountPercent > 100)
                throw new IllegalArgumentException("discount percent not in range [0,100]");
            this.discountPercent = discountPercent;
        }catch (IllegalArgumentException e){
            System.out.println((char)27 + "[33m" + e.getMessage() + (char)27 + "[0m");
        }
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "duration=" + duration +
                ", discountPercent=" + discountPercent +
                '}';
    }
}
