package model;

import model.cinematography.CWork;

import java.sql.Time;
import java.util.List;
import java.util.TreeMap;

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
    private Time simulationTime;
    private int money;
    private boolean hasProfit;
    private TreeMap<String, Distributor> distributors;
    private TreeMap<String, User> users;
    private TreeMap<String, CWork> CWorks;

    public void runSimulation(){
    }

    public void pauseSimulation(){
    }

    public void resetSimulation(){
    }

    public void pay(){
    }

    public List<CWork> search(String pattern){
        List<CWork> result = null;
        return result;
    }
}
