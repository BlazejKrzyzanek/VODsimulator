package model.cinematography;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import model.ControlPanel;
import model.Distributor;
import model.Simulation;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents any kind of cinematographic work
 */
public abstract class CWork implements Serializable {

    private String type;
    private String title;

    private Distributor distributor;
    private String category;
    private volatile int id;
    private int imageId;
    private String description;
    private List<String> countryList;
    private List<Integer> productionYearList;
    private List<Actor> actorList;
    private int userRating;

    private int singlePrice;

    private Map<LocalDate, Integer> audienceMap;
    Random r;

    /**
     * Creates new CWork with random parameters, using words from appropriate lists
     */
    public CWork(Distributor distributor){
        this(distributor, ControlPanel.getInstance().getNewCWorkId());
    }

    public CWork(Distributor distributor, int id){
        this.distributor = distributor;
        this.type = this.getClass().getSimpleName();
        this.r = new Random();
        synchronized (this) {
            this.id = id;
        }
        this.imageId = new Random().nextInt(17);
        this.category = createCategory();
        this.title = createText(3,5);
        this.description = createText(20,50);
        this.countryList = createCountryList(1, 5);
        this.productionYearList = createProductionYearList(1,6);
        this.actorList = new ArrayList<>();
        for (int i=0; i<new Random().nextInt(10) + 1; i++) actorList.add(new Actor());
        this.userRating = r.nextInt(10) + 1;
        this.singlePrice = ControlPanel.getInstance().getMovieSinglePrice(); // between 0.1$ and 100$

        this.audienceMap = new TreeMap<>();
    }

    public int getImageId() {
        return imageId;
    }

    public Distributor getDistributor(){
        return distributor;
    }


    public void watch(){
        LocalDate date = Simulation.getDateTime().toLocalDate();
        this.audienceMap.merge(date, 1, (a, b) -> a + b);
    }

    public Map<LocalDate, Integer> getAudienceMap(){
        return audienceMap;
    }

    public List<Actor> getActorList(){
        return actorList;
    }

    /**
     * @return price for the option of watching through availableTime (cents)
     */
    public int getSinglePrice() {
        return singlePrice;
    }

    /**
     * Sets price for the option of watching through availableTime (cents)
     * @param singlePrice price (cents)
     */
    public void setSinglePrice(int singlePrice) {
        this.singlePrice = singlePrice;
    }


    /**
     * Creates random category based on the given list
     * @return random category name
     */
    private String createCategory() {
        int i = r.nextInt(ControlPanel.getInstance().getCategories().size());
        return ControlPanel.getInstance().getCategories().get(i);
    }

    /**
     * @return category name
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category name
     * @param category new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Creates a random list of the consecutive years of production
     * @param minLen minimal list length
     * @param maxLen maximal list length
     * @return list of years in which the CWork was created
     */
    private ArrayList<Integer> createProductionYearList(int minLen, int maxLen) {
        ArrayList<Integer> productionYearList = new ArrayList<>();
        int length = r.nextInt((maxLen - minLen) + 1) + minLen;
        int firstYear = r.nextInt((2020 - 1895) + 1) + 1895;
        for (int i=0; i<length; i++) productionYearList.add(firstYear+i);
        return productionYearList;
    }

    /**
     * Creates a random list of countries in which the CWork was created based on those given in the list "allCountries"
     * @param minLen minimal list length
     * @param maxLen maximal list length
     * @return list of countries in which the CWork was created
     */
    private ArrayList<String> createCountryList(int minLen, int maxLen) {
        ArrayList<String> countryList = new ArrayList<>();
        int length = r.nextInt((maxLen - minLen) + 1) + minLen;
        int idx;
        for(int i=0; i<length; i++) {
            idx = r.nextInt(ControlPanel.getInstance().getAllCountries().size());
            countryList.add(ControlPanel.getInstance().getAllCountries().get(idx));
        }
        return countryList;
    }

    /**
     * Creates random text based on words given in the list "words"
     * @param minLen minimal text length (in words)
     * @param maxLen maximal text length (in words)
     * @return random text
     */
    private String createText(int minLen, int maxLen) {
        int length = r.nextInt((maxLen - minLen) + 1) + minLen - 1;
        StringBuilder text = new StringBuilder();
        int wordIdx = r.nextInt(ControlPanel.getInstance().getWords().size());

        text.append(ControlPanel.getInstance().getWords().get(wordIdx).substring(0, 1).toUpperCase());
        for (int i=0; i<length; i++){
            wordIdx = r.nextInt(ControlPanel.getInstance().getWords().size());
            text.append(ControlPanel.getInstance().getWords().get(wordIdx));
            if (i % 10 == 9){
                text.append(". ");
            } else {
                text.append(" ");
            }
        }
        return text.toString();
    }
    /**
     * @return CWork title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets a title
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return CWork description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a description of CWork
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return list of countries in which the CWork was created
     */
    public List<String> getCountryList() {
        return countryList;
    }

    /**
     * sets new list of countries in which the CWork was created
     * @param countryList new list
     */
    public void setCountryList(List<String> countryList) {
        this.countryList.clear();
        this.countryList.addAll(countryList);
    }

    /**
     * @return list of years in which the CWork was created
     */
    public List<Integer> getProductionYearList() {
        return productionYearList;
    }

    /**
     * Sets list of years in which the CWork was created
     * @param productionYearList new list
     */
    public void setProductionYearList(List<Integer> productionYearList) {
        this.productionYearList.clear();
        this.productionYearList.addAll(productionYearList);
    }

    /**
     * @return rating of users
     */
    public int getUserRating() {
        return userRating;
    }

    /**
     * Sets a user rating
     * @param userRating new rating
     */
    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    /**
     * @return fixed id of CWork
     */
    public int getId() {
        return id;
    }

    public void delete(){
        distributor.deleteCWork(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CWork cWork = (CWork) o;
        return Objects.equals(id, cWork.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
