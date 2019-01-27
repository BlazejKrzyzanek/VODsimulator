package model;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.cinematography.CWork;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Thread.sleep;

/*
TODO płacenie dystrybutorom (np. na koniec każdego miesiąca)
TODO pobieranie opłat od użytkowników
TODO wyszukiwarka (może osobna klasa do tego)
TODO pobieranie danych o filmach / serialach i live z imdb https://www.imdb.com/interfaces/
TODO sprawdzanie czy biznes się opłaca (można podnieść ceny jeśli się nie będzie opłacać, ale powinno wtedy spaść prawdopodobieństwo oglądania filmu)
TODO Dokumentacja!

FIXME zrobić z tego singleton i poprawić wszystkie błędy  z serializacja zwlaszcza
 */

/**
 * Singleton class which controls simulation objects
 */
public class ControlPanel implements Serializable {
    private static ControlPanel INSTANCE;
    private volatile int cWorkId;
    private volatile int userId;
    private volatile int singlePrice;
    private volatile int basicPrice;
    private volatile int familyPrice;
    private volatile int premiumPrice;
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
        singlePrice = 0;

        basicPrice = 0;
        familyPrice = 0;
        premiumPrice = 0;
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

    /**
     * @return instance of ControlPanel
     */
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

    /**
     * @return new unique ID of CWork
     */
    public synchronized int getNewCWorkId() {
        return cWorkId++;
    }

    /**
     * @return new unique User ID
     */
    synchronized int getNewUserId() {
        return userId++;
    }

    /**
     * @param c CWork to delete
     */
    void deleteCWork(CWork c){
        this.cWorks.removeIf(cWork -> cWork.equals(c));
    }

    /**
     * Sets all fields to default
     * FIXME: Lepsze zatrzymywanie wątków
     */
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

    /**
     * Paying distributors
     */
    public void pay(){
        for (Distributor d: distributors)
            this.money.setValue(this.money.get() - d.getPayment());
    }

    /**
     * Negotiating new payment with distributors
     */
    public void negotiate(){
        for (Distributor d: distributors){
            if(new Random().nextInt(100) < 15){
                d.negotiate();
            }
        }
    }

    /**
     * @return list of people first names
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * @return list of distributor names
     */
    List<String> getDistributorNames() {
        return distributorNames;
    }

    /**
     * @return list of all english words
     */
    public List<String> getWords() {
        return words;
    }

    /**
     * @return list of all countries names
     */
    public List<String> getAllCountries() {
        return allCountries;
    }

    /**
     * @return list of CWork categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * @param price new single price
     */
    public void setSinglePrice(int price){
        this.singlePrice = price;
    }

    /**
     * @return single price
     */
    public int getSinglePrice() {
        return singlePrice;
    }

    /**
     * @return Observable list of distributors
     */
    public synchronized ObservableList<Distributor> getDistributors() {
        return distributors;
    }

    /**
     * Adds new running distributor to list
     */
    public synchronized void addDistributor(){
        Distributor dist = new Distributor();
        while(this.distributors.contains(dist)){ dist = new Distributor(); }
        Thread th = new Thread(dist);
        th.setDaemon(true);
        th.start();
        this.distributors.add(dist);
        this.cWorks.addAll(dist.getCWorks());
    }

    /**
     * Adds new running user to list
     */
    public synchronized void addUser(){
            Platform.runLater(() -> {
                User u = new User();
                Thread th = new Thread(u);
                th.setDaemon(true);
                th.start();
                if (!users.contains(u)) {
                    users.add(u);
                }
            });
            canUserBeAdded = false;
    }

    /**
     * @return Observable list of users
     */
    public synchronized ObservableList<User> getUsers() {
        return users;
    }

    /**
     * @param cWork new CWork to add
     */
    synchronized void addCWork(CWork cWork){
        this.cWorks.add(cWork);
        this.canUserBeAdded = true;
    }

