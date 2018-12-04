package model;

import java.util.Calendar;
import java.util.Objects;

/*
TODO Indywidualne ID dla każdego (może static albo coś?)
TODO Opłacanie abonamentu
TODO Losowe kupowanie filmu/serialu/live
TODO Dokumentacja!
 */

public class User {
    private int id;
    private Calendar birthDate;
    private String email;
    private String cardNumber;
    private VodSubscription vodSubscription;

    public User(int id, Calendar birthDate, String email, String cardNumber, VodSubscription vodSubscription) {
        this.id = id;
        this.birthDate = birthDate;
        this.email = email;
        this.cardNumber = cardNumber;
        this.vodSubscription = vodSubscription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public VodSubscription getVodSubscription() {
        return vodSubscription;
    }

    public void setVodSubscription(VodSubscription vodSubscription) {
        this.vodSubscription = vodSubscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
