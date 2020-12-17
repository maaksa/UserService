package sk_microservices.UserService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sk_microservices.UserService.forms.flightService.AddAirplaneForm;
import sk_microservices.UserService.forms.flightService.AddFlightForm;
import sk_microservices.UserService.repository.CreditCardRepository;
import sk_microservices.UserService.repository.UserRepository;
import sk_microservices.UserService.service.NotificationService;
import sk_microservices.UserService.utils.UtilsMethods;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private BCryptPasswordEncoder encoder;
    private UserRepository userRepo;
    private CreditCardRepository creditCardRepository;
    private NotificationService notificationService;

    @Autowired
    public AdminController(BCryptPasswordEncoder encoder, UserRepository userRepo, NotificationService notificationService, CreditCardRepository creditCardRepository) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
        this.creditCardRepository = creditCardRepository;
    }

    @PostMapping("/addFlight")
    public ResponseEntity<String> addFlight(@RequestBody AddFlightForm addFlightForm) {

        try {
            ResponseEntity<String> response = UtilsMethods.sendPost("http://localhost:8081/flight/save", addFlightForm);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addAirplane")
    public ResponseEntity<String> addAirplane(@RequestBody AddAirplaneForm addAirplaneForm) {

        try {
            ResponseEntity<String> response = UtilsMethods.sendPost("http://localhost:8081/airplane/save", addAirplaneForm);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getAllAirplanes")
    public ResponseEntity<Object> getAllAirplanes() {

        try {
            ResponseEntity<Object> response = UtilsMethods.sendGet("http://localhost:8081/airplane/list");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllFlights")
    public ResponseEntity<Object> getAllFlights() {

        try {
            ResponseEntity<Object> response = UtilsMethods.sendGet("http://localhost:8081/airplane/allFlights");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteAirplane/{id}")
    public ResponseEntity<String> deleteAirplane(@PathVariable long id) {

        try {
            ResponseEntity<String> response = UtilsMethods.sendDelete("http://localhost:8081/airplane/delete/" + id);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteFlight/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable long id) {

        try {
            ResponseEntity<String> response = UtilsMethods.sendDelete("http://localhost:8081/flight/delete/" + id);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
