package sk_microservices.UserService.entities.flightService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Airplane {

    private String naziv;

    private int kapacitetPutnika;

    public Airplane(String naziv, int kapacitetPutnika) {
        this.naziv = naziv;
        this.kapacitetPutnika = kapacitetPutnika;
    }

}
