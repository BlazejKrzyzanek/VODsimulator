package model;

import model.cinematography.CWork;
import model.cinematography.LiveStreaming;
import model.cinematography.Movie;
import model.cinematography.Series;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

/*
TODO obsługa symulacji
TODO płacenie dystrybutorom (np. na koniec każdego miesiąca)
TODO pobieranie opłat od użytkowników
TODO wyszukiwarka (może osobna klasa do tego)
TODO pobieranie danych o filmach / serialach i live z imdb https://www.imdb.com/interfaces/
TODO sprawdzanie czy biznes się opłaca (można podnieść ceny jeśli się nie będzie opłacać, ale powinno wtedy spaść prawdopodobieństwo oglądania filmu)
TODO Dokumentacja!
 */

public class ControlPanel {
    private int money;
    private boolean hasProfit;
    private TreeMap<String, Distributor> distributors;
    private TreeMap<String, User> users;
    private TreeMap<String, CWork> CWorks;
    private static List<String> names;
    private static List<String> words;
    private static List<String> allCountries;
    private static List<String> categories;
    private static LocalDateTime simulationDateTime;
    private int speedMultiplier; // 60000: 1 ms = 1 min
    private VariableSpeedClock clk;
    protected final Logger log = Logger.getLogger(getClass().getName());

    static {
        simulationDateTime = LocalDateTime.now();
        try {
            names = Files.readAllLines(Paths.get(".", "resources\\text", "names.txt"), Charset.forName("utf-8"));
            words = Files.readAllLines(Paths.get(".", "resources\\text", "dict.txt"), Charset.forName("utf-8"));
            allCountries = Files.readAllLines(Paths.get(".", "resources\\text", "countries.txt"), Charset.forName("utf-8"));
            categories = Files.readAllLines(Paths.get(".", "resources\\text", "categories.txt"), Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ControlPanel(int money, boolean hasProfit, int speedMultiplier){
        this.money = money;
        this.hasProfit = hasProfit;
        this.speedMultiplier = speedMultiplier;
        this.clk = new VariableSpeedClock(this.speedMultiplier);
    }

    void runSimulation() throws InterruptedException {
        System.out.println((char)27 + "[33m\tSTART\n" +(char)27 +"[0m");
        long st = System.currentTimeMillis();


        long end = System.currentTimeMillis();
        System.out.println((char)27 + "[36m\tEND\nTIME: " + ((end-st)) + "ms");
    }

    public void pauseSimulation(){
    }

    public void resetSimulation(){
    }

    public void pay(){
    }

    public static List<String> getNames() {
        return names;
    }

    public static List<String> getWords() {
        return words;
    }

    public static List<String> getAllCountries() {
        return allCountries;
    }

    public static List<String> getCategories() {
        return categories;
    }

    public static LocalDateTime getSimulationDateTime() {
        return simulationDateTime;
    }

    public List<CWork> search(String pattern){
        return null;
    }
}
