package sk_microservices.UserService.forms;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfilEditForm {

    private String ime;
    private String prezime;
    private String email;
    private String password;
    private long brojPasosa;

}
