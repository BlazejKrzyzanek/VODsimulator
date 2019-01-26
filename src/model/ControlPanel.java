package model;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.cinematography.CWork;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/*
TODO płacenie dystrybutorom (np. na koniec każdego miesiąca)
TODO pobieranie opłat od użytkowników
TODO wyszukiwarka (może osobna klasa do tego)
TODO pobieranie danych o filmach / serialach i live z imdb https://www.imdb.com/interfaces/
TODO sprawdzanie czy biznes się opłaca (można podnieść ceny jeśli się nie będzie opłacać, ale powinno wtedy spaść prawdopodobieństwo oglądania filmu)
TODO Dokumentacja!

FIXME zrobić z tego singleton i poprawić wszystkie błędy  z serializacja zwlaszcza
 */

public class ControlPanel implements Serializable {
    private static ControlPanel INSTANCE;
    private volatile int cWorkId;
    private volatile int userId;
    private volatile int movieSinglePrice;
    private volatile int liveStreamSinglePrice;
    private volatile int seriesSinglePrice;
    private IntegerProperty basicPrice;
    private IntegerProperty familyPrice;
    private IntegerProperty premiumPrice;
    private List<String> names;
    private List<String> distributorNames;
    private List<String> words;
    private List<String> allCountries;
    private List<String> categories;
    private IntegerProperty money;
    private IntegerProperty monthsWithoutProfit;
    private boolean canUserBeAdded;

    private transient volatile ObservableList<Distributor> distributors;
    private transient volatile ObservableList<User> users;
    private transient volatile ObservableList<CWork> cWorks;


    private ControlPanel() {
        cWorkId = 0;
        userId = 0;
        movieSinglePrice = new Random().nextInt(5) + 1;
        seriesSinglePrice = new Random().nextInt(5) + 1;
        liveStreamSinglePrice = new Random().nextInt(2) + 1;

        basicPrice = new SimpleIntegerProperty();
        familyPrice = new SimpleIntegerProperty();
        premiumPrice = new SimpleIntegerProperty();
        money = new SimpleIntegerProperty();
        monthsWithoutProfit = new SimpleIntegerProperty();
        canUserBeAdded = false;

        try {
            names = Files.readAllLines(Paths.get(".", "resources\\text", "names.txt"), Charset.forName("utf-8"));
            distributorNames = Files.readAllLines(Paths.get(".", "resources\\text", "distributorNames.txt"), Charset.forName("utf-8"));
            words = Files.readAllLines(Paths.get(".", "resources\\text", "dict.txt"), Charset.forName("utf-8"));
            allCountries = Files.readAllLines(Paths.get(".", "resources\\text", "countries.txt"), Charset.forName("utf-8"));
            categories = Files.readAllLines(Paths.get(".", "resources\\text", "categories.txt"), Charset.forName("utf-8"));
        } catch (IOException e) {
            System.out.println("File does not exist");
            e.printStackTrace();
        }

        distributors = FXCollections.observableArrayList(new ArrayList<>());
        users = FXCollections.observableArrayList(new ArrayList<>());
        cWorks = FXCollections.observableArrayList(new ArrayList<>());
    }

