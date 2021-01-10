package sk_microservices.UserService.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ime;

    private String prezime;

    private long broj;

    private int pin;

    @ManyToOne()
    private User user;

    public CreditCard(String ime, String prezime, long broj, int pin, User user) {
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.pin = pin;
        this.user = user;
    }

}
