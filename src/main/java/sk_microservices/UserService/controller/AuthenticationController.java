package sk_microservices.UserService.controller;

import com.auth0.jwt.JWT;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwt;
import lombok.extern.java.Log;
import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.forms.JwtResponseForm;
import sk_microservices.UserService.forms.Login_Form;
import sk_microservices.UserService.forms.RegistrationForm;
import sk_microservices.UserService.service.UserService;
import sk_microservices.UserService.utils.JwtUtil;

import javax.validation.Valid;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static sk_microservices.UserService.security.SecurityConstants.*;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private BCryptPasswordEncoder encoder;
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(BCryptPasswordEncoder encoder, UserService userService,
                                    AuthenticationManager authenticationManager) {
        this.encoder = encoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody Login_Form login_form) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login_form.getEmail(), login_form.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JWT.create().withSubject(login_form.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        String toWrite = TOKEN_PREFIX + token;

        return ResponseEntity.ok(toWrite);
    }


    @GetMapping("/login")
    public String login() {
        return "user/login-test";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUserAccount(@RequestBody RegistrationForm registrationForm) {
        try {
            User user = new User(registrationForm.getIme(), registrationForm.getPrezime(), registrationForm.getEmail(),
                    encoder.encode(registrationForm.getPassword()), registrationForm.getBrojPasosa());
            user = userService.saveAndFlush(user);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
