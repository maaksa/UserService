package sk_microservices.UserService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.service.NotificationService;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.forms.RegistrationForm;
import sk_microservices.UserService.repository.UserRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@RestController
@RequestMapping("")
public class UserController {

    private BCryptPasswordEncoder encoder;
    private UserRepository userRepo;
    private NotificationService notificationService;

    @Autowired
    public UserController(BCryptPasswordEncoder encoder, UserRepository userRepo, NotificationService notificationService) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> subtractionPost(@RequestBody RegistrationForm registrationForm) {

        try {
            User user = new User(registrationForm.getIme(), registrationForm.getPrezime(), registrationForm.getEmail(),
                    encoder.encode(registrationForm.getPassword()), registrationForm.getBrojPasosa());

            User userToSend = userRepo.saveAndFlush(user);

            //send email
            notificationService.sendMail(userToSend.getEmail());

            return new ResponseEntity<>("success", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
