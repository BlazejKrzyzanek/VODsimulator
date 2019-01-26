package model.cinematography;

import model.ControlPanel;
import model.Distributor;
import model.Simulation;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
TODO Konstruktor bezparametrowy powinien wypełniać live losowymi parametrami (może z jakiegoś pliku?)
TODO Dokumentacja!
 */

public class LiveStreaming extends CWork{
    private int singlePrice;
    private LocalDateTime displayDateTime;

    public LiveStreaming(Distributor distributor) {
        super(distributor);
        this.singlePrice = ControlPanel.getInstance().getLiveStreamSinglePrice();
        this.displayDateTime = createDateTime(1, 30);
    }

    private LocalDateTime createDateTime(int minDays, int maxDays) {

        int days = r.nextInt(maxDays - minDays + 1) + minDays;
        int hour = r.nextInt(24);
        int min = r.nextInt(60);

        return Simulation.getDateTime().plusDays(days).withHour(hour).withMinute(min);
    }

    public int getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(int singlePrice) {
        this.singlePrice = singlePrice;
    }

    public LocalDateTime getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(LocalDateTime displayDateTime) {
        this.displayDateTime = displayDateTime;
    }

    @Override
    public String toString() {
        return "LiveStreaming{" +
                "singlePrice=" + singlePrice +
                ", displayDateTime=" + displayDateTime.toLocalDate() +
                "}\n\n";
    }
}
