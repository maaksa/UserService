package sk_microservices.UserService.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk_microservices.UserService.entities.Admin;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.repository.AdminRepository;

import static sk_microservices.UserService.security.SecurityConstants.SECRET;
import static sk_microservices.UserService.security.SecurityConstants.TOKEN_PREFIX;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    public Boolean check(String token) {
        String username = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, "")).getSubject();

        System.out.println(username);

        //todo generisanje tokena i za admin u securty
        Admin admin = adminRepository.findByUsername(username);

        if (admin == null) {
            return null;
        }

        return true;
    }

}
