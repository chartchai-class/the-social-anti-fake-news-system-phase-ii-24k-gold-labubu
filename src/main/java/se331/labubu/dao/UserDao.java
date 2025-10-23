package se331.labubu.dao;

import se331.labubu.entity.User;

public interface UserDao {
    User findByUsername(String username);

    User save(User user);
}