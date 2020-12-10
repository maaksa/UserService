package sk_microservices.UserService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk_microservices.UserService.repository.UserRepository;

@RestController
@RequestMapping("")
public class AdminController {

    @Autowired
    private UserRepository userRepo;


}
