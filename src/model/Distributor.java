package model;

import model.cinematography.CWork;

import java.util.List;
import java.util.Objects;

/*
TODO konstruktor wypełniający losowymi danymi (skąd je brać żeby miały jakiś sens?)
TODO negotiate - negocjacja z ControlPanel ustalając nowy payment
TODO generator filmów/ seriali/ live
TODO dokumentacja!
 */

public class Distributor {
    private String name;
    private int payment;
    private int money;
    private List<CWork> cWorks;

    public Distributor(String name, int payment, int money, List<CWork> cWorks) {
        this.name = name;
        this.payment = payment;
        this.money = money;
        this.cWorks = cWorks;
    }

    public void addMoney(int money){
        this.money += money;
    }

    public void negotiate(){
        this.setPayment(1000);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public List<CWork> getcWorks() {
        return cWorks;
    }

    public void setcWorks(List<CWork> cWorks) {
        this.cWorks = cWorks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distributor that = (Distributor) o;
        return payment == that.payment &&
                money == that.money &&
                Objects.equals(name, that.name) &&
                Objects.equals(cWorks, that.cWorks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, payment, money, cWorks);
    }

    @Override
    public String toString() {
        return "Distributor{" +
                "name='" + name + '\'' +
                ", payment=" + payment +
                ", money=" + money +
                ", cWorks=" + cWorks +
                '}';
    }


}
