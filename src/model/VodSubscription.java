package model;

import java.util.Calendar;
import java.util.Objects;

/*
TODO Dokumentacja!
 */

public class VodSubscription {
    private enum type {Basic, Family, Premium};
    private int price;
    private int numberOfDevices;
    private enum maxResolution {HD, FullHD, UHD};
    private Calendar expirationDate;

    public VodSubscription(int price, int numberOfDevices, Calendar expirationDate) {
        this.price = price;
        this.numberOfDevices = numberOfDevices;
        this.expirationDate = expirationDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberOfDevices() {
        return numberOfDevices;
    }

    public void setNumberOfDevices(int numberOfDevices) {
        this.numberOfDevices = numberOfDevices;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VodSubscription that = (VodSubscription) o;
        return price == that.price &&
                numberOfDevices == that.numberOfDevices &&
                Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, numberOfDevices, expirationDate);
    }

    @Override
    public String toString() {
        return "VodSubscription{" +
                "price=" + price +
                ", numberOfDevices=" + numberOfDevices +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
