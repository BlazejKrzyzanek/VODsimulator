package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import model.cinematography.CWork;
import model.cinematography.LiveStreaming;
import model.cinematography.Movie;
import model.cinematography.Series;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/*
TODO konstruktor wypełniający losowymi danymi
TODO negotiate - negocjacja z ControlPanel ustalając nowy payment
TODO generator filmów/ seriali/ live
TODO dokumentacja!
 */

public class Distributor extends Task<Integer> {
    private String distributorName;
    private int payment = 0;
    private int money;
    private ObservableList<CWork> cWorks;
    private boolean isPaused;

    public Distributor() {
        this.distributorName = createName();
        negotiate();
        this.money = 2;
        this.cWorks = FXCollections.observableArrayList(new ArrayList<>());
        this.isPaused = false;
    }

    @Override
    public Integer call() {
        int randomMinutes;
        while (!isCancelled() && !Thread.currentThread().isInterrupted()){
            try {
                if (!this.isPaused) {
                    randomMinutes = new Random().nextInt(4320)+1; // Minimum one per 3 days
                    System.out.println(this.toString() + (char)27 + "[33m\tTWORZĘ DZIEŁO\n" +(char)27 +"[0m");
                    createCWork();
                    Thread.sleep(Simulation.simMinutesToRealMillis(randomMinutes));
                } else {
                    Thread.sleep(Simulation.ONE_TICK);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return 0;
    }

    private String createName(){
        String name = ControlPanel.getDistributorNames().get(
                new Random().nextInt(ControlPanel.getDistributorNames().size())); // Random distributorName from list
        String country = ControlPanel.getAllCountries().get(
                new Random().nextInt(ControlPanel.getAllCountries().size())); // Random country from list
        return(name + " " + country);

    }

    public void addMoney(int money){
        this.money += money;
    }

    public void negotiate(){
        this.payment = 1000;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String name) {
        this.distributorName = name;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public synchronized CWork createCWork(){
        int type = new Random().nextInt(3);
        CWork cWork;
        switch (type){
            case 0:
                cWork = new Movie(this);
                this.cWorks.add(cWork);
                return cWork;
            case 1:
                cWork = new Series(this);
                this.cWorks.add(cWork);
                return cWork;
            case 2:
                cWork = new LiveStreaming(this);
                this.cWorks.add(cWork);
                return cWork;

                default: return null;
        }
    }

    public ObservableList<CWork> getcWorks() {
        return cWorks;
    }

    public void setcWorks(List<CWork> cWorks) {
        this.cWorks.clear();
        this.cWorks.addAll(cWorks);
    }

    public void pauseLoop(){
        this.isPaused = true;
    }

    public void startLoop(){
        this.isPaused = false;
    }

    public void stopLoop(){ this.cancel(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distributor that = (Distributor) o;
        return payment == that.payment &&
                money == that.money &&
                Objects.equals(distributorName, that.distributorName) &&
                Objects.equals(cWorks, that.cWorks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distributorName, payment, money, cWorks);
    }

    @Override
    public String toString() {
        return distributorName;
    }


}
