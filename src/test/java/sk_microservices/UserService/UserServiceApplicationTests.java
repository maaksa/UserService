package sk_microservices.UserService;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk_microservices.UserService.entities.Admin;
import sk_microservices.UserService.entities.User;
import sk_microservices.UserService.entities.enums.Role;
import sk_microservices.UserService.repository.UserRepository;
import sk_microservices.UserService.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceApplicationTests {

	@Autowired
	UserService userService;

	@org.junit.Test
	public void saveAdminTest() throws Exception{

		User user1 = new User();
		user1.setRole(Role.ADMIN);
		user1.setEmail("boris");
		user1.setPassword("12345678");
		userService.saveUser(user1);

		User user2 = new User();
		user1.setRole(Role.ADMIN);
		user1.setEmail("maxa");
		user1.setPassword("12345678");
		userService.saveUser(user2);

	}

	@Test
	void contextLoads() {

	}

}
