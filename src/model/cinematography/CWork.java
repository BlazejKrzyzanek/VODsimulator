package model.cinematography;

import model.ControlPanel;
import model.Distributor;
import model.Simulation;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

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
    private LocalDate releaseDate;
    private List<Actor> actorList;
    private int userRating;

    private int singlePrice;
    private Promotion promotion;


    private Map<LocalDate, Integer> audienceMap;
    protected Random r;

    /**
     * Creates new CWork with random parameters, using words from appropriate lists
     * @param distributor creator
     */
    public CWork(Distributor distributor){
        this(distributor, ControlPanel.getInstance().getNewCWorkId());
    }

    /**
     * Creates new CWork with specified ID number, can be used as another CWork within more general one
     * (ex. episode of series)
     * @param distributor creator
     * @param id
     */
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
        this.releaseDate = createReleaseDate(1980, 2020);
        this.actorList = new ArrayList<>();
        for (int i=0; i<new Random().nextInt(10) + 1; i++) actorList.add(new Actor());
        this.userRating = r.nextInt(10) + 1;
        this.singlePrice = ControlPanel.getInstance().getSinglePrice();
        this.audienceMap = new TreeMap<>();
        this.promotion = null;
    }

    /**
     * @return ID of poster image
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * @return creator (Distributor)
     */
    public Distributor getDistributor(){
        return distributor;
    }

    /**
     * Note that someone watched CWork at given date
     */
    public void watch(){
        LocalDate date = Simulation.getDateTime().toLocalDate();
        this.audienceMap.merge(date, 1, (a, b) -> a + b);
    }

    /**
     * @return Map of audience (how many users watched CWork at given date)
     */
    public Map<LocalDate, Integer> getAudienceMap(){
        return audienceMap;
    }

    /**
     * @return list of actors (Stars) playing in CWork
     */
    public List<Actor> getActorList(){
        return actorList;
    }

    /**
     * @return price for single watching
     */
    public int getSinglePrice() {
        if (promotion != null && Simulation.getDateTime().isAfter(promotion.getExpirationDateTime())){
            promotion = null;
            return singlePrice;
        } else if(promotion != null) {
            return (int) Math.round(singlePrice * (1 - promotion.getDiscountPercent() / 100.0));
        } else {
            return singlePrice;
        }
    }

    /**
     * Sets price for single watching
     * @param singlePrice price in $
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

        text.append(ControlPanel.getInstance().getWords().get(wordIdx));
        for (int i=0; i<length; i++){
            wordIdx = r.nextInt(ControlPanel.getInstance().getWords().size());
            text.append(ControlPanel.getInstance().getWords().get(wordIdx));
            if (i % 10 == 9){
                text.append(". ");
            } else {
                text.append(" ");
            }
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    /**
     * Creates random date of release between years
     * @param minY minimum year
     * @param maxY maximum year
     * @return release date
     */
    protected LocalDate createReleaseDate(int minY, int maxY) {
        int year = r.nextInt((maxY - minY + 1)) + minY;
        int bound = 365;
        if (year%400==0 || year%4==0 && year%100!=0) bound = 366;
        int day = r.nextInt(bound)+1;
        return LocalDate.ofYearDay(year, day);
    }

    /**
     * @return CWork title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return type of CWork
     */
    public String getType() {
        return type;
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
     * @return release date
     */
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate date of release episode
     */
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return fixed id of CWork
     */
    public int getId() {
        return id;
    }

    /**
     * delete this CWork
     */
    public void delete(){
        distributor.deleteCWork(this);
    }

    /**
     * @return promotion, null if does not exist
     */
    public Promotion getPromotion() {
        return promotion;
    }

    /**
     * Sets the promotion
     * @param promotion new promotion, null to remove
     */
    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    /**
     * Begins new random promotion
     */
    public void startPromotion() {
        this.promotion = new Promotion();
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
