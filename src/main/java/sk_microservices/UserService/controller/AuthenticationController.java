package sk_microservices.UserService.controller;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwt;
import lombok.extern.java.Log;
import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
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
import sk_microservices.UserService.forms.Login_Form;
import sk_microservices.UserService.forms.RegistrationForm;
import sk_microservices.UserService.service.UserService;
import sk_microservices.UserService.utils.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static sk_microservices.UserService.security.SecurityConstants.HEADER_STRING;

@Controller
@RequestMapping("")
public class AuthenticationController {

    private BCryptPasswordEncoder encoder;
    private UserService userService;

    @Autowired
    public AuthenticationController(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login-test";
    }

    /*
    @PostMapping("/authenticate")
    @ResponseBody
    public String authenticate(HttpServletRequest req, HttpServletResponse res) {
        Login_Form login_form = null;
        try {
            Gson gson = new Gson();
            login_form = gson.fromJson(req.getReader(), Login_Form.class);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login_form.getEmail(), login_form.getPassword(), java.util.Collections.emptyList()));

            String token = jwtUtil.generateToken(login_form.getEmail());
            res.addHeader("Authentication", token);
            res.setStatus(200);
        }catch (Exception e){
            e.printStackTrace();
            res.setStatus(403);
            return null;
        }
        return jwtUtil.generateToken(login_form.getEmail());
    }

     */

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

    @GetMapping("/index")
    public String index(){
        System.out.println("here");
        return "static/index";
    }

}