    public static ControlPanel getInstance(){
        if (INSTANCE == null){
            synchronized (ControlPanel.class){
                if (INSTANCE == null){
                    INSTANCE = new ControlPanel();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized int getNewCWorkId() {
        return cWorkId++;
    }

    public synchronized int getNewUserId() {
        return userId++;
    }

    public void deleteCWork(CWork c){
        this.cWorks.removeIf(cWork -> cWork.equals(c));
    }

    // TODO reset
    public void resetAll(){
        for (Distributor d: distributors){
            d.cancel();
        }
        for (User u: users){
            u.cancel();
        }
        canUserBeAdded = false;
        distributors = FXCollections.observableArrayList(new ArrayList<>());
        users = FXCollections.observableArrayList(new ArrayList<>());
        cWorks = FXCollections.observableArrayList(new ArrayList<>());

        cWorkId = 0;
        userId = 0;
        money.setValue(0);
        monthsWithoutProfit.setValue(0);
    }

    public void pay(){
        for (Distributor d: distributors)
            this.money.setValue(this.money.get() - d.getPayment());
    }

    public void negotiate(){
        for (Distributor d: distributors){
            if(new Random().nextInt(100) < 15){
                d.negotiate();
            }
        }
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getDistributorNames() {
        return distributorNames;
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> getAllCountries() {
        return allCountries;
    }

    public List<String> getCategories() {
        return categories;
    }

    public synchronized int getMovieSinglePrice() {
        return movieSinglePrice;
    }

    public synchronized int getLiveStreamSinglePrice() {
        return liveStreamSinglePrice;
    }

    public synchronized int getSeriesSinglePrice() {
        return seriesSinglePrice;
    }

    public synchronized void setMovieSinglePrice(int moviePrice) {
        movieSinglePrice = moviePrice;
    }

    public synchronized void setLiveStreamSinglePrice(int liveStreamPrice) {
        liveStreamSinglePrice = liveStreamPrice;
    }

    public synchronized void setSeriesSinglePrice(int seriesPrice) {
        seriesSinglePrice = seriesPrice;
    }

    public synchronized ObservableList<Distributor> getDistributors() {
        return distributors;
    }

    public synchronized void addDistributor(){
        Distributor dist = new Distributor();
        while(this.distributors.contains(dist)){ dist = new Distributor(); }

        Thread th = new Thread(dist);
        th.setDaemon(true);
        th.start();
        this.distributors.add(dist);
        this.cWorks.addAll(dist.getCWorks());
    }

    public synchronized void addUser(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    User u = new User();
                    Thread th = new Thread(u);
                    th.setDaemon(true);
                    th.start();
                    if (!users.contains(u)) {
                        users.add(u);
                    }
                }
            });
            canUserBeAdded = false;
    }

    public synchronized ObservableList<User> getUsers() {
        return users;
    }

    public synchronized void addCWork(CWork cWork){
        this.cWorks.add(cWork);
        this.canUserBeAdded = true;
    }

    public ObservableList<CWork> getCWorks() {
        return cWorks;
    }

    public synchronized void write() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream("save.b")));
        
//        out.writeObject(names);
//        out.writeObject(cWorkId);
//        out.writeObject(userId);
//        out.writeObject(movieSinglePrice);
//        out.writeObject(liveStreamSinglePrice);
//        out.writeObject(seriesSinglePrice);
//        out.writeObject(names);
//        out.writeObject(distributorNames);
//        out.writeObject(words);
//        out.writeObject(allCountries);
//        out.writeObject(categories);
//        out.writeObject(money);
//        out.writeObject(monthsWithoutProfit);
//        out.writeObject(canUserBeAdded);

        for (Distributor d: distributors){
            d.cancel();
        }
        out.writeObject(new ArrayList<>(distributors));

        for (User u: users){
            u.cancel();
        }
        out.writeObject(new ArrayList<>(users));
        out.writeObject(new ArrayList<>(cWorks));
        out.close();
    }

    public void read() throws IOException, ClassNotFoundException { // FIXME Brak zapisanego pliku exception, uruchomienie po wczytaniu  
        ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream("save.b")));
        

        distributors = FXCollections.observableArrayList((ArrayList<Distributor>) in.readObject());
        users = FXCollections.observableArrayList((ArrayList<User>) in.readObject());
        cWorks = FXCollections.observableArrayList((ArrayList<CWork>) in.readObject());

        for (Distributor d: distributors){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Thread th = new Thread(d);
                    th.setDaemon(true);
                    th.start();
                }
            });
        }

        for (User u: users){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Thread th = new Thread(u);
                    th.setDaemon(true);
                    th.start();
                }
            });
        }

        in.close();
    }

    public boolean isCanUserBeAdded() {
        return canUserBeAdded;
    }

    public IntegerProperty getMoney() {
        return money;
    }

    public void addMoney(int m){
        money.setValue(money.get() + m);
    }

    public List<CWork> search(String pattern){
        return null;
    }

    public IntegerProperty getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(int basicPrice) {
        this.basicPrice.setValue(basicPrice);
    }

    public IntegerProperty getFamilyPrice() {
        return familyPrice;
    }

    public void setFamilyPrice(int familyPrice) {
        this.familyPrice.setValue(familyPrice);
    }

    public IntegerProperty getPremiumPrice() {
        return premiumPrice;
    }

    public void setPremiumPrice(int premiumPrice) {
        this.premiumPrice.setValue(premiumPrice);
    }

    public IntegerProperty getMonthsWithoutProfit() {
        return monthsWithoutProfit;
    }

    public void setMonthsWithoutProfit(int monthsWithoutProfit) {
        this.monthsWithoutProfit.set(monthsWithoutProfit);
    }
}
