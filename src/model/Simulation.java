package model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import model.cinematography.CWork;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Simulation extends Task<Integer> implements Serializable {

    private static LocalDateTime simulationTime;
    private static int TICKS_PER_SECOND = 50;
    static int ONE_TICK = 1000 / TICKS_PER_SECOND;
    private static volatile boolean ADD_USER;
    private static int speed;

    private boolean paused;
    private volatile ObservableList<Distributor> distributors;
    private volatile ObservableList<User> users;
    private volatile ObservableList<ObservableList<CWork>> cWorks; // TODO Wrzucić całą symulację do MainController, a wartości statyczne do ControlPanel


    static {
        simulationTime = LocalDateTime.now();
        ADD_USER = true;
        speed = 1;
    }

    Simulation() {
        simulationTime = LocalDateTime.now();
        speed = 1;
        paused = true;
        distributors = FXCollections.observableArrayList(new ArrayList<>());
        users = FXCollections.observableArrayList(new ArrayList<>());
        cWorks = FXCollections.observableArrayList(new ArrayList<>());
        updateMessage("Start simulation");
    }

    @Override
    protected Integer call(){
        long next_game_tick = System.currentTimeMillis();
        while (!isCancelled() && !Thread.currentThread().isInterrupted()) {
            if(!paused) {
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                simulationTime = simulationTime.plusMinutes(speed);
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                updateMessage(simulationTime.toLocalDate()
                        + " " + simulationTime.getHour()
                        + ":" + simulationTime.getMinute());
                synchronized (this) {
                    if (ADD_USER) {
                        addUser();
                        ADD_USER = false;
                    }
                }
                System.out.println(this.cWorks);
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
        if(isCancelled()){
            for (Distributor d: distributors){
                d.cancel();
            }
            for (User u: users){
                u.cancel();
            }
        }
        return 0;
    }

    public synchronized void startSimulation(){
        paused = false;
        for (Distributor d : distributors){
            d.startLoop();
        }
        for (User u : users){
            u.startLoop();
        }

    }

    public synchronized void pauseSimulation(){
        for (Distributor d : distributors){
            d.pauseLoop();
        }
        for (User u : users){
            u.pauseLoop();
        }
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public synchronized static LocalDateTime getDateTime(){
        return simulationTime;
    }

    public static int simMinutesToRealMillis(int minutes) {
        return ONE_TICK * (minutes / Simulation.speed);
    }

    public static void faster(){
        if(Simulation.speed < 100){
            Simulation.speed += 20;
        }else {
            Simulation.speed = 1;
        }
    }

    public static void setSpeed(int speed) {
        Simulation.speed = speed;
    }

    public synchronized void addDistributor(){
        Distributor dist = new Distributor();
        while(this.distributors.contains(dist)){ dist = new Distributor(); }

        Thread th = new Thread(dist);
        th.setDaemon(true);
        th.start();
        this.distributors.add(dist);
        this.cWorks.add(dist.getcWorks());
    }

    public synchronized ObservableList<Distributor> getDistributors() {
        return distributors;
    }

    public synchronized void addUser(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                User u = new User();
                Thread th = new Thread(u);
                th.setDaemon(true);
                th.start();
                if(!users.contains(u)){
                    users.add(u);
                }
            }
        });

    }

    public synchronized ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<ObservableList<CWork>> getcWorks() {
        return cWorks;
    }

    public void save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream("save")));
        out.writeObject(this);
        out.close();

    }
}
