package model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import model.cinematography.CWork;
import model.cinematography.LiveStreaming;
import model.cinematography.Movie;
import model.cinematography.Series;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents User
 */
public class User extends Task<Integer> implements Serializable {
    private ControlPanel cp;
    private volatile int id;
    private LocalDate birthDate;
    private int gender;
    private String email;
    private String cardNumber;
    private VodSubscription vodSubscription;
    private AtomicBoolean paused;
    private boolean completed;

    User() {
        this.cp = ControlPanel.getInstance();
        this.id = cp.getNewUserId();
        this.gender = new Random().nextInt(2);
        this.birthDate = createBirthDate();
        this.email = createEmail();
        this.cardNumber = createCardNumber();
        this.vodSubscription = null;
        this.paused = new AtomicBoolean(false);
        this.completed = false;
    }

    /**
     * Main loop of typical VOD user lifecycle
     */
    @Override
    protected Integer call() throws Exception {
        int randomCWorkId, randomCWorkMinutes;
        CWork randomCWork;
        startLoop();
        while (!isCancelled() && !Thread.currentThread().isInterrupted()){
            try {
                // waiting if paused
                synchronized(this) {
                    while (paused.get()) try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                if (cp.getCWorks().size() > 0) {
                    // System.out.println(this.id); // who is active
                    randomCWorkId = new Random().nextInt(cp.getCWorks().size());
                    randomCWork = cp.getCWorks().get(randomCWorkId);

                    int p = new Random().nextInt(1000);
                    if(this.vodSubscription == null &&  p < 20){ // probability of buying subscription
                        this.vodSubscription = new VodSubscription();
                        Platform.runLater(() ->
                                cp.addMoney(vodSubscription.getPrice()));
                    } else if (this.vodSubscription != null && // checking expiration date of subscription
                            Simulation.getDateTime().toLocalDate().isAfter(this.vodSubscription.getExpirationDate())){
                        this.vodSubscription = null;
                    }

                    // watching CWorks
                    switch (randomCWork.getType()){
                        case "Movie":
                            Movie m = (Movie) randomCWork;
                            randomCWorkMinutes = m.getDuration();
                            m.watch();
                            if(vodSubscription == null)
                                Platform.runLater(() ->
                                        cp.addMoney(m.getSinglePrice()));
                            break;
                        case "LiveStreaming":
                            LiveStreaming l = (LiveStreaming) randomCWork;
                            randomCWorkMinutes = new Random().nextInt(400);
                            l.watch();
                            if(vodSubscription == null)
                                Platform.runLater(() ->
                                        cp.addMoney(l.getSinglePrice()));
                            break;
                        case "Series":
                            Series s = (Series) randomCWork;
                            int a = new Random().nextInt(s.getSeasons().size());
                            int b = new Random().nextInt(s.getSeasons().get(a).getEpisodes().size());
                            randomCWorkMinutes = s.getSeasons().get(a).getEpisodes().get(b).getDuration();
                            s.watch();
                            s.getSeasons().get(a).getEpisodes().get(b).watch();

                            if(vodSubscription == null)
                                Platform.runLater(() ->
                                        cp.addMoney(s.getSeasons().get(a).getEpisodes().get(b).getSinglePrice()));
                            break;
                            default:
                                throw new Exception("Something went wrong");
                    }

                    try {
                        Thread.sleep(Simulation.simMinutesToRealMillis(randomCWorkMinutes));
                        Thread.sleep(Simulation.simMinutesToRealMillis(new Random().nextInt(120)));
                    } catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                } else {
                    Thread.sleep(Simulation.getOneTick());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.completed = true;
        return 0;
    }


    /**
     * @return true if task is completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Pausing loop
     */
    synchronized void pauseLoop(){
        paused.set(true);
    }

    /**
     * Starting loop after pause
     */
    synchronized void startLoop(){
        notify();
        paused.set(false);
    }

    /**
     * @return user ID
     */
    public synchronized int getId() {
        return id;
    }

    /**
     * @param id new user ID
     */
    public synchronized void setId(int id) {
        this.id = id;
    }

    /**
     * @return birth date of user
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Creates new birth date
     * @return birth date of user
     */
    private LocalDate createBirthDate() {
        int yr = new Random().nextInt(2018-1910) + 1910;
        int day = new Random().nextInt(365) + 1;
        return LocalDate.ofYearDay(yr, day);

    }

    /**
     * Creates new email address
     * @return email
     */
    private String createEmail() {
        String email = ControlPanel.getInstance().getWords().get(new Random().nextInt(ControlPanel.getInstance().getWords().size()));
        return email + "@mail.com";
    }

    /**
     * @return email address
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @return users card number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Creates user card number
     * @return card number
     */
    private String createCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i=0; i < 5; i++){
            int n = new Random().nextInt(9000) + 1000;
            cardNumber.append(n);
            cardNumber.append(" ");
        }
        return cardNumber.toString();
    }

    /**
     * @return subscription subscribed by user
     */
    public VodSubscription getVodSubscription() {
        return vodSubscription;
    }

    /**
     * @param vodSubscription new subscription
     */
    public void setVodSubscription(VodSubscription vodSubscription) {
        this.vodSubscription = vodSubscription;
    }

    /**
     * @return user gender
     */
    public int getGender() {
        return gender;
    }

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
