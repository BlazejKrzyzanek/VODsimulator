package model;

import model.cinematography.CWork;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*
TODO obsługa symulacji
TODO płacenie dystrybutorom (np. na koniec każdego miesiąca)
TODO pobieranie opłat od użytkowników
TODO wyszukiwarka (może osobna klasa do tego)
TODO pobieranie danych o filmach / serialach i live z imdb https://www.imdb.com/interfaces/
TODO sprawdzanie czy biznes się opłaca (można podnieść ceny jeśli się nie będzie opłacać, ale powinno wtedy spaść prawdopodobieństwo oglądania filmu)
TODO Dokumentacja!
 */

public abstract class ControlPanel implements Serializable {
    private static volatile int cWorkId;
    private static volatile int userId;
    private static volatile int movieSinglePrice;
    private static volatile int liveStreamSinglePrice;
    private static volatile int seriesSinglePrice;
    private static List<String> names;
    private static List<String> distributorNames;
    private static List<String> words;
    private static List<String> allCountries;
    private static List<String> categories;
    private static int money;
    private static int monthsWithoutProfit;


    static {
        cWorkId = 0;
        userId = 0;
        movieSinglePrice = new Random().nextInt(10000) + 10;
        seriesSinglePrice = new Random().nextInt(10000) + 10;
        liveStreamSinglePrice = new Random().nextInt(10000) + 10;
        money = 0;
        monthsWithoutProfit = 0;

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
    }


    public static synchronized int getNewCWorkId() {
        return cWorkId++;
    }
    
    public static synchronized int getNewUserId() {
        return userId++;
    }



    // TODO reset
    public static void resetAll(){
        cWorkId = 0;
        userId = 0;
        money = 0;
        monthsWithoutProfit = 0;
    }

    public static void pay(){
    }

    public static List<String> getNames() {
        return names;
    }

    public static List<String> getDistributorNames() {
        return distributorNames;
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

    public static synchronized int getMovieSinglePrice() {
        return movieSinglePrice;
    }

    public static synchronized int getLiveStreamSinglePrice() {
        return liveStreamSinglePrice;
    }

    public static synchronized int getSeriesSinglePrice() {
        return seriesSinglePrice;
    }

    public static synchronized void setMovieSinglePrice(int moviePrice) {
        movieSinglePrice = moviePrice;
    }

    public static synchronized void setLiveStreamSinglePrice(int liveStreamPrice) {
        liveStreamSinglePrice = liveStreamPrice;
    }

    public static synchronized void setSeriesSinglePrice(int seriesPrice) {
        seriesSinglePrice = seriesPrice;
    }


    public static List<CWork> search(String pattern){
        return null;
    }
}
