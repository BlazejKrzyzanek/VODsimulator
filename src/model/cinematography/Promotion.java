package model.cinematography;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

/**
 * Represents promotion with selected discount
 */
public class Promotion {
    private Calendar startDate;
    private Calendar endDate;
    private int discountPercent;

    /**
     * Creates new promotion between dates
     * @param startDate
     * @param endDate
     */
    public Promotion(Calendar startDate, Calendar endDate) {
        this(startDate, endDate, Promotion.createDiscount());
    }

    /**
     * Creates new promotion between dates with the appropriate discount
     * @param startDate
     * @param endDate
     * @param discountPercent
     */
    public Promotion(Calendar startDate, Calendar endDate, int discountPercent) {
        if (startDate.compareTo(endDate) > 0){
            throw new IllegalArgumentException("Data początkowa jest późniejsza niż końcowa");
        }
        this.startDate = startDate;
        this.endDate = endDate;
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
     * Returns random percent of discount in range
     * @param a min discount > 0
     * @param b max discount <= 100
     * @return int
     * @throws IllegalArgumentException
     */
    public static int createDiscount(int a, int b) throws IllegalArgumentException {
        try {
            if (a >= b || a < 0 || b > 100) {
                throw new IllegalArgumentException("a musi być mniejsze od b, przyjmuję obie liczby z przedzialu [0,100]");
            }
        } catch (IllegalArgumentException e){
            a = 0;
            b = 100;
        }
        Random generator = new Random();
        return generator.nextInt((b-a)+1) + a;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() { return endDate; }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promotion promotion = (Promotion) o;
        return discountPercent == promotion.discountPercent &&
                Objects.equals(startDate, promotion.startDate) &&
                Objects.equals(endDate, promotion.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, discountPercent);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", discountPercent=" + discountPercent +
                '}';
    }
}
