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

    private String cardName;

    private long cardNumber;

    private int securityCode;

    @ManyToOne()
    private User user;

    public CreditCard(String cardName, long cardNumber, int securityCode, User user) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
        this.user = user;
    }

}
