package sk_microservices.UserService.forms.flightService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddAirplaneForm {

    private String naziv;
    private int kapacitetPutnika;

}
