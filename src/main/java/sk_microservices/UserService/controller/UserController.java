package sk_microservices.UserService.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.entities.CreditCard;
import sk_microservices.UserService.entities.enums.Rank;
import sk_microservices.UserService.forms.*;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.security.JWTAuthenticationFilter;
import sk_microservices.UserService.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static sk_microservices.UserService.security.SecurityConstants.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private BCryptPasswordEncoder encoder;
    private UserService userService;

    @Autowired
    public UserController(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @GetMapping("/getRank/{id}")
    public ResponseEntity<Object> getRank(@PathVariable long id) {
        try {
            Rank rank = userService.getMiles(id);
            return new ResponseEntity<>(rank.toString(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateMiles/{id}/{miles}")
    public ResponseEntity<String> updateMiles(@PathVariable long id, @PathVariable int miles) {
        try {
            System.out.println(miles);
            User user = userService.findById(id);
            user.setBrojMilja(user.getBrojMilja() + miles);
            userService.updateRunk(user);
            userService.saveAndFlush(user);
            return new ResponseEntity<>("Uspesno promenjen", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/editProfile")
    public ResponseEntity<String> editProfil(HttpServletRequest req, @RequestBody UserProfilEditForm userProfilEditForm) {
        try {
            String token = req.getHeader(HEADER_STRING);
            userService.editUser(token, userProfilEditForm);
            return new ResponseEntity<>("successfully edited", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/addCreditCard")
    public ResponseEntity<Object> addCreditCard(HttpServletRequest req, @RequestBody AddCreditCardForm addCreditCardForm) {
        try {
            System.out.println("adding");
            String token = req.getHeader(HEADER_STRING);
            CreditCard creditCard = userService.saveCreditCard(token, addCreditCardForm);
            AddCreditCardForm creditCardForm = new AddCreditCardForm(creditCard.getIme(), creditCard.getPrezime(),
                    creditCard.getBroj(), creditCard.getPin());
            System.out.println(creditCardForm);
            return new ResponseEntity<>(creditCardForm, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/checkUser")
    public ResponseEntity<Object> checkUser(@RequestHeader(value = HEADER_STRING) String token) {
        try {
            System.out.println("here");
            Boolean toReturn = userService.check(token);
            return new ResponseEntity<>(toReturn, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<Object> getUser(@RequestHeader(value = HEADER_STRING) String token) {
        try {
            User user = userService.getAuthentication(token);
            UserProfilEditForm toReturn = new UserProfilEditForm(user.getIme(), user.getPrezime(), user.getEmail(), user.getBrojPasosa(), user.getRole().toString(), new ArrayList<AddCreditCardForm>());
            for (CreditCard creditCard : user.getCreditCards()) {
                AddCreditCardForm creditCardForm = new AddCreditCardForm(creditCard.getIme(), creditCard.getPrezime(),
                        creditCard.getBroj(), creditCard.getPin());
                toReturn.getCards().add(creditCardForm);
            }
            return new ResponseEntity<>(toReturn, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUserId")
    public ResponseEntity<Long> getUserId(@RequestHeader(value = HEADER_STRING) String token) {
        try {
            User user = userService.getAuthentication(token);
            return new ResponseEntity<Long>(user.getId(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCreditCards")
    public ResponseEntity<List<AddCreditCardForm>> getCreditCards(@RequestHeader(value = HEADER_STRING) String token) {
        try {
            User user = userService.getAuthentication(token);
            List<AddCreditCardForm> toReturn = new ArrayList<>();
            for (CreditCard creditCard : user.getCreditCards()) {
                AddCreditCardForm creditCardForm = new AddCreditCardForm(creditCard.getIme(), creditCard.getPrezime(),
                        creditCard.getBroj(), creditCard.getPin());
                toReturn.add(creditCardForm);
            }
            return new ResponseEntity<List<AddCreditCardForm>>(toReturn, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
