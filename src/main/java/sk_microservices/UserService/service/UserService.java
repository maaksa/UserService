package sk_microservices.UserService.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk_microservices.UserService.entities.CreditCard;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.forms.AddCreditCardForm;
import sk_microservices.UserService.forms.UserProfilEditForm;
import sk_microservices.UserService.repository.CreditCardRepository;
import sk_microservices.UserService.repository.UserRepository;
import sk_microservices.UserService.utils.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sk_microservices.UserService.security.SecurityConstants.SECRET;
import static sk_microservices.UserService.security.SecurityConstants.TOKEN_PREFIX;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private NotificationService notificationService;

    public Boolean check(String token) {
        String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, "")).getSubject();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        return true;
    }

    public User getUser(String token){
        String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, "")).getSubject();

        return userRepository.findByEmail(email);
    }

    public CreditCard saveCreditCard(String token, AddCreditCardForm addCreditCardForm) {
        String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, "")).getSubject();

        String cardName = addCreditCardForm.getCardName();
        long cardNumber = addCreditCardForm.getCardNumber();
        int securityCode = addCreditCardForm.getSecurityCode();

        User user = userRepository.findByEmail(email);

        CreditCard creditCard = new CreditCard(cardName, cardNumber, securityCode, user);

        return creditCardRepository.save(creditCard);
    }

    public User editUser(String token, UserProfilEditForm userProfilEditForm) {

        String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, "")).getSubject();

        String newIme = userProfilEditForm.getIme();
        String newPrezime = userProfilEditForm.getPrezime();
        String newEmail = userProfilEditForm.getEmail();
        String newPassword = userProfilEditForm.getPassword();
        long newBrojPasosa = userProfilEditForm.getBrojPasosa();

        User user = userRepository.findByEmail(email);

        user.setIme(newIme);
        user.setPrezime(newPrezime);
        user.setBrojPasosa(newBrojPasosa);
        user.setPassword(newPassword);

        if (!newEmail.isEmpty() && !newEmail.equals(user.getEmail())) {
            user.setEmail(newEmail);

            //send email
            //notificationService.sendMail(userToSend.getEmail());

            user = userRepository.save(user);
            notificationService.sendMail(user.getEmail());
        } else {
            user = userRepository.save(user);
        }
        return user;
    }

    public User saveAndFlush(User user) {
        return userRepository.saveAndFlush(user);
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllByIds(List<Long> ids) {
        List<User> toReturn = new ArrayList<>();
        for (Long id : ids) {
            User user = userRepository.findById(id.longValue());
            if (user != null) {
                toReturn.add(user);
            }
        }
        return toReturn;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
