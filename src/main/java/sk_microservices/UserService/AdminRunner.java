package sk_microservices.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.entities.enums.Role;
import sk_microservices.UserService.service.UserService;

@Component
public class AdminRunner implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setRole(Role.ADMIN);
        user1.setEmail("boris");
        user1.setPassword(encoder.encode("12345678"));
        userService.saveUser(user1);

        User user2 = new User();
        user2.setRole(Role.ADMIN);
        user2.setEmail("maxa");
        user2.setPassword(encoder.encode("12345678"));
        userService.saveUser(user2);
    }
}
