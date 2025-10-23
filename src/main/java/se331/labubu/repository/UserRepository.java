package se331.labubu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se331.labubu.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

}
