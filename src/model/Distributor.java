package model;

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

/**
 * Represents distributor of CWorks
 */
public class Distributor extends Task<Integer> implements Serializable {
    private String distributorName;
    private int payment = 10;
    private ObservableList<CWork> cWorks;
    private AtomicBoolean paused;
    private ControlPanel cp;
    private boolean completed;

    public Distributor() {
        this.distributorName = createName();
        this.cWorks = FXCollections.observableArrayList(new ArrayList<>());
        this.paused = new AtomicBoolean(false);
        this.cp = ControlPanel.getInstance();
        this.completed = false;
    }

    /**
     * Runs distributor loop
     * @return
     */
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
                randomMinutes = new Random().nextInt(4320)+1000; // Minimum one per 4 days, max 1 per day
                createCWork();
                Thread.sleep(Simulation.simMinutesToRealMillis(randomMinutes));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.completed = true;
        return 0;
    }

    /**
     * @return true if task is completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Pausing distributor loop
     */
    synchronized void pauseLoop(){
        paused.set(true);
    }

    /**
     * Starting distributor loop when it was paused
     */
    synchronized void startLoop(){
        notify();
        paused.set(false);
    }

    /**
     * Creates new distributor name
     * @return distributor name
     */
    private String createName(){
        String name = ControlPanel.getInstance().getDistributorNames().get(
                new Random().nextInt(ControlPanel.getInstance().getDistributorNames().size())); // Random distributorName from list
        String country = ControlPanel.getInstance().getAllCountries().get(
                new Random().nextInt(ControlPanel.getInstance().getAllCountries().size())); // Random country from list
        return(name + " " + country);

    }

    /**
     * Negotiating with ControlPanel about new payment
     */
    public void negotiate(){
        this.payment =  cp.getSinglePrice() * (new Random().nextInt(cp.getUsers().size() + 1) + 1)  ;
    }

    /**
     * @return name of distributor
     */
    public String getDistributorName() {
        return distributorName;
    }

    /**
     * @return how many money should distributor be paid
     */
    public int getPayment() {
        return payment;
    }

    /**
     * @param payment how many money should distributor be paid
     */
    public void setPayment(int payment) {
        this.payment = payment;
    }

    /**
     * Creates new random CWork
     */
    private synchronized void createCWork(){
        int type = new Random().nextInt(3);
        CWork cWork;
        switch (type){
            case 0:
                cWork = new Movie(this);
                this.cWorks.add(cWork);
                this.cp.addCWork(cWork);
                break;
            case 1:
                cWork = new Series(this);
                this.cWorks.add(cWork);
                this.cp.addCWork(cWork);
                break;
            case 2:
                cWork = new LiveStreaming(this);
                this.cWorks.add(cWork);
                this.cp.addCWork(cWork);
                break;
        }
    }

    /**
     * @return observable list of created CWorks
     */
    public ObservableList<CWork> getCWorks() {
        return cWorks;
    }

    /**
     * Deletes CWork from list
     * @param c CWork to delete
     */
    public void deleteCWork(CWork c){
        this.cp.deleteCWork(c);
        this.cWorks.removeIf(cWork -> cWork.equals(c));
    }

    /**
     * Sets new cWorks list
     * @param cWorks new list
     */
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