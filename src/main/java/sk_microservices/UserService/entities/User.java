package sk_microservices.UserService.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sk_microservices.UserService.entities.enums.Rank;
import sk_microservices.UserService.entities.enums.Role;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ime;

    private String prezime;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Rank runk;

    private long brojPasosa;

    private int brojMilja;

    @OneToMany(mappedBy = "user")
    private List<CreditCard> creditCards;

    public User(String ime, String prezime, String email, String password, long brojPasosa) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.password = password;
        this.brojPasosa = brojPasosa;
    }

}
