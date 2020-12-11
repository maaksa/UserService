package sk_microservices.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk_microservices.UserService.entities.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

}
