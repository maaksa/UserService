package sk_microservices.UserService.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.entities.CreditCard;
import sk_microservices.UserService.forms.*;
import sk_microservices.UserService.repository.CreditCardRepository;
import sk_microservices.UserService.service.NotificationService;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.repository.UserRepository;
import sk_microservices.UserService.service.UserService;
import sk_microservices.UserService.utils.UtilsMethods;

import static sk_microservices.UserService.security.SecurityConstants.*;

@RestController
@RequestMapping("")
public class UserController {

    private BCryptPasswordEncoder encoder;
    private UserService userService;

    @Autowired
    public UserController(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> subtractionPost(@RequestBody RegistrationForm registrationForm) {

        try {
            User user = new User(registrationForm.getIme(), registrationForm.getPrezime(), registrationForm.getEmail(),
                    encoder.encode(registrationForm.getPassword()), registrationForm.getBrojPasosa());
            User userToSend = userService.saveAndFlush(user);

            //send email
            //notificationService.sendMail(userToSend.getEmail());

            return new ResponseEntity<>("success", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/editProfil")
    public ResponseEntity<String> editProfil(@RequestHeader(value = HEADER_STRING) String token, @RequestBody UserProfilEditForm userProfilEditForm) {

        try {
            userService.editUser(token, userProfilEditForm);
            return new ResponseEntity<>("successfully edited", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addCreditCard")
    public ResponseEntity<String> addCreditCard(@RequestHeader(value = HEADER_STRING) String token, @RequestBody AddCreditCardForm addCreditCardForm) {
        try {
            userService.saveCreditCard(token, addCreditCardForm);
            return new ResponseEntity<>("successfully added", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllFlights")
    public ResponseEntity<Object> getAllFlights() {
        try {
            ResponseEntity<Object> response = UtilsMethods.sendGet("http://localhost:8081/flight/list");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //za GUI //todo
//    @GetMapping("/getAllFlights")
//    public ResponseEntity<String> getAllFlights() {
//
//        try {
//            ResponseEntity<String> response = UtilsMethods.sendGet("http://localhost:8081/flight/list");
//
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

}
