package sk_microservices.UserService.forms.ticketService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketForm {

    private Date datumKupovine;
    private long user_id;
    private long flight_id;

}
