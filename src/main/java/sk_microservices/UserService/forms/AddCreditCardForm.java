package sk_microservices.UserService.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCreditCardForm {

    private String cardName;
    private long cardNumber;
    private int securityCode;

}
