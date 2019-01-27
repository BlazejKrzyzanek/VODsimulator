package model;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles time and sends signals to other objects
 */
public class Simulation extends Task<Integer> implements Serializable {

    private static LocalDateTime simulationTime;

    // Simulation speed parameters
    private static final int TICKS_PER_SECOND = 50;
    private static final int SPEED = 50;
    private static final int ONE_TICK = 1000 / TICKS_PER_SECOND;

    private ControlPanel cp;
    private AtomicBoolean paused;

    static {
        simulationTime = LocalDateTime.of(2019,1,1,1,1);
    }

    public Simulation() {
        cp = ControlPanel.getInstance();
        simulationTime = LocalDateTime.of(2019,1,1,1,1);
        paused = new AtomicBoolean(true);
        updateMessage("Start simulation");
    }

    /**
     * Main loop of simulation
     */
    @Override
    protected Integer call(){
        long next_game_tick = System.currentTimeMillis();
        int yesterday = 0;
        boolean monthWithoutProfitAdded = false;

        while (!isCancelled() && !Thread.currentThread().isInterrupted()) {

            // changing time
            simulationTime = simulationTime.plusMinutes(SPEED);

            updateMessage(simulationTime.toLocalDate().toString());
            // checking how much money has ControlPanel at the beginning of month
            if(simulationTime.toLocalDate().getDayOfMonth() == 1 && cp.getMoney().get() < 0 && !monthWithoutProfitAdded) {
                monthWithoutProfitAdded = true;
                Platform.runLater(() ->
                        cp.setMonthsWithoutProfit(cp.getMonthsWithoutProfit().get() + 1));
            } // if month with profit occurs, reset monthsWithoutProfit
            else if (simulationTime.toLocalDate().getDayOfMonth() == 1 && cp.getMoney().get() >= 0)
                Platform.runLater(() ->
                        cp.setMonthsWithoutProfit(0));
            else if (simulationTime.toLocalDate().getDayOfMonth() == 2 && monthWithoutProfitAdded){
                monthWithoutProfitAdded = false;
            }

            synchronized (this) { // adding 1 User per 3 CWorks
                if (cp.isCanUserBeAdded() && cp.getCWorks().size() % 3 == 0) {
                    cp.addUser();
                } // Paying and negotiating with distributors
                if(simulationTime.getDayOfMonth() != yesterday)
                    Platform.runLater(() -> {
                        cp.pay();
                        cp.negotiate();
                        int size = cp.getCWorks().size();
                        if (size > 1)
                            for (int i = 0; i <size/5; i++) // Creating promotions
                                cp.getCWorks().get(new Random().nextInt(size)).startPromotion();
                    });
            }
            yesterday = simulationTime.getDayOfMonth();

            // pause
            synchronized(this) {
                while (paused.get()) {
                    try {
                        wait();
                        next_game_tick = System.currentTimeMillis();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            next_game_tick += ONE_TICK;
            long sleep_time = next_game_tick - System.currentTimeMillis();
            if (sleep_time >= 0) {
                try {
                    Thread.sleep(sleep_time);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return 0;
    }

    /**
     * Starts paused simulation
     */
    public synchronized void startSimulation(){
        notify();

        paused.set(false);
        for (Distributor d : cp.getDistributors()){
            d.startLoop();
        }
        for (User u : cp.getUsers()){
            u.startLoop();
        }
    }

    /**
     * Pauses running simulation
     */
    public synchronized void pauseSimulation(){
        paused.set(true);
        for (Distributor d : cp.getDistributors()){
            d.pauseLoop();
        }
        for (User u :  cp.getUsers()){
            u.pauseLoop();
        }
    }

    /**
     * @return true if simulation is paused
     */
    public boolean isPaused() {
        return paused.get();
    }

    /**
     * @return simulation time
     */
    public synchronized static LocalDateTime getDateTime(){
        return simulationTime;
    }

    /**
     * Converts duration in "minutes" (for example from movie duration) for realtime milliseconds to sleep
     * @param minutes minutes to convert
     * @return time to sleep in milliseconds
     */
    static int simMinutesToRealMillis(int minutes) {
        return ONE_TICK * (minutes / Simulation.SPEED);
    }

    /**
     * @return duration of one tick in milliseconds
     */
    static int getOneTick() {
        return ONE_TICK;
    }
}
