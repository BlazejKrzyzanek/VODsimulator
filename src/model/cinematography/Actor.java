package model.cinematography;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Actor implements Serializable {
    private String firstname;
    private String surname;

    public Actor() {
        ArrayList<String> firstnames = new ArrayList<>(Arrays.asList("John", "Samuel", "Forrest", "Magda", "Leonardo",
                "Piotr", "Adam", "Michael", "Geralt", "Jack", "Tom", "Donald", "Barack", "George", "Anna", "Anja",
                "Kinga", "Patricia", "Hannah", "Alex", "Max", "Julie", "Romeo", "Ted", "Zac", "Brad", "Robert",
                "Jake", "Will", "Liam"));
        ArrayList<String> surnames = new ArrayList<>(Arrays.asList("Depp", "Gump", "Trump", "Obama", "Efron", "di Caprio",
                "z Rivii", "Gessler", "Rusin", "Pazura", "Bundy", "Pit", "Vat", "Adams Jr.", "Montana", "Nana", "Rubjik",
                "Sparrow", "Apple", "Zuckerberg", "Jobs", "Works", "Gates", "Cage", "Gan", "Mad", "Edison", "Bezos", "Corn",
                "Screen", "Mandela", "Duck", "Tusk", "Wham", "Atlanta", "Fiorello", "Flat", "Tuner"));
        this.firstname = firstnames.get(new Random().nextInt(firstnames.size()));
        this.surname = surnames.get(new Random().nextInt(surnames.size()));
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    @Override
    public String toString() {
        return firstname  + " " + surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(firstname, actor.firstname) &&
                Objects.equals(surname, actor.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, surname);
    }
}
