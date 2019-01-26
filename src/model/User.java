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

/*
TODO Indywidualne ID dla każdego (może static albo coś?)
TODO Opłacanie abonamentu
TODO Losowe kupowanie filmu/serialu/live
TODO Dokumentacja!
 */

public class User extends Task<Integer> implements Serializable {
    private ControlPanel cp;
    private volatile int id;
    private LocalDate birthDate;
    private int genre;
    private String email;
    private String cardNumber;
    private VodSubscription vodSubscription;
    private AtomicBoolean paused;

    public User() {
        this.cp = ControlPanel.getInstance();
        this.id = cp.getNewUserId();
        this.genre = new Random().nextInt(2);
        this.birthDate = createBirthDate();
        this.email = createEmail();
        this.cardNumber = createCardNumber();
        this.vodSubscription = null;
        this.paused = new AtomicBoolean(false);
    }

    @Override
    protected Integer call() throws Exception {
        int randomCWorkId, randomCWorkMinutes;
        CWork randomCWork;
        startLoop();
        while (!isCancelled() && !Thread.currentThread().isInterrupted()){
            try {
                synchronized(this) {
                    while (paused.get()) try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                if (cp.getCWorks().size() > 0) {
                    System.out.println(this.id);
                    randomCWorkId = new Random().nextInt(cp.getCWorks().size());
                    randomCWork = cp.getCWorks().get(randomCWorkId);
                    int p = new Random().nextInt(1000);
                    if(this.vodSubscription == null &&  p < 3){
                        this.vodSubscription = new VodSubscription();
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                cp.addMoney(vodSubscription.getPrice());
                            }
                        });
                    } else if (this.vodSubscription != null &&
                            Simulation.getDateTime().toLocalDate().isAfter(this.vodSubscription.getExpirationDate())){
                        this.vodSubscription = null;
                    }
                    switch (randomCWork.getType()){
                        case "Movie":
                            Movie m = (Movie) randomCWork;
                            randomCWorkMinutes = m.getDuration();
                            m.watch();
                            if(vodSubscription == null)
                                Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        cp.addMoney(m.getSinglePrice());
                                    }
                                });
                            break;
                        case "LiveStreaming":
                            LiveStreaming l = (LiveStreaming) randomCWork;
                            randomCWorkMinutes = new Random().nextInt(400);
                            l.watch();
                            if(vodSubscription == null)
                                Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        cp.addMoney(l.getSinglePrice());
                                    }
                                });
                            break;
                        case "Series":
                            Series s = (Series) randomCWork;
                            int a = new Random().nextInt(s.getSeasons().size());
                            int b = new Random().nextInt(s.getSeasons().get(a).getEpisodes().size());
                            randomCWorkMinutes = s.getSeasons().get(a).getEpisodes().get(b).getDuration();
                            s.watch();
                            s.getSeasons().get(a).getEpisodes().get(b).watch();

                            if(vodSubscription == null)
                                Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        cp.addMoney(s.getSeasons().get(a).getEpisodes().get(b).getSinglePrice());
                                    }
                                });
                            break;
                            default:
                                throw new Exception("Something went wrong");
                    }
                    Thread.sleep(Simulation.simMinutesToRealMillis(randomCWorkMinutes));
                } else {
                    Thread.sleep(Simulation.getOneTick());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return 0;
    }


    public synchronized void pauseLoop(){
        paused.set(true);
    }

    public synchronized void startLoop(){
        notify();
        paused.set(false);
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
        String email = ControlPanel.getInstance().getWords().get(new Random().nextInt(ControlPanel.getInstance().getWords().size()));
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

    public int getGenre() {
        return genre;
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
