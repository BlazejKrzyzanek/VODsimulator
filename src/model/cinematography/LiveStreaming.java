package model.cinematography;

import model.ControlPanel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*
TODO Konstruktor bezparametrowy powinien wypełniać live losowymi parametrami (może z jakiegoś pliku?)
TODO Dokumentacja!
 */

public class LiveStreaming extends CWork{
    private float singlePrice;
    private LocalDateTime displayDateTime;

    public LiveStreaming(int id) {
        super(id);
        this.singlePrice = (r.nextInt(2000) + 10) / 100f;
        this.displayDateTime = createDateTime(1, 30);
    }

    private LocalDateTime createDateTime(int minDays, int maxDays) {

        int days = r.nextInt(maxDays - minDays + 1) + minDays;
        int hour = r.nextInt(24);
        int min = r.nextInt(60);

        return ControlPanel.getSimulationDateTime().plusDays(days).withHour(hour).withMinute(min);
    }

    public float getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(float singlePrice) {
        this.singlePrice = singlePrice;
    }

    public LocalDateTime getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(LocalDateTime displayDateTime) {
        this.displayDateTime = displayDateTime;
    }
}
