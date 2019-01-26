package model;

import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import model.cinematography.CWork;
import model.cinematography.LiveStreaming;
import model.cinematography.Movie;
import model.cinematography.Series;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/*
TODO konstruktor wypełniający losowymi danymi
TODO negotiate - negocjacja z ControlPanel ustalając nowy payment
TODO generator filmów/ seriali/ live
TODO dokumentacja!
 */

public class Distributor extends Task<Integer> implements Serializable {
    private String distributorName;
    private int payment = 10;
    private ObservableList<CWork> cWorks;
    private AtomicBoolean paused;
    private ControlPanel cp;

    public Distributor() {
        this.distributorName = createName();
        this.cWorks = FXCollections.observableArrayList(new ArrayList<>());
        this.paused = new AtomicBoolean(false);
        this.cp = ControlPanel.getInstance();
    }

    @Override
    public Integer call() {
        int randomMinutes;
        startLoop();
        while (!isCancelled() && !Thread.currentThread().isInterrupted()){
            try {
                synchronized(this) {
                    while (paused.get()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                randomMinutes = new Random().nextInt(4320)+1; // Minimum one per 3 days
                System.out.println(this.toString() + (char)27 + "[33m\tTWORZĘ DZIEŁO\n" +(char)27 +"[0m");
                createCWork();
                Thread.sleep(Simulation.simMinutesToRealMillis(randomMinutes));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return 0;
    }

    public synchronized void pauseLoop(){
        paused.set(true);
    }

    public synchronized void startLoop(){
        notify();
        paused.set(false);
    }








    private String createName(){
        String name = ControlPanel.getInstance().getDistributorNames().get(
                new Random().nextInt(ControlPanel.getInstance().getDistributorNames().size())); // Random distributorName from list
        String country = ControlPanel.getInstance().getAllCountries().get(
                new Random().nextInt(ControlPanel.getInstance().getAllCountries().size())); // Random country from list
        return(name + " " + country);

    }

    public void negotiate(){
        int tmp = new Random().nextInt((cp.getMovieSinglePrice() +
                cp.getLiveStreamSinglePrice() +
                cp.getSeriesSinglePrice()) * 4);
        this.payment = tmp * this.cWorks.size() + cp.getUsers().size() / 20;
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
                this.cp.addCWork(cWork);
                return cWork;
            case 1:
                cWork = new Series(this);
                this.cWorks.add(cWork);
                this.cp.addCWork(cWork);
                return cWork;
            case 2:
                cWork = new LiveStreaming(this);
                this.cWorks.add(cWork);
                this.cp.addCWork(cWork);
                return cWork;

                default: return null;
        }
    }

    public ObservableList<CWork> getCWorks() {
        return cWorks;
    }

    public void deleteCWork(CWork c){
        this.cp.deleteCWork(c);
        this.cWorks.removeIf(cWork -> cWork.equals(c));
    }

    public void setcWorks(List<CWork> cWorks) {
        this.cWorks.clear();
        this.cWorks.addAll(cWorks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distributor that = (Distributor) o;
        return payment == that.payment &&
                Objects.equals(distributorName, that.distributorName) &&
                Objects.equals(cWorks, that.cWorks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distributorName, payment, cWorks);
    }

    @Override
    public String toString() {
        return distributorName;
    }


}
