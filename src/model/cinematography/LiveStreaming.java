package model.cinematography;

import model.Distributor;
import model.Simulation;

import java.time.LocalDate;

/**
 * Live streaming as a cinematographic work (CWork)
 */
public class LiveStreaming extends CWork{

    public LiveStreaming(Distributor distributor) {
        super(distributor);
        this.setReleaseDate(createDate(1, 30));
    }

    /**
     * Creates new release date in range from simulation date
     * @param minDays minimum days of delay from now
     * @param maxDays maximum days of delay from now
     * @return new release date
     */
    private LocalDate createDate(int minDays, int maxDays) {
        int days = r.nextInt(maxDays - minDays + 1) + minDays;
        return Simulation.getDateTime().plusDays(days).toLocalDate();
    }

    @Override
    public String toString() {
        return "LiveStreaming{" +
                "}\n\n";
    }
}
