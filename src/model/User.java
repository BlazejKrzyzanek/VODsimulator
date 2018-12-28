package model;

import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

/*
TODO Indywidualne ID dla każdego (może static albo coś?)
TODO Opłacanie abonamentu
TODO Losowe kupowanie filmu/serialu/live
TODO Dokumentacja!
 */

public class User extends Task<Integer> {
    private volatile int id;
    private LocalDate birthDate;
    private String email;
    private String cardNumber;
    private VodSubscription vodSubscription;
    private boolean isPaused;

    public User() {
        this.id = ControlPanel.getNewUserId();
        System.out.println(this.id);
        this.birthDate = createBirthDate();
        this.email = createEmail();
        this.cardNumber = createCardNumber();
        this.vodSubscription = null;
        this.isPaused = false;
    }

    @Override
    protected Integer call() throws Exception {
        int randomMinutes;
        while (!isCancelled() && !Thread.currentThread().isInterrupted()){
            try {
                if (!this.isPaused) {
                    randomMinutes = new Random().nextInt(430)+1; // TODO watching accurately to movie length
                    System.out.println(this.getId() + " Oglądam");
                    //watch();
                    System.out.println(this.getId() + " ide Spać");
                    Thread.sleep(Simulation.simMinutesToRealMillis(randomMinutes)); // TODO: Sleeping accurately to changing speed
                } else {
                    Thread.sleep(Simulation.ONE_TICK);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return 0;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    private LocalDate createBirthDate() {
        int yr = new Random().nextInt(2018-1910) + 1910;
        int day = new Random().nextInt(365) + 1;
        return LocalDate.ofYearDay(yr, day);

    }

    private String createEmail() {
        String email = ControlPanel.getWords().get(new Random().nextInt(ControlPanel.getWords().size()));
        return email + "@mail.com";
    }

    public String getEmail() {
        return this.email;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    private String createCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i=0; i < 5; i++){
            int n = new Random().nextInt(9000) + 1000;
            cardNumber.append(n);
            cardNumber.append(" ");
        }
        return cardNumber.toString();
    }

    public VodSubscription getVodSubscription() {
        return vodSubscription;
    }

    public void setVodSubscription(VodSubscription vodSubscription) {
        this.vodSubscription = vodSubscription;
    }

    public void pauseLoop(){
        this.isPaused = true;
    }

    public void startLoop(){
        this.isPaused = false;
    }

    public void stopLoop(){ this.cancel(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ",\t" + email;
    }
}
