package model.cinematography;

import javafx.scene.image.Image;
import model.ControlPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represents any kind of cinematographic work
 */
public class CWork {

    private String category;
    private final int id;
    private Image posterImage;
    private String title;
    private String description;
    private List<String> countryList;
    private List<Integer> productionYearList;
    private float userRating;
    Random r;

    /**
     * Creates new CWork with fixed id and random parameters, using words from appropriate lists
     * @param id individual id
     */
    public CWork(int id){
        this.r = new Random();
        this.id = id;
        this.category = createCategory();
        this.posterImage = createPoster();
        this.title = createText(3,5);
        this.description = createText(20,50);
        this.countryList = createCountryList(1, 5);
        this.productionYearList = createProductionYearList(1,6);
        this.userRating = r.nextFloat() * (10);
    }


    /**
     * Creates random category based on the given list
     * @return random category name
     */
    private String createCategory() {
        int i = r.nextInt(ControlPanel.getCategories().size());
        return ControlPanel.getCategories().get(i);
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
            idx = r.nextInt(ControlPanel.getAllCountries().size());
            countryList.add(ControlPanel.getAllCountries().get(idx));
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
        int wordIdx = r.nextInt(ControlPanel.getWords().size());

        text.append(ControlPanel.getWords().get(wordIdx).substring(0, 1).toUpperCase());
        for (int i=0; i<length; i++){
            wordIdx = r.nextInt(ControlPanel.getWords().size());
            text.append(ControlPanel.getWords().get(wordIdx));
            if (i % 10 == 9){
                text.append(".\n");
            } else {
                text.append(" ");
            }
        }
        return text.toString();
    }

    // TODO better image choosing, now it is not safe
    /**
     * Creates a poster image
     * @return random poster image
     */
    private Image createPoster(){
        Integer randPoster = new Random().nextInt(7);
        String posterUrl = "/img/" + randPoster.toString() + ".jpg";
        Image image;
        // image = new Image(posterUrl); // FIXME throws Exception? Ask someone, it executes forever
        long end = System.currentTimeMillis();
        return null;
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
    public float getUserRating() {
        return userRating;
    }

    /**
     * Sets a user rating
     * @param userRating new rating
     */
    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    /**
     * @return fixed id of CWork
     */
    public int getId() {
        return id;
    }

    /**
     * @return poster image
     */
    public Image getPosterImage() {
        return posterImage;
    }

    /**
     * Sets a poster image
     * @param posterImage new image
     */
    public void setPosterImage(Image posterImage) {
        this.posterImage = posterImage;
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
