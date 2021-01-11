package sk_microservices.UserService.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.entities.enums.EmailMessage;
import sk_microservices.UserService.entities.enums.Rank;
import sk_microservices.UserService.service.NotificationService;
import sk_microservices.UserService.service.UserService;
import sk_microservices.UserService.utils.UtilsMethods;

import java.util.Map;

@Component
public class Consumer {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @JmsListener(destination = "user.queue")
    public void consume(String ticket) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = mapper.readValue(ticket, Map.class);
            Integer id = (Integer)map.get("user_id");
            if(id == null){
                return;
            }
            Long user_id = Long.valueOf(id);
            Integer miles = (Integer)(map.get("miles"));
            User user = userService.findById(user_id);


            if(user == null){
                return;
            }
            //send email
            //notificationService.sendMail(user.getEmail(), EmailMessage.REFUNDS);
            user.setBrojMilja(user.getBrojMilja() - miles);

            userService.updateRunk(user);

            userService.saveUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
