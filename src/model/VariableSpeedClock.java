package model;

import java.time.LocalDateTime;

public class VariableSpeedClock {

    private LocalDateTime simulationDateTime;
    private int speed;

    public VariableSpeedClock(int speed) {this(speed, LocalDateTime.now());}

    public VariableSpeedClock(int speed, LocalDateTime simulationDateTime) {
        this.speed = speed;
        this.simulationDateTime = simulationDateTime;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void run(){
        while (true){
            this.simulationDateTime.plusMinutes(this.speed);
        }
    }

    public LocalDateTime getSimulationDateTime() {
        return this.simulationDateTime;
    }
}