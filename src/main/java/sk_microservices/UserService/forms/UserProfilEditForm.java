package sk_microservices.UserService.forms;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk_microservices.UserService.entities.enums.Role;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfilEditForm {

    private String ime;
    private String prezime;
    private String email;
    private long brojPasosa;
    private String role;
    private List<AddCreditCardForm> cards;
}
