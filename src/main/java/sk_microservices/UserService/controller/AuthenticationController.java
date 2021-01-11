package sk_microservices.UserService.controller;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.entities.enums.Role;
import sk_microservices.UserService.entities.enums.EmailMessage;
import sk_microservices.UserService.forms.Login_Form;
import sk_microservices.UserService.forms.RegistrationForm;
import sk_microservices.UserService.service.NotificationService;
import sk_microservices.UserService.service.UserService;

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
    private NotificationService notificationService;

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
            user.setRole(Role.USER);
            user = userService.saveAndFlush(user);

            //send email
            //notificationService.sendMail(user.getEmail(), EmailMessage.REGISTER);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
