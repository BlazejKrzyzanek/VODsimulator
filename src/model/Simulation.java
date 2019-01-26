package model;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation extends Task<Integer> implements Serializable {

    private static LocalDateTime simulationTime;

    private static final int TICKS_PER_SECOND = 50;
    private static final int SPEED = 50;
    private static final int ONE_TICK = 1000 / TICKS_PER_SECOND;

    private ControlPanel cp;
    private AtomicBoolean paused;

    static {
        simulationTime = LocalDateTime.of(2019,1,1,1,1);
    }

    Simulation() {
        cp = ControlPanel.getInstance();
        paused = new AtomicBoolean(true);
        updateMessage("Start simulation");
    }

    @Override
    protected Integer call(){
        long next_game_tick = System.currentTimeMillis();
        int yesterday = 0;
        boolean monthWithoutProfitAdded = false;

        while (!isCancelled() && !Thread.currentThread().isInterrupted()) {

            simulationTime = simulationTime.plusMinutes(SPEED);

            updateMessage(simulationTime.toLocalDate().toString());
            if(simulationTime.toLocalDate().getDayOfMonth() == 1 && cp.getMoney().get() < 0 && !monthWithoutProfitAdded) {
                monthWithoutProfitAdded = true;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        cp.setMonthsWithoutProfit(cp.getMonthsWithoutProfit().get() + 1);
                    }
                });
            }
            else if (simulationTime.toLocalDate().getDayOfMonth() == 1 && cp.getMoney().get() >= 0)
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        cp.setMonthsWithoutProfit(0);
                    }
                });
            else if (simulationTime.toLocalDate().getDayOfMonth() == 2 && monthWithoutProfitAdded){
                monthWithoutProfitAdded = false;
            }

            if (cp.getMonthsWithoutProfit().get() >= 3){

            }
            synchronized (this) {
                if (cp.isCanUserBeAdded() && cp.getCWorks().size() % 3 == 0) {
                    cp.addUser();
                }
                if(simulationTime.getDayOfMonth() != yesterday)
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            cp.pay();
                            cp.negotiate();
                        }
                    });
            }

            yesterday = simulationTime.getDayOfMonth();

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

    synchronized void startSimulation(){
        notify();

        paused.set(false);
        for (Distributor d : cp.getDistributors()){
            d.startLoop();
        }
        for (User u : cp.getUsers()){
            u.startLoop();
        }
    }

    synchronized void pauseSimulation(){
        paused.set(true);
        for (Distributor d : cp.getDistributors()){
            d.pauseLoop();
        }
        for (User u :  cp.getUsers()){
            u.pauseLoop();
        }
    }

    boolean isPaused() {
        return paused.get();
    }

    public synchronized static LocalDateTime getDateTime(){
        return simulationTime;
    }

    static int simMinutesToRealMillis(int minutes) {
        return ONE_TICK * (minutes / Simulation.SPEED);
    }

    public static int getOneTick() {
        return ONE_TICK;
    }
}
