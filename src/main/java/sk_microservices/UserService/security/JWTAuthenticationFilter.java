package sk_microservices.UserService.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sk_microservices.UserService.forms.JwtResponseForm;
import sk_microservices.UserService.forms.Login_Form;
import sk_microservices.UserService.repository.UserRepository;
import sk_microservices.UserService.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static sk_microservices.UserService.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        BufferedReader reader = null;
        Login_Form user = new Login_Form();
        try {
            reader = req.getReader();
            System.out.println(reader.readLine());
            Gson gson = new Gson();
            user = gson.fromJson(reader, Login_Form.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(),
                    user.getPassword(), Collections.emptyList());
            return authenticationManager.authenticate(token);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {

        String email = auth.getName();
        String token = JWT.create().withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        String toWrite = TOKEN_PREFIX + token;

        res.addHeader(HEADER_STRING, toWrite);


        res.getWriter().append(toWrite);
        res.getWriter().flush();
        res.setStatus(200);
    }

}