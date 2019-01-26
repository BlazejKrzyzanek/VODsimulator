package model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

/*
TODO Dokumentacja!
 */

public class VodSubscription {
    private String type;
    private int price;
    private int numberOfDevices;
    private String maxResolution;
    private LocalDate expirationDate;

    public VodSubscription() {
        ControlPanel cp = ControlPanel.getInstance();
        int r = new Random().nextInt(3);
        switch (r){
            case 0:
                this.type = "Basic";
                this.price = cp.getBasicPrice();
                this.maxResolution = "HD";
                this.numberOfDevices = 2;
                break;
            case 1:
                this.type = "Family";
                this.price = cp.getFamilyPrice();
                this.maxResolution = "FullHD";
                this.numberOfDevices = 3;
                break;
            case 2:
                this.type = "Premium";
                this.price = cp.getPremiumPrice();
                this.maxResolution = "8K FUHD HDR 3D VR";
                this.numberOfDevices = 4;
                break;
        }
        this.expirationDate = Simulation.getDateTime().toLocalDate().plusMonths(1);
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

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
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
        return "Type: " + this.type +
                ",\nMax resolution: " + maxResolution +
                ",\nNumber of devices: " + numberOfDevices +
                ",\nExpiration date: " + expirationDate;
    }
}
