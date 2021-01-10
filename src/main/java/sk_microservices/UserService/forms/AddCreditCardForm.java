package sk_microservices.UserService.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddCreditCardForm {

    private String ime;
    private String prezime;
    private long broj;
    private int pin;

}
