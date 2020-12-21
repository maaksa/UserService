package sk_microservices.UserService.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.service.UserService;
import sk_microservices.UserService.utils.UtilsMethods;

import java.util.Map;

@Component
public class Consumer {

    @Autowired
    private UserService userService;

    @JmsListener(destination = "user.queue")
    public void consume(String ticket) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = mapper.readValue(ticket, Map.class);
            Long user_id = Long.valueOf((Integer)map.get("user_id"));
            Long flight_id = Long.valueOf((Integer)map.get("flight_id"));
            User user = userService.findById(user_id);
            ResponseEntity<Object> response = UtilsMethods.sendGet("http://localhost:8081/flight/miles/" + flight_id);
            if (response.getBody() == null) {
                return;
            }
            int miles = (int) response.getBody();
            user.setBrojMilja(user.getBrojMilja() - miles);
            //todo promeni rank
            userService.saveUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
