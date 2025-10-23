package se331.labubu.service;

import jakarta.transaction.Transactional;
import se331.labubu.entity.User;

public interface UserService {
    User save(User user);

    @Transactional
    User findByUsername(String username);
}