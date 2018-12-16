package model;

public class VariableSpeedClock {

    private double speed;
    private long startTime;

    public VariableSpeedClock(double speed) {
        this(speed, System.currentTimeMillis());
    }

    public VariableSpeedClock(double speed, long startTime) {
        this.speed = speed;
        this.startTime = startTime;
    }

    public long getTime () {
        return (long) ((System.currentTimeMillis() - this.startTime) * this.speed + this.startTime);
    }
}