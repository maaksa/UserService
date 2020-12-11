package sk_microservices.UserService.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.entities.CreditCard;
import sk_microservices.UserService.forms.AddCreditCardForm;
import sk_microservices.UserService.forms.UserProfilEditForm;
import sk_microservices.UserService.repository.CreditCardRepository;
import sk_microservices.UserService.service.NotificationService;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.forms.RegistrationForm;
import sk_microservices.UserService.repository.UserRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static sk_microservices.UserService.security.SecurityConstants.*;

@RestController
@RequestMapping("")
public class UserController {

    private BCryptPasswordEncoder encoder;
    private UserRepository userRepo;
    private CreditCardRepository creditCardRepository;
    private NotificationService notificationService;

    @Autowired
    public UserController(BCryptPasswordEncoder encoder, UserRepository userRepo, NotificationService notificationService, CreditCardRepository creditCardRepository) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
        this.creditCardRepository = creditCardRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> subtractionPost(@RequestBody RegistrationForm registrationForm) {

        try {
            User user = new User(registrationForm.getIme(), registrationForm.getPrezime(), registrationForm.getEmail(),
                    encoder.encode(registrationForm.getPassword()), registrationForm.getBrojPasosa());

            User userToSend = userRepo.saveAndFlush(user);

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
            String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                    .verify(token.replace(TOKEN_PREFIX, "")).getSubject();

            String newIme = userProfilEditForm.getIme();
            String newPrezime = userProfilEditForm.getPrezime();
            String newEmail = userProfilEditForm.getEmail();
            String newPassword = userProfilEditForm.getPassword();
            long newBrojPasosa = userProfilEditForm.getBrojPasosa();

            User user = userRepo.findByEmail(email);

            user.setIme(newIme);
            user.setPrezime(newPrezime);
            user.setBrojPasosa(newBrojPasosa);
            user.setPassword(newPassword);

            if (!newEmail.isEmpty() && !newEmail.equals(user.getEmail())) {
                user.setEmail(newEmail);
                User userToSend = userRepo.save(user);
                notificationService.sendMail(userToSend.getEmail());
            } else {
                userRepo.save(user);
            }

            return new ResponseEntity<>("successfully edited", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addCreditCard")
    public ResponseEntity<String> addCreditCard(@RequestHeader(value = HEADER_STRING) String token, @RequestBody AddCreditCardForm addCreditCardForm) {

        try {
            String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                    .verify(token.replace(TOKEN_PREFIX, "")).getSubject();

            String cardName = addCreditCardForm.getCardName();
            long cardNumber = addCreditCardForm.getCardNumber();
            int securityCode = addCreditCardForm.getSecurityCode();

            User user = userRepo.findByEmail(email);

            CreditCard creditCard = new CreditCard(cardName, cardNumber, securityCode, user);

            creditCardRepository.save(creditCard);

            return new ResponseEntity<>("successfully added", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
