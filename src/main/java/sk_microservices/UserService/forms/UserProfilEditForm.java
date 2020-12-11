package sk_microservices.UserService.forms;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfilEditForm {

    private String ime;
    private String prezime;
    private String email;
    private String password;
    private long brojPasosa;

}
