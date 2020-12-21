package sk_microservices.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk_microservices.UserService.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id = :id")
    User findById(long id);

    User findByEmail(String email);

    boolean existsByEmail(String email);

}
