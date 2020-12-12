package sk_microservices.UserService.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk_microservices.UserService.entities.Airplane;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddFlightForm {

    private String pocetnaDestinacija;
    private String krajnjaDestinacija;
    private int duzinaLeta;
    private float cena;
    private Airplane avion;

}
