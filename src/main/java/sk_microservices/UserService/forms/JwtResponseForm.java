package sk_microservices.UserService.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponseForm {

    private String token;
    private String type = "Basic";
    private Long id;
    private String ime;
    private String prezime;
    private String email;

    public JwtResponseForm(String token, Long id, String ime, String prezime, String email) {
        this.token = token;
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
    }
}
