package sk_microservices.UserService.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sk_microservices.UserService.entities.CreditCard;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.forms.AddCreditCardForm;
import sk_microservices.UserService.forms.UserProfilEditForm;
import sk_microservices.UserService.repository.CreditCardRepository;
import sk_microservices.UserService.repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static sk_microservices.UserService.security.SecurityConstants.*;

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

    public String decodeToken(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING);
        if (req.getCookies() != null) {
            Cookie[] cookies = req.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("Authorization")) {
                    token = cookies[i].getValue();
                    byte[] decodedBytes = Base64.getDecoder().decode(token);
                    String decodedToken = new String(decodedBytes);
                    return decodedToken;
                }
            }
        }

        return null;
    }

    public CreditCard saveCreditCard(String token, AddCreditCardForm creditCardForm) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, ""));

        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email);

        CreditCard creditCard = new CreditCard(creditCardForm.getCardName(), creditCardForm.getCardNumber(),
                creditCardForm.getSecurityCode(), user);

        return creditCardRepository.save(creditCard);
    }

    public User getAuthentication(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, ""));

        String email = jwt.getSubject();

        if (email != null) {
            return userRepository.findByEmail(email);
        }

        return null;
    }

    public User editUser(String token, UserProfilEditForm userProfilEditForm) {

        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, ""));

        String email = jwt.getSubject();

        User user = userRepository.findByEmail(email);

        user.setBrojPasosa(userProfilEditForm.getBrojPasosa());
        user.setPrezime(userProfilEditForm.getPrezime());
        user.setIme(userProfilEditForm.getIme());

        if(!(userProfilEditForm.getEmail().equals(user.getEmail()))){
            user.setEmail(userProfilEditForm.getEmail());
            //send email
            //notificationService.sendMail(userToSend.getEmail());
            return userRepository.save(user);
        } else {
            return userRepository.save(user);
        }
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
