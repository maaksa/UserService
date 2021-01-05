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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Base64;

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
    public ResponseEntity<Object> checkUser(@RequestHeader(value = HEADER_STRING) String token) {
        try {
            Boolean toReturn = userService.check(token);
            return new ResponseEntity<>(toReturn, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<Object> getUser(@RequestHeader(value = HEADER_STRING) String token){
        try {
            User toReturn = userService.getAuthentication(token);
            return new ResponseEntity<>(toReturn, HttpStatus.ACCEPTED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
