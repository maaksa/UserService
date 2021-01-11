package sk_microservices.UserService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.repository.UserRepository;
import sk_microservices.UserService.service.AdminService;
import sk_microservices.UserService.service.UserService;

import static sk_microservices.UserService.security.SecurityConstants.HEADER_STRING;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private BCryptPasswordEncoder encoder;
    private UserService userService;

    @Autowired
    public AdminController(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @GetMapping("/checkAdmin")
    public ResponseEntity<Object> checkAdmin(@RequestHeader(value = HEADER_STRING) String token) {
        try {
            Boolean toReturn = userService.checkAdmin(token);
            return new ResponseEntity<>(toReturn, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //
//    @DeleteMapping("/deleteFlight/{id}")
//    public ResponseEntity<String> deleteFlight(@PathVariable long id) {
//
//        try {
//            ResponseEntity<String> response = UtilsMethods.sendDelete("http://localhost:8081/flight/delete/" + id);
//
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    //za GUI //todo
/*    @GetMapping("/getAllAirplanes")
    public ResponseEntity<String> getAllFlights() {

        try {
            ResponseEntity<String> response = UtilsMethods.sendGet("http://localhost:8081/airplane/list");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }*/

}
