package model;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.*;
import java.time.LocalDateTime;

public class Simulation extends Task<Integer> implements Serializable {

    private static LocalDateTime simulationTime;

    private static final int TICKS_PER_SECOND = 50;
    private static final int SPEED = 50;


    static final int ONE_TICK = 1000 / TICKS_PER_SECOND;

    private ControlPanel cp;
    private boolean paused;

    static {
        simulationTime = LocalDateTime.now();
    }

    Simulation() {
        cp = ControlPanel.getInstance();
        simulationTime = LocalDateTime.now();
        paused = true;
        updateMessage("Start simulation");
    }

    @Override
    protected Integer call(){
        long next_game_tick = System.currentTimeMillis();
        int yesterday = 0;
        while (!isCancelled() && !Thread.currentThread().isInterrupted()) {
            if (!paused) {
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                simulationTime = simulationTime.plusMinutes(SPEED);
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                updateMessage(simulationTime.toLocalDate().toString());
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
        }
        return 0;
    }

    synchronized void startSimulation(){
        paused = false;
        for (Distributor d : cp.getDistributors()){
            d.startLoop();
        }
        for (User u : cp.getUsers()){
            u.startLoop();
        }
    }

    synchronized void pauseSimulation(){
        for (Distributor d : cp.getDistributors()){
            d.pauseLoop();
        }
        for (User u :  cp.getUsers()){
            u.pauseLoop();
        }
        paused = true;
    }

    boolean isPaused() {
        return paused;
    }

    public synchronized static LocalDateTime getDateTime(){
        return simulationTime;
    }

    static int simMinutesToRealMillis(int minutes) {
        return ONE_TICK * (minutes / Simulation.SPEED);
    }

}
