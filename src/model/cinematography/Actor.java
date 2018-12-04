package model.cinematography;

import javafx.scene.image.Image;

import java.util.Objects;

public class Actor {
    private String firstname;
    private String surname;
    private Image image;

    public Actor(String firstname, String surname, Image image) {
        this.firstname = firstname;
        this.surname = surname;
        this.image = image;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", image=" + image +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(firstname, actor.firstname) &&
                Objects.equals(surname, actor.surname) &&
                Objects.equals(image, actor.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, surname, image);
    }
}
