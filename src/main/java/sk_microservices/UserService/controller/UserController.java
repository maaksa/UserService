package sk_microservices.UserService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.forms.*;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.security.JWTAuthenticationFilter;
import sk_microservices.UserService.service.UserService;

import static sk_microservices.UserService.security.SecurityConstants.*;

@Controller
@RequestMapping("")
public class UserController {

    private BCryptPasswordEncoder encoder;
    private UserService userService;

    @Autowired
    public UserController(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    /*
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
    */

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new Login_Form());
        return "user/login";
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {

        RegistrationForm user = new RegistrationForm();
        model.addAttribute("user", user);

        return "user/register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") RegistrationForm registrationForm) {
        User user = new User(registrationForm.getIme(), registrationForm.getPrezime(), registrationForm.getEmail(),
                encoder.encode(registrationForm.getPassword()), registrationForm.getBrojPasosa());
        user = userService.saveAndFlush(user);
        return "redirect:/login";
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

    @GetMapping("/checkUser")
    public ResponseEntity<Object> getAllFlights(@RequestHeader(value = HEADER_STRING) String token) {
        try {
            Boolean toReturn = userService.check(token);
            return new ResponseEntity<>(toReturn, HttpStatus.ACCEPTED);
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
