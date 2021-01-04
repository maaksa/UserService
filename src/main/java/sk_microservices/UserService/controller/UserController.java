package sk_microservices.UserService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.entities.CreditCard;
import sk_microservices.UserService.entities.flightService.Airplane;
import sk_microservices.UserService.forms.*;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.forms.flightService.AddAirplaneForm;
import sk_microservices.UserService.security.JWTAuthenticationFilter;
import sk_microservices.UserService.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Base64;
import java.util.List;

import static sk_microservices.UserService.security.SecurityConstants.*;

@Controller
@RequestMapping("/user")
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

    @GetMapping("/showFormForUpdate")
    public String editProfil(@RequestParam("userId") long theId, Model theModel) {

        try {
            theModel.addAttribute("user", new UserProfilEditForm());
            theModel.addAttribute("userEdit", userService.findById(theId));

            return "user/edit-profile";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/saveEditProfile")
    public String saveEditProfile(HttpServletRequest req, @ModelAttribute("userEdit") User user) {
        try {

            String decodedToken = userService.decodeToken(req);
            User usr = userService.getAuthentication(decodedToken);

            userService.editUser(user, usr);

            return "redirect:/user/profile";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/showCards")
    public String showCards(HttpServletRequest req, @RequestParam("userId") long theId, Model theModel) {

        try {

            String decodedToken = userService.decodeToken(req);
            User usr = userService.getAuthentication(decodedToken);

            List<CreditCard> creditCards = usr.getCreditCards();

            theModel.addAttribute("cards", creditCards);

            return "card/list-cards";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/showFormForAddCard")
    public String showFormForAddCard(Model theModel) {
        try {

            theModel.addAttribute("card", new AddCreditCardForm());

            return "card/card-form";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @PostMapping("/saveCard")
    public String saveAirplane(HttpServletRequest req, @ModelAttribute("card") CreditCard theCreditCard) {
        try {
            String decodedToken = userService.decodeToken(req);
            User user = userService.getAuthentication(decodedToken);

            userService.saveCreditCard(user, theCreditCard);

            return "redirect:/user/profile";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }


    @GetMapping("/profile")
    public String userProfile(HttpServletRequest req, Model theModel) {

        String decodedToken = userService.decodeToken(req);
        User user = userService.getAuthentication(decodedToken);
        theModel.addAttribute("user", user);

        return "user/profile";
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

}
