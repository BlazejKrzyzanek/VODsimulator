package model.cinematography;

import model.Distributor;

/**
 * Represents movie as CWork
 */
public class Movie extends CWork{
    private int duration; // minutes

    /**
     * Creates a random movie with given id
     */
    public Movie(Distributor distributor, int id){
        super(distributor, id);
        this.duration = r.nextInt(61) + 20;
    }

    /**
     * Creates a random movie
     */
    public Movie(Distributor distributor){
        super(distributor);
        this.duration = r.nextInt(61) + 20;
    }

    /**
     * @return duration of the movie (minutes)
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the movie (minutes)
     * @param duration duration of the movie (minutes)
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "duration=" + duration +
                ", singlePrice=" + getSinglePrice() +
                "}\n\n";
    }
}
