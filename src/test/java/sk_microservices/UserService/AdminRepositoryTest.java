package sk_microservices.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk_microservices.UserService.entities.Admin;
import sk_microservices.UserService.repository.AdminRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    @Test
    public void saveAdminTest() throws Exception{

        Admin admin1 = new Admin("maxa", "111");

        Admin admin2 = new Admin("boris", "222");

        adminRepository.save(admin1);
        adminRepository.save(admin2);

    }

}
