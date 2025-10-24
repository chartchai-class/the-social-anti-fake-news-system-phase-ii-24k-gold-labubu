package se331.labubu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se331.labubu.entity.Role;
import se331.labubu.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
  Page<User> findByRole(Role role, Pageable pageable);
  Optional<User> findByUsername(String username);

}