    /**
     * @return Observable list of CWorks
     */
    public ObservableList<CWork> getCWorks() {
        return cWorks;
    }

    /**
     * Writing actual simulation state to file.
     * FIXME Obiekty Observable sie nie serializuja, do poprawienia
     * @throws IOException
     */
    public synchronized void write() throws IOException {
//        ObjectOutputStream out = new ObjectOutputStream(
//                new BufferedOutputStream(
//                        new FileOutputStream("save.b")));
//
//        out.writeObject(cWorkId);
//        out.writeObject(userId);
//        out.writeObject(singlePrice);
//        out.writeObject(basicPrice);
//        out.writeObject(familyPrice);
//        out.writeObject(premiumPrice);
//        out.writeObject(names);
//        out.writeObject(distributorNames);
//        out.writeObject(words);
//        out.writeObject(allCountries);
//        out.writeObject(categories);
//        out.writeObject(money.get());
//        out.writeObject(monthsWithoutProfit.get());
//        out.writeObject(canUserBeAdded);
//
////        out.writeObject(new ArrayList<Distributor>(distributors).toString());
////        out.writeObject(new ArrayList<User>(users).toString());
////        out.writeObject(new ArrayList<CWork>(cWorks).toString());
//        out.close();
    }

    /**
     * Read simulation state from save file
     * FIXME: trzeba zaczac od naprawienia zapisu
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void read() throws IOException, ClassNotFoundException {
//        ObjectInputStream in = new ObjectInputStream(
//                new BufferedInputStream(
//                        new FileInputStream("save.b")));
//
//
//        distributors = FXCollections.observableArrayList((ArrayList<Distributor>) in.readObject());
//        users = FXCollections.observableArrayList((ArrayList<User>) in.readObject());
//        cWorks = FXCollections.observableArrayList((ArrayList<CWork>) in.readObject());
//
//        for (Distributor d: distributors){
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    Thread th = new Thread(d);
//                    th.setDaemon(true);
//                    th.start();
//                }
//            });
//        }
//
//        for (User u: users){
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    Thread th = new Thread(u);
//                    th.setDaemon(true);
//                    th.start();
//                }
//            });
//        }
//
//        in.close();
    }

    /**
     * @return flag is true, when new user can be added to simulation
     */
    boolean isCanUserBeAdded() {
        return canUserBeAdded;
    }

    /**
     * @return ControlPanel money
     */
    public IntegerProperty getMoney() {
        return money;
    }

    /**
     * @param money how much money should the ControlPanel have
     */
    public void setMoney(int money) {
        this.money.set(money);
    }

    /**
     * Adds money to ControlPanel
     * @param m money to add
     */
    void addMoney(int m){
        money.setValue(money.get() + m);
    }

    /**
     * @return price of basic subscription
     */
    int getBasicPrice() {
        return basicPrice;
    }

    /**
     * @param basicPrice price of basic subscription
     */
    public void setBasicPrice(int basicPrice) {
        this.basicPrice = basicPrice;
    }

    /**
     * @return price of family subscription
     */
    int getFamilyPrice() {
        return familyPrice;
    }

    /**
     * @param familyPrice price of family subscription
     */
    public void setFamilyPrice(int familyPrice) {
        this.familyPrice = familyPrice;
    }

    /**
     * @return price of premium subscription
     */
    int getPremiumPrice() {
        return premiumPrice;
    }

    /**
     * @param premiumPrice price of premium subscription
     */
    public void setPremiumPrice(int premiumPrice) {
        this.premiumPrice = premiumPrice;
    }

    /**
     * @return how many months money is under 0
     */
    public IntegerProperty getMonthsWithoutProfit() {
        return monthsWithoutProfit;
    }

    /**
     * @param monthsWithoutProfit how many months money is under 0
     */
    void setMonthsWithoutProfit(int monthsWithoutProfit) {
        this.monthsWithoutProfit.set(monthsWithoutProfit);
    }
}
