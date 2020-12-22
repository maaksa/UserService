package sk_microservices.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk_microservices.UserService.entities.Admin;
import sk_microservices.UserService.entities.User;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByUsername(String username);

    Admin findByUsername(String username);

}
