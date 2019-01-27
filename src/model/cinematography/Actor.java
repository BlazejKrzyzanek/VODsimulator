package model.cinematography;

import model.ControlPanel;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * Random actor class
 */
public class Actor implements Serializable {
    private String firstname;
    private String surname;

    Actor() {
        this.firstname = ControlPanel.getInstance().getNames().get(
                new Random().nextInt(ControlPanel.getInstance().getNames().size()));
        this.surname = ControlPanel.getInstance().getWords().get(
                new Random().nextInt(ControlPanel.getInstance().getWords().size()));
        this.surname = surname.substring(0, 1).toUpperCase() + surname.substring(1);
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
